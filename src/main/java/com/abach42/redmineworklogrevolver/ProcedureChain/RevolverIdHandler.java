package com.abach42.redmineworklogrevolver.ProcedureChain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.abach42.redmineworklogrevolver.ApiAdapter.ApiRequest;
import com.abach42.redmineworklogrevolver.ApiAdapter.JsonFormatter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RedmineAdaptee;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdRedmineAdapter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdTargetInterface;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.ProgressBar;
import com.abach42.redmineworklogrevolver.Entity.Worklog;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;

/*
 * Search for Revolver Ids of worklog entries, concurrently.
 */
public class RevolverIdHandler extends AbstractProcedureHandler{

    public RevolverIdHandler(ContextInterface context) {
        super(context);
    }

    @Override
    public void handle() {
        if(!context.getAppConfig().isInitialized()) {
            handleNext();
        }

        if(context.getWorklogList().size() == 0) {
            handleNext();
        }

        context = context.resetApiDemand();
        WorklogList list = context.getWorklogList();

        WorklogList listWithIds = fetchRevolverIdsConcurrent(list);
        context.setWorklogList(listWithIds);
 
        handleNext();
    }

    protected WorklogList fetchRevolverIdsConcurrent(WorklogList list) {
        ExecutorService executor = getExecutor();
        CompletionService<Worklog> service = getService(executor);

        List<Future<Worklog>> futures = submitRevolverIdJobs(list, service);

        WorklogList listWithIds = waitAndCollectResult(futures);
        stopExecutor(executor);

        return listWithIds;
    }

    private List<Future<Worklog>> submitRevolverIdJobs(WorklogList list, CompletionService<Worklog> service) {
        RevolverIdTargetInterface target = setupAdapter();

        List<Future<Worklog>> futures = new ArrayList<>();
        
        for (Worklog worklog : list) {
            futures.add(service.submit(() -> {
                return addRevolverIdToWorlog(worklog, target);
            }));
        }
        return futures;
    }

    protected RevolverIdTargetInterface setupAdapter() {
        return new RevolverIdRedmineAdapter(
                new RedmineAdaptee(new ApiRequest()),
                new JsonFormatter()
            );
    }

    protected ExecutorService getExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    protected ExecutorCompletionService<Worklog> getService(ExecutorService executorService) {
        return new ExecutorCompletionService<>(executorService);
    }

    protected Worklog addRevolverIdToWorlog(Worklog worklog, RevolverIdTargetInterface target) {
        context.getApiDemand().setIssueId(worklog.getId());
        String revolverId = target.singleRevolverId(context.getApiDemand());
        worklog.setRevolverIdentifier(revolverId);
        return worklog;
    }
    
    protected WorklogList waitAndCollectResult(List<Future<Worklog>> futures) {
        WorklogList listWithIds = new WorklogList();
        
        ProgressBar progressBar = new ProgressBar(futures.size());
        
        for (Future<Worklog> future : futures) {
            try {
                listWithIds.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationException(e.getMessage());
            }
            progressBar.incrementProgressIterator();
            progressBar.drawProgessBar();
        }

        return listWithIds;
    }

    protected void stopExecutor(ExecutorService executor) {
        executor.shutdown();
    }
}
