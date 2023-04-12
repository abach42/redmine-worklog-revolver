package com.abach42.redmineworklogrevolver.Context;

/*
 * App configuration according to configuration file .configuration/config.properties
 */
public class AppConfig {
    private boolean isInitialized = false;

    private String baseUri;

    private Integer defaultLimit;

    private String datePattern; 

    private String apiKey;
    
    public AppConfig() {
        isInitialized = false;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized() {
        isInitialized = false;
        if(
            isSet(getBaseUri())
            &&
            getDefaultLimit() != null
            &&
            isSet(getDatePattern())
            &&
            isSet(getApiKey())
        ) {
            isInitialized = true;
        }
    }

    private boolean isSet(String value) {
        if (value == null) {
            return false;
        }
        if (value.isBlank()) {
            return false;
        }
        return true;
    }

    public void setDefaultLimitFromString(String defaultLimitConfigString) {
        try {
            setDefaultLimit(Integer.parseInt(defaultLimitConfigString));
        
        } catch (NumberFormatException | NullPointerException e) {
            
            setDefaultLimit(null);
        }
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public Integer getDefaultLimit() {
        return defaultLimit;
    }

    public void setDefaultLimit(Integer defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String datePattern) {
        this.datePattern = datePattern;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
