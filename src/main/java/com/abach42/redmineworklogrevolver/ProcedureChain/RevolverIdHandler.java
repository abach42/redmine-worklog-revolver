package com.abach42.redmineworklogrevolver.ProcedureChain;

import com.abach42.redmineworklogrevolver.ApiAdapter.ApiRequest;
import com.abach42.redmineworklogrevolver.ApiAdapter.JsonFormatter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RedmineAdaptee;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdRedmineAdapter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RevolverIdTargetInterface;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Entity.Worklog;

/*
 * Display result
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

        RevolverIdTargetInterface target = new RevolverIdRedmineAdapter(
                new RedmineAdaptee(new ApiRequest()),
                new JsonFormatter()
            );
        
        WorklogList list = context.getWorklogList();
        int progress = 0;
        for (Worklog worklog : list) {
            context.getApiDemand().setIssueId(worklog.getId());
            String revolverId = target.singleRevolverId(context.getApiDemand());

            worklog.setRevolverIdentifier(revolverId);

            new UserOutput().writeProgressBar(++progress, list.size());
        }

        handleNext();
    }
}
