package com.abach42.redmineworklogrevolver.Context;

public interface ContextInterface {
    public ApiDemand getApiDemand();

    public void setApiDemand(ApiDemand apiDemand);


    public AppConfig getAppConfig();

    public void setAppConfig(AppConfig appConfig);


    public Context resetApiDemand();

    public WorklogList getWorklogList();

    
    public void setWorklogList(WorklogList worklogList);
}
