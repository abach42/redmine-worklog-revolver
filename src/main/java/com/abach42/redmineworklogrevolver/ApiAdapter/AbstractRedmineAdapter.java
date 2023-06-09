package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.util.LinkedHashMap;
import org.json.JSONObject;

public abstract class AbstractRedmineAdapter {

    protected RedmineAdaptee adaptee;
    protected JsonExtractable jsonFormatter;
    
    public AbstractRedmineAdapter(RedmineAdaptee adaptee, JsonExtractable jsonFormatter) {
        this.adaptee = adaptee;
        this.jsonFormatter = jsonFormatter;
    }
    
    protected String sendRequest(LinkedHashMap<String, String> urlSegments, String apiKey) {
        return adaptee.getResult(urlSegments, apiKey);
    }

    protected JSONObject convertResponse(String response) {
        return jsonFormatter.convertToJson(response);
    }
}
