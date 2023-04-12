package com.abach42.redmineworklogrevolver.ApiAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonFormatter implements JsonInterface {

	@Override
    public JSONObject convertToJson(String jsonString) {
		return new JSONObject(jsonString);
	}
	
	@Override
    public JSONArray getJsonArray(String key, JSONObject jsonObject) {
		return jsonObject.getJSONArray(key);
	}

    public JSONObject getJsonObject(String key, JSONObject jsonObject) {
        return jsonObject.getJSONObject(key);
    }

    @Override
    public String getString(String key, JSONObject jsonObject) {
        return jsonObject.getString(key);
    }

	@Override
    public int getInt(String key, JSONObject jsonObject) {
		return jsonObject.getInt(key);
	}

    @Override
    public double getDouble(String key, JSONObject jsonObject) {
        return jsonObject.getDouble(key);
    }
}
