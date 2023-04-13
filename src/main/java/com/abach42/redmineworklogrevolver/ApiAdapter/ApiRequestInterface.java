package com.abach42.redmineworklogrevolver.ApiAdapter;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import java.net.http.HttpResponse;

public interface ApiRequestInterface {
    
    public void withParameter(String url, String... headers);

    public HttpResponse<String> handleRequest() throws ApiRequestException;

    String toString();
}