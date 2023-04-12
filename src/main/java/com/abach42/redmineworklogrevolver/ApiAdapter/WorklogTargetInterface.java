package com.abach42.redmineworklogrevolver.ApiAdapter;

import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Context.WorklogList;

public interface WorklogTargetInterface {

    public WorklogList listWorklog(ApiDemand apiDemand);

}