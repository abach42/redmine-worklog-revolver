package com.abach42.redmineworklogrevolver.ProcedureChain;

import java.util.Arrays;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Display.TerminalInputable;
import com.abach42.redmineworklogrevolver.Display.UserInput;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;
import com.abach42.redmineworklogrevolver.Exception.IllegalCommandKeyException;
import com.abach42.redmineworklogrevolver.TimeRangeFactory.TimeRangeFactory;
import com.abach42.redmineworklogrevolver.TimeRangeFactory.TimeRangeFactoryInterface;
import com.abach42.redmineworklogrevolver.TimeRangeFactory.TimeRangeable;

/*
 * Contact user to show options, get option from user and fullfill.
 */
public class ChooseTimeRangeHandler extends AbstractProcedureHandler {
    public static final String VOCATIVE_MSG = "Enter a command: ";
    public static final String WRONG_INTPUT_MSG = "Entered key not valid.";
    public static final String USER_EXIT_MSG = "User exit.";
    public static final int USER_EXIT_CHOICE = 0;
    public static final String USER_TO_CHOSE_MSG = "(%d) for %s";
    public static final String USER_CHOSEN_MSG = "... starting redmine api for %s ...";
    public static final String USER_DEFAULT_MSG = "[%d %s]: ";
    public static final String USER_TO_EXIT_MSG = "(%d) for ending application.";

    protected UserOutput output; 

    public ChooseTimeRangeHandler(ContextInterface context) {
        super(context);

        output = new UserOutput();
    }
    
    @Override
    public void handle() {
        TerminalInputable input = new UserInput();
        output.write(VOCATIVE_MSG);

        printOptionsInConsole();

        //TODO Stop of chain by input console, risky.
        Integer inputKey = input.getIntFromUser();

        try {
            if(inputKey == USER_EXIT_CHOICE) {
                //programm flow by exception: avoid other dependencies
                throw new ApplicationException(USER_EXIT_MSG);
            }
            
            TimeRangeFactoryInterface timeRangeFactory = new TimeRangeFactory();
            TimeRangeable timeRange = timeRangeFactory.getTimeRange(inputKey);

            output.writeNotice(String.format(USER_CHOSEN_MSG, timeRange.toString()));
            
            context.getApiDemand().setFrom(timeRange.getFrom());
            context.getApiDemand().setTo(timeRange.getTo());

        } catch (IllegalCommandKeyException e) {
            output.writeException(WRONG_INTPUT_MSG);
            
        }

        handleNext();
    }

    private void printOptionsInConsole() {
        TimeRangeFactoryInterface timeRangeFactory = new TimeRangeFactory();

        Arrays.stream(TimeRangeFactory.TimeRangeTypes.values())
            .forEach(
                timeRangeType -> 
                    output.write(
                        String.format(
                            USER_TO_CHOSE_MSG, 
                            timeRangeType.inputKey, 
                            timeRangeFactory.getTimeRange(timeRangeType.inputKey).toString()),
                        10));
        
       output.write(String.format(USER_TO_EXIT_MSG, USER_EXIT_CHOICE));

       output.writeInLine(
            String.format(
                USER_DEFAULT_MSG, 
                UserInput.DEFAULT_INPUT_INT, 
                timeRangeFactory.getTimeRange(UserInput.DEFAULT_INPUT_INT).toString()));
    }
    
}
