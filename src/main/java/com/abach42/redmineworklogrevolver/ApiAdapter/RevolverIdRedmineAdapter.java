package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.OptionalInt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;

/*
 * Searches for Revolver ID, starting with issue id of the origin, if not present, sliding up to parent, 
 * finally giving up returning "not provided..." 
 * 
 * TODO test Optionals
 */
public class RevolverIdRedmineAdapter extends AbstractRedmineAdapter implements RevolverIdTargetInterface {
    protected static final String NOT_PROVIDED = "not provided for #";

    public RevolverIdRedmineAdapter(RedmineAdaptee adaptee, JsonExtractable jsonFormatter) {
        super(adaptee, jsonFormatter);
    }

    @Override
    public String singleRevolverId(ApiDemand apiDemand) {
        try {
            return searchForRevolverId(apiDemand);
        } catch  (ApiRequestException e) {
            throw new ApplicationException(e.getMessage());

        }
    }

    protected String searchForRevolverId(ApiDemand apiDemand) {
        JSONArray issue;
        OptionalInt parentIssueId;
        Optional<String> revolverId;
        Integer originalIssueId = apiDemand.getIssueId();

        do {
            LinkedHashMap<String, String> query = composeQuery(apiDemand);
            String response = sendRequest(query, apiDemand.getApiKey());
            
            issue = getIssue(response);
            revolverId = extractRevolerId(issue);

            parentIssueId = extractParentIssueId(issue);
            parentIssueId.ifPresent(
                id -> apiDemand.setIssueId(id)
            );

        } while (keepSearching(revolverId, parentIssueId));

        return revolverId.orElseGet(() -> NOT_PROVIDED + originalIssueId);
    }

    protected JSONArray getIssue(String response) {
        JSONArray issue;
        JSONObject json = convertResponse(response);
        issue = extractIssue(json);

        return issue;
    }

    protected LinkedHashMap<String, String> composeQuery(ApiDemand apiDemand) {
        LinkedHashMap<String, String> segments = new LinkedHashMap<>();
    
        segments.put(apiDemand.getRequestUrl(), RedmineAdaptee.SINGLE_ISSUE_OPERATION_ID);
        segments.put(RedmineAdaptee.PARAM_ISSUE_ID, apiDemand.getIssueId().toString());

        return segments;
    }

    protected JSONArray extractIssue(JSONObject json) {
        return jsonFormatter.getJsonArray(RedmineAdaptee.SUBKEY_ISSUES, json);
    }

    protected OptionalInt extractParentIssueId(JSONArray issue) {
        try {
            return OptionalInt.of(
                issue.getJSONObject(0)
                    .getJSONObject(RedmineAdaptee.SUBKEY_PARENT).getInt(RedmineAdaptee.SUBKEY_ID));
        } catch (JSONException e) {
            return OptionalInt.empty();
        }
    }
    
    protected boolean keepSearching(Optional<String> revolverId, OptionalInt parentIssueId) {
        return revolverId.isEmpty() && !parentIssueId.isEmpty();
    }

    public Optional<String> extractRevolerId(JSONArray issueArray) {
        Optional<JSONArray> customFields = getCustomFields(issueArray);

        if(customFields.isEmpty()) {
            return Optional.empty();
        }

        for (int x = 0; x < customFields.get().length(); x++) {
            JSONObject customElement = customFields.get().getJSONObject(x);
            
            if(customElement.getString(RedmineAdaptee.SUBKEY_NAME).equals(RedmineAdaptee.SUBKEY_REVOLVER_ID)) {
                try {
                    String value = customElement.getString(RedmineAdaptee.SUBKEY_VALUE).trim();
                    Optional<String> extractedValue = Optional.of(value);
                    return extractedValue.filter(str -> !str.isBlank());
                } catch (JSONException e) {
                    return Optional.empty();
                }
            }
        }

        // No matching custom field found
        return Optional.empty();
    }
    
    public Optional<JSONArray> getCustomFields(JSONArray issueArray) {
        try {
            return Optional.ofNullable(issueArray.getJSONObject(0).getJSONArray(RedmineAdaptee.SUBKEY_CUSTOM_FIELDS));

        } catch (JSONException e) {
            return Optional.empty();
        }
    }
}
