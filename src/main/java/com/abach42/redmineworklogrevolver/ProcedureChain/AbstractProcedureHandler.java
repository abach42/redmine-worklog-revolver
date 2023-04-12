package com.abach42.redmineworklogrevolver.ProcedureChain;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;

/*
 * Head of chain of responsibility to backbone application. 
 */
public abstract class AbstractProcedureHandler {
    protected AbstractProcedureHandler next;
    protected ContextInterface context;

    public AbstractProcedureHandler(ContextInterface context) {
        this.context = context;
    }

    protected void setNext(AbstractProcedureHandler next) {
        this.next = next;
    }

    public static AbstractProcedureHandler initializeEndlessChain(AbstractProcedureHandler first, AbstractProcedureHandler... chain) {
        AbstractProcedureHandler head = first;
        for (AbstractProcedureHandler nextLinkInChain: chain) {
            head.setNext(nextLinkInChain);
            head = nextLinkInChain;
        }
        
        //set endless loop: last head gets reference of first link.
        head.setNext(first);
        
        return first;
    }

    public abstract void handle();

    protected void handleNext() {
        if (next != null) {
            next.handle();
        }
    }
}
