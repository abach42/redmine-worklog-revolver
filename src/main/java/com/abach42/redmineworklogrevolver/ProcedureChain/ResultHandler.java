package com.abach42.redmineworklogrevolver.ProcedureChain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.abach42.redmineworklogrevolver.Context.ContextInterface;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Display.UserOutput;
import com.abach42.redmineworklogrevolver.Entity.Worklog;

/*
 * Display result
 */
public class ResultHandler extends AbstractProcedureHandler {

    protected static final String LABEL_REVOLVER_ID = "Revolver Identifier";
    protected static final String LABEL_HOURS = "Hours";
    protected static final String LABEL_DATE = "Date";
    protected static final String LABEL_TOTAL_HOUERS = "Total hours";

    public ResultHandler(ContextInterface context) {
        super(context);
    }

    @Override
    public void handle() {

        if (context.getWorklogList().size() == 0) {
            handleNext();
        }

        printGroupedTableSimple(
            groupWorklogs(context.getWorklogList()),
            collectIds(context.getWorklogList())
        );

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

    protected Map<LocalDate, Map<String, String>> collectIds(WorklogList listOfWorklogs) {
        Map<LocalDate, Map<String, String>> groupedWorklogsIds = listOfWorklogs.stream()
        .collect(Collectors.groupingBy(Worklog::getDate,
                Collectors.groupingBy(Worklog::getRevolverIdentifier,
                        Collectors.mapping(worklog -> String.valueOf(worklog.getId()), Collectors.joining(", "))
                )
        ));

        return groupedWorklogsIds;
    }

    private void printGroupedTableSimple(
        Map<LocalDate, Map<String, Double>> groupedWorklogs, 
        Map<LocalDate, Map<String, String>> groupedWorklogsIds
    ) {
        UserOutput output = new UserOutput();

        output.addLineFeeds(2);

        for (Map.Entry<LocalDate, Map<String, Double>> entry : groupedWorklogs.entrySet()) {
            Double totalHours = 0.0;
            LocalDate date = entry.getKey();
            Map<String, Double> worklogsForDate = entry.getValue();

            output.addLineFeeds(2);

            printHeader(output, date);

            for (Map.Entry<String, Double> innerEntry : worklogsForDate.entrySet()) {
                String revolverIdentifier = innerEntry.getKey().substring(0, Math.min(innerEntry.getKey().length(), 35));
                Double hours = innerEntry.getValue();
                totalHours += hours;
                printRevolverIdAndDuration(output, revolverIdentifier, hours);
                
                String issueIdList = " Issues: " + groupedWorklogsIds.get(date).get(innerEntry.getKey());
                printIssuesList(output, issueIdList); 
            }

            printFooter(output, totalHours);

            output.wait(20);
        }

        output.addLineFeeds(2);

        output.wait(1000);
    }

    private void printHeader(UserOutput output, LocalDate date) {
        output.write(printTableLine());

        String dateString = date.format(DateTimeFormatter.ofPattern(context.getAppConfig().getDatePattern()));
        output.write(String.format("| %s: %-30s | %-5s |", LABEL_DATE, dateString, ""));
        output.write(String.format("| %-36s | %-5s |", LABEL_REVOLVER_ID, LABEL_HOURS));

        output.write(printTableLine());
    }

    private void printRevolverIdAndDuration(UserOutput output, String revolverIdentifier, Double hours) {
        output.write(String.format("| %-36s | %-5.2f |", revolverIdentifier, hours));
    }

    private void printIssuesList(UserOutput output, String issueIdList) {
        int maxLength = 36; // Maximum length of each line before line break
        String[] formattedIssuesArray = IntStream.range(0, (issueIdList.length() + maxLength - 1) / maxLength)
            .mapToObj(i -> issueIdList.substring(i * maxLength, Math.min((i + 1) * maxLength, issueIdList.length())))
            .toArray(String[]::new);

        for(String issues : formattedIssuesArray) {
            output.write(String.format("| %-36s | %-5.2s |", issues, ""));
        }
    }

    private void printFooter(UserOutput output, Double totalHours) {
        output.write(printTableLine());
        output.write(String.format("| %-36s | %-5.2f |", LABEL_TOTAL_HOUERS, totalHours));
        output.write(printTableLine());
    }

    private String printTableLine() {
        return String.format("+ %-36s + %-5s +", "------------------------------------", "-----");
    }
}
