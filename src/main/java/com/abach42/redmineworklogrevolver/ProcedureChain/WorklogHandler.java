package com.abach42.redmineworklogrevolver.ProcedureChain;

import com.abach42.redmineworklogrevolver.ApiAdapter.ApiRequest;
import com.abach42.redmineworklogrevolver.ApiAdapter.JsonFormatter;
import com.abach42.redmineworklogrevolver.ApiAdapter.RedmineAdaptee;
import com.abach42.redmineworklogrevolver.ApiAdapter.WorklogRedmineAdapter;
import com.abach42.redmineworklogrevolver.ApiAdapter.WorklogTargetInterface;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.UserInput;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Exception.EmptyResultException;
import com.abach42.redmineworklogrevolver.Exception.WrongAccessKeyException;
import com.abach42.redmineworklogrevolver.Init.ConfigFileConnector;
import com.abach42.redmineworklogrevolver.Init.InitializeApp;

/*
 * Build up, finalize api call parameters
 */
public class WorklogHandler extends AbstractProcedureHandler {

    public WorklogHandler(ContextInterface context) {
        super(context);
    }

    @Override
    public void handle() {
        if(!context.getAppConfig().isInitialized()) {
            handleNext();
        }

        //TODO test is reset?
        context = context.resetApiDemand();
        
        try {
            WorklogTargetInterface target = new WorklogRedmineAdapter(
                new RedmineAdaptee(new ApiRequest()), 
                new JsonFormatter()
            );
            
            WorklogList worklogList = target.listWorklog(context.getApiDemand());
            context.setWorklogList(worklogList);

        } catch (WrongAccessKeyException e) {
            (new InitializeApp(new UserInput(), new ConfigFileConnector()))
                .withContext(context)
                .reInitializeAccessKey();

        } catch (EmptyResultException e) {
            new UserOutput().write(e.getMessage());
            
        }
        
        handleNext();
    }
}
