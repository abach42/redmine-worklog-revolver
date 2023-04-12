package com.abach42.redmineworklogrevolver.ProcedureChain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Entity.Worklog;

/*
 * Display result
 */
public class ResultHandler extends AbstractProcedureHandler{

    public ResultHandler(ContextInterface context) {
        super(context);
    }

    @Override
    public void handle() {

        if(context.getWorklogList().size() == 0) {
            handleNext();
        }

        printGroupedTableSimple(groupWorklogs(context.getWorklogList()));
        
        handleNext();
    }

    protected Map<LocalDate, Map<String, Double>> groupWorklogs(WorklogList listOfWorklogs) {
		Map<LocalDate, Map<String, Double>> groupedWorklogs = listOfWorklogs.stream()
			.collect(Collectors.groupingBy(Worklog::getDate,
				Collectors.groupingBy(Worklog::getRevolverIdentifier,
					Collectors.summingDouble(Worklog::getHours))));

		Map<LocalDate, Map<String, Double>> sortedMap = new TreeMap<>(groupedWorklogs);
		
		return sortedMap;
	}

    private void printGroupedTableSimple(Map<LocalDate, Map<String, Double>> groupedWorklogs) {
		UserOutput out = new UserOutput();

		out.consoleLog("");
		
		for (Map.Entry<LocalDate, Map<String, Double>> entry : groupedWorklogs.entrySet()) {
			Double totalHours = 0.0;
		    LocalDate date = entry.getKey();
		    Map<String, Double> worklogsForDate = entry.getValue();
		    
			out.consoleLog("");
			out.consoleLog("");

		    out.consoleLog(String.format("+ %-35s + %-5s +", "-----------------------------------", "-----"),3);
		   
			String dateString = date.format(DateTimeFormatter.ofPattern(context.getAppConfig().getDatePattern()));
		    out.consoleLog(String.format("| Date: %-29s | %-5s |", dateString, ""));
		    out.consoleLog(String.format("| %-35s | %-5s |", "Revolver Identifier", "Hours"));
		    
		    out.consoleLog(String.format("+ %-35s + %-5s +", "-----------------------------------", "-----"));
		    
		    for (Map.Entry<String, Double> innerEntry : worklogsForDate.entrySet()) {
		        String revolverIdentifier = innerEntry.getKey();
		        Double hours = innerEntry.getValue();
		        totalHours += hours;
		        out.consoleLog(String.format("| %-35s | %-5.2f |", revolverIdentifier, hours));
		    }
		    
		    out.consoleLog(String.format("+ %-35s + %-5s +", "-----------------------------------", "-----"));
		    out.consoleLog(String.format("| %-35s | %-5.2f |", "Total hours", totalHours));
		    out.consoleLog(String.format("+ %-35s + %-5s +", "-----------------------------------", "-----"),3);
		    
			out.wait(100);
		}
		
		out.consoleLog("");
		out.consoleLog("");

		out.wait(1000);
	}
}
