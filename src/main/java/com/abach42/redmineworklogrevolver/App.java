package com.abach42.redmineworklogrevolver;

import com.abach42.redmineworklogrevolver.Context.Context;
import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;
import com.abach42.redmineworklogrevolver.ProcedureChain.AbstractProcedureHandler;
import com.abach42.redmineworklogrevolver.ProcedureChain.AppInitializeHandler;
import com.abach42.redmineworklogrevolver.ProcedureChain.WorklogHandler;
import com.abach42.redmineworklogrevolver.ProcedureChain.ChooseTimeRangeHandler;
import com.abach42.redmineworklogrevolver.ProcedureChain.RevolverIdHandler;
import com.abach42.redmineworklogrevolver.ProcedureChain.ResultHandler;

/*
 * Show user options of time ranges, posibility to choose of it, 
 * calling API to get work log entries in chosen time range, 
 * searching for special revolver id number sliding up ticket tree, 
 * sorting it, summing up working time by this very revolver id number. 
 */
public final class App {

    public static void main(String... args) {
        ContextInterface context = new Context();
    
        AbstractProcedureHandler handler = getHandler(context);
        
        try {
            handler.handle();
        } catch(ApplicationException e) {
            new UserOutput().writeException("Application aborted: " + e.getMessage());
            System.exit(0);
        }
    }

    protected static AbstractProcedureHandler getHandler(ContextInterface context) {
        return AbstractProcedureHandler.initializeEndlessChain(
            new AppInitializeHandler(context),
            new ChooseTimeRangeHandler(context),
            new WorklogHandler(context),
            new RevolverIdHandler(context),
            new ResultHandler(context));
    } 
}