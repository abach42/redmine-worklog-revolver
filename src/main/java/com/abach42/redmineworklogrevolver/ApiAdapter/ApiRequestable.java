package com.abach42.redmineworklogrevolver.ApiAdapter;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;

public interface ApiRequestable {

    public void withParameter(String url, String... headers);

    public void handleRequest() throws ApiRequestException;

    public String getBody();
}