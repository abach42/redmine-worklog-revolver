package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;

/*
 * TODO write
 */
public class RevolverIdRedmineAdapter extends AbstractRedmineAdapter implements RevolverIdTargetInterface {

    public RevolverIdRedmineAdapter(RedmineAdaptee adaptee, JsonExtractable jsonFormatter) {
        super(adaptee, jsonFormatter);
    }

    @Override
    public String singleRevolverId(ApiDemand apiDemand) {
        try {
            return slideRevolverId(apiDemand);
        } catch  (ApiRequestException e) {
            throw new ApplicationException(e.getMessage());

        }
    }

    protected String slideRevolverId(ApiDemand apiDemand) {
        JSONArray issue;
		Integer parentIssueId; 
        String revolverId = "";
        Integer originalIssueId = apiDemand.getIssueId();

        do {
			LinkedHashMap<String, String> query = composeQuery(apiDemand);
			String response = sendRequest(query, apiDemand.getApiKey());

			issue = getIssue(response);
			revolverId = extractRevolerId(issue);

			parentIssueId = getParentIssueId(issue);
			apiDemand.setIssueId(parentIssueId);
			
		} while (keepSliding(revolverId, parentIssueId));
		
        return revolverId.isBlank() ? reportOriginalIssue(originalIssueId) : revolverId;
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

    protected Integer getParentIssueId(JSONArray issue) {
		try {
			return issue.getJSONObject(0)
                .getJSONObject(RedmineAdaptee.SUBKEY_PARENT).getInt(RedmineAdaptee.SUBKEY_ID);
		} catch (JSONException e) {
			return null;
		}
	}

	protected boolean keepSliding(String revolverId, Integer parentIssueId) {
		return revolverId.equals("") && parentIssueId != null;
	}
	
    public String extractRevolerId(JSONArray issueArray) {
		JSONArray customFields = getCustomFields(issueArray);
		
		for (int x = 0; x < customFields.length(); x++) {
			JSONObject customElement = customFields.getJSONObject(x);
			
            
			if(customElement.getString(RedmineAdaptee.SUBKEY_NAME)
                    .equals(RedmineAdaptee.SUBKEY_REVOLVER_ID)) {
				return customElement.getString(RedmineAdaptee.SUBKEY_VALUE).trim();
				
			}
		}
		return "";
	}
    
	public JSONArray getCustomFields(JSONArray issueArray) {
		try {
			return issueArray.getJSONObject(0).getJSONArray(RedmineAdaptee.SUBKEY_CUSTOM_FIELDS);

		} catch (JSONException e) {
			return null;

		}
	}

    private String reportOriginalIssue(Integer originalIssueId) {
        return "not provided for #" + originalIssueId;
    }
}
