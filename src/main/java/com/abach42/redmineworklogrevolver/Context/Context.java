package com.abach42.redmineworklogrevolver.Context;

/*
 * Context of chain of responsibility, to transport data inbetween app. 
 * Not Singleton, but hand made DI.
 */
public class Context implements ContextInterface {
    protected ApiDemand apiDemand = new ApiDemand();
    protected AppConfig appConfig = new AppConfig();
    protected WorklogList worklogList = new WorklogList();

    @Override
    public ApiDemand getApiDemand() {
        return apiDemand;
    }

    @Override
    public void setApiDemand(ApiDemand apiDemand) {
        this.apiDemand = apiDemand;
    }

    @Override
    public AppConfig getAppConfig() {
        return appConfig;
    }

    @Override
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public WorklogList getWorklogList() {
        return worklogList;
    }

    @Override
    public void setWorklogList(WorklogList worklogList) {
        this.worklogList = worklogList;
    }

    public Context resetApiDemand() {
        apiDemand.setRequestUrl(appConfig.getBaseUri());
        
        apiDemand.setLimit(appConfig.getDefaultLimit());
        apiDemand.setOffset(ApiDemand.DEFAULT_OFFSET);
        apiDemand.setApiKey(appConfig.getApiKey());

        return this; 
    }
}
