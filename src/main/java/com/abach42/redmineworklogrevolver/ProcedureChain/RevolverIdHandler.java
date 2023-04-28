package com.abach42.redmineworklogrevolver.ProcedureChain;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.abach42.redmineworklogrevolver.ApiAdapter.ApiRequest;
import com.abach42.redmineworklogrevolver.ApiAdapter.JsonFormatter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RedmineAdaptee;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdRedmineAdapter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdTargetInterface;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
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

        fetchRevolverIdsConcurrent(list);
 
        handleNext();
    }

    protected void fetchRevolverIdsConcurrent(WorklogList list) {
        ExecutorService executor = getExecutor();
        CompletionService<Worklog> service = getService(executor);

        RevolverIdTargetInterface target = setupAdapter();

        for (Worklog worklog : list) {
            service.submit(() -> {
                return addRevolverIdToWorlog(worklog, target);
            });
        }

        for (int i = 0; i < list.size(); i++) {
            waitAndCollectResult(list, service, i);
        }

        stopExecutor(executor);
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
    
    protected void waitAndCollectResult(WorklogList list, CompletionService<Worklog> service, int i) {
        try {
            service.take();
        } catch (InterruptedException e) {
            throw new ApplicationException(e.getCause().getMessage());
        }
        new UserOutput().drawProgessBar(i+1, list.size());
    }

    protected void stopExecutor(ExecutorService executor) {
        executor.shutdown();
    }
}
