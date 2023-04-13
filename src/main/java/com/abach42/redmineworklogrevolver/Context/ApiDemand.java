package com.abach42.redmineworklogrevolver.Context;

import java.time.LocalDate;

/*
 * Contains parameters for API call, can be changed on runtime, set initially by App-Config and User Input
 */
public class ApiDemand {
    
    public final static int DEFAULT_OFFSET = 0; 

    private String requestUrl;

    private LocalDate from;

    private LocalDate to; 

    private Integer issueId;
    
    private Integer limit;

    private Integer offset = DEFAULT_OFFSET;

    private String apiKey;
    
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }
    
    public Integer getIssueId() {
        return issueId;
    }

    public void setIssueId(Integer issueId) {
        this.issueId = issueId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
