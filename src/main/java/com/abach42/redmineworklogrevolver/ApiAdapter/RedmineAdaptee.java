package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

public class RedmineAdaptee {

    //Request Parameters
    public static final String LIST_MY_TIME_ENTRIES_OPERATION_ID = "/time_entries.json?user_id=me";
    public static final String SINGLE_ISSUE_OPERATION_ID = "/issues.json?status_id=*";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String PARAM_FROM = "&from=";
	public static final String PARAM_TO = "&to=";
    public static final String PARAM_OFFSET = "&offset=";
	public static final String PARAM_LIMIT = "&limit=";
	public static final String PARAM_ISSUE_ID = "&issue_id=";

    protected static final String HEADER_AUTH_KEY = "X-Redmine-API-Key";
	protected static final String HEADER_CONTENT_TYPE_KEY = "content-type";
	protected static final String HEADER_CONTENT_TYPE_VALUE = "application/json";

    //Response Parameters
    public static final String SUBKEY_TIME_ENTRIES = "time_entries";
    public static final String SUBKEY_TOTAL_COUNT = "total_count";
    public static final String SUBKEY_LIMIT = "limit";
    public static final String SUBKEY_ISSUE = "issue";
    public static final String SUBKEY_ISSUES = "issues";
    public static final String SUBKEY_ID = "id";
    public static final String SUBKEY_SPENT_ON = "spent_on";
    public static final String SUBKEY_HOURS = "hours";
    public static final String SUBKEY_PARENT = "parent";
    public static final String SUBKEY_NAME = "name";
    public static final String SUBKEY_REVOLVER_ID = "Revolver-ID";
    public static final String SUBKEY_VALUE = "value";
    public static final String SUBKEY_CUSTOM_FIELDS = "custom_fields";

    
    protected ApiRequestable apiRequest; 

    public RedmineAdaptee(ApiRequestable apiRequest) {
        this.apiRequest = apiRequest;
    }

    public String getResult(LinkedHashMap<String, String> urlSegments, String apiKey) {
        String url = concatenateSegments(urlSegments);

        apiRequest.withParameter(
            url, 
            HEADER_AUTH_KEY,
            apiKey,
            HEADER_CONTENT_TYPE_KEY,
            HEADER_CONTENT_TYPE_VALUE
        );
        
        return getResponseBody();
    }

    protected String concatenateSegments(LinkedHashMap<String, String> urlSegments) {
        StringBuilder concatenatedString = new StringBuilder();
        for (Map.Entry<String, String> entry : urlSegments.entrySet()) {
            concatenatedString.append(entry.getKey()).append(entry.getValue());
        }

        return concatenatedString.toString();
    }

    protected String getResponseBody() {
        return apiRequest.handleRequest().body();
    }
}
