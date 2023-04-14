package com.abach42.redmineworklogrevolver.ApiAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

public interface JsonExtractable {

    public JSONObject convertToJson(String jsonString);

    public JSONArray getJsonArray(String key, JSONObject jsonObject);

    public JSONObject getJsonObject(String key, JSONObject jsonObject);

    public String getString(String key, JSONObject jsonObject);

    public int getInt(String key, JSONObject jsonObject);
    
    public double getDouble(String key, JSONObject jsonObject);
}