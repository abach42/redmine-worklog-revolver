package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Entity.Worklog;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;

/*
 * Adaption to receive {@code WorklogList}. 
 * Works with an Adaptee, the demand object (wich is changed during pagination), and a json formatter
 * interface, to keep out JSON-Convertion details. 
 */
public class WorklogRedmineAdapter extends AbstractRedmineAdapter implements WorklogTargetInterface {

    public WorklogRedmineAdapter(RedmineAdaptee adaptee, JsonInterface jsonFormatter) {
        super(adaptee, jsonFormatter);
    }

    @Override
    public WorklogList listWorklog(ApiDemand apiDemand) {
        JSONArray jsonArray = processResultPages(apiDemand);
        
        WorklogList worklogList = buildWorklogList(jsonArray);
        
        return worklogList;
    }

    protected JSONArray processResultPages(ApiDemand apiDemand) {
        int count = 1;

        JSONArray timeEntriesArray = new JSONArray();

        while (apiDemand.getOffset() < count) {
            try {
                LinkedHashMap<String, String> query = composeQuery(apiDemand);
                String response = sendRequest(query, apiDemand.getApiKey());

                JSONObject json = convertResponse(response);
                timeEntriesArray.putAll(extractTimeEntries(json));
                
                count = paginateDemand(apiDemand, json);

            } catch (ApiRequestException e) {
                throw new ApplicationException(e.getMessage());

            }
        }
        
        return timeEntriesArray;
    }

    protected LinkedHashMap<String, String> composeQuery(ApiDemand apiDemand) {
        LinkedHashMap<String, String> segments = new LinkedHashMap<>();
    
        segments.put(apiDemand.getRequestUrl(), RedmineAdaptee.LIST_MY_TIME_ENTRIES_OPERATION_ID);
        segments.put(RedmineAdaptee.PARAM_FROM, dateFormatForRequest(apiDemand.getFrom()));
        segments.put(RedmineAdaptee.PARAM_TO, dateFormatForRequest(apiDemand.getTo()));
        
        segments.put(RedmineAdaptee.PARAM_LIMIT, apiDemand.getLimit().toString());
        segments.put(RedmineAdaptee.PARAM_OFFSET, apiDemand.getOffset().toString());

        return segments;
    }

    protected JSONArray extractTimeEntries(JSONObject json) {
        return jsonFormatter.getJsonArray(RedmineAdaptee.SUBKEY_TIME_ENTRIES, json);
    }

    protected WorklogList buildWorklogList(JSONArray timeEntries) {
        WorklogList worklogList = new WorklogList();

        for (int i = 0; i < timeEntries.length(); i++) { 
            JSONObject timeEntry = timeEntries.getJSONObject(i);

            int id = jsonFormatter.getInt(RedmineAdaptee.SUBKEY_ID, jsonFormatter.getJsonObject(RedmineAdaptee.SUBKEY_ISSUE, timeEntry));
            LocalDate date = LocalDate.parse(jsonFormatter.getString(RedmineAdaptee.SUBKEY_SPENT_ON, timeEntry));
            double hours = jsonFormatter.getDouble(RedmineAdaptee.SUBKEY_HOURS, timeEntry);
   
            worklogList.add(new Worklog(id, date, hours));
        }

        return worklogList;
    }

    protected int paginateDemand(ApiDemand apiDemand, JSONObject json) {
        apiDemand.setOffset(apiDemand.getOffset() + convertLimit(json));
        return jsonFormatter.getInt(RedmineAdaptee.SUBKEY_TOTAL_COUNT, json);
    }    
    
    protected int convertLimit(JSONObject json) {
        return jsonFormatter.getInt(RedmineAdaptee.SUBKEY_LIMIT, json);
    }
    
    protected String dateFormatForRequest(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(RedmineAdaptee.DATE_FORMAT);
        return date.format(dateFormatter); 
    }
}
