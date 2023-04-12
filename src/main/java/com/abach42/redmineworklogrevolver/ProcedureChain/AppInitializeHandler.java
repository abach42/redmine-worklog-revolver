package com.abach42.redmineworklogrevolver.ProcedureChain;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Display.UserInput;
import com.abach42.redmineworklogrevolver.Init.ConfigFileConnector;
import com.abach42.redmineworklogrevolver.Init.InitializeApp;

/*
 * Initialize app by config file
 */
public class AppInitializeHandler extends AbstractProcedureHandler{

    public AppInitializeHandler(ContextInterface context) {
        super(context);
    }

    @Override
    public void handle() {
        if(context.getAppConfig().isInitialized()) {
            handleNext();
        }
        
        new InitializeApp(new UserInput(), new ConfigFileConnector())
            .withContext(context)
            .initialize();

        //TODO Verify by test
        context.getAppConfig().setInitialized();

        handleNext();
    }
}
