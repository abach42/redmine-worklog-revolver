package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import com.abach42.redmineworklogrevolver.Display.Logable;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.EmptyResultException;
import com.abach42.redmineworklogrevolver.Exception.WrongAccessKeyException;

/*
 * API to request some Resful or RPC API using {@code HttpResponse} API, giving url and some header information. 
 * Usage: <pre>{@code String response = new ApiRequest("https://myendpoint.de/hello/1, "SOME-HEADER-KEY", "someValue", "KEY", "value")
 * 				.handleRequest().toString() }</pre>
 * You better try-catch this to be able to handle exceptions.
 * Working in train wreck to make call short hand. 
 */
public class ApiRequest implements ApiRequestable, Logable {

    protected String url;
    protected String[] headers;

    protected static final String CONNECTION_ERROR_MSG = "Connection error during call redmine API: ";
    protected static final String API_ERROR_MSG = "Error during call redmine API: ";
    protected static final String STATUS_CODE_ERROR_MSG = "Can not read data from API, http status code ";
    protected static final String RESULT_EMPTY_MSG = "Request result empty.";
    protected static final String WRONG_ACCESS_KEY_MSG = "Access key does not match, http status code ";

    protected Optional<HttpResponse<String>> response;
    protected String body;

    public void withParameter(String url, String... headers) {
        this.url = url;
        this.headers = headers;
    }

    @Override
    public void handleRequest() throws ApiRequestException {
        try {
            HttpRequest request = getRequest();
            response = getResponse(request);

            setBody(response);
            evaluateStatusCode(readStatusCode(response));
        } catch (ConnectException e) {
            String errorMessage = CONNECTION_ERROR_MSG + e.getMessage();
            logServere(this, errorMessage);
            throw new ApiRequestException(errorMessage);
        } catch (IOException | InterruptedException e) {
            throw new ApiRequestException(API_ERROR_MSG + e.getCause().getMessage());
        }
    }

    @Override
    public String getBody() throws EmptyResultException {
        if(body.isBlank()) {
            throw new EmptyResultException(RESULT_EMPTY_MSG);
        }
        
        return body;
    }

    protected void setBody(Optional<HttpResponse<String>> response) {
        body = response.map(mpResponse -> mpResponse.body())
            .orElseThrow(
                () -> new EmptyResultException(RESULT_EMPTY_MSG)
            );
    }

    protected HttpRequest getRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers(headers)
                .GET()
                .build();
    }

    protected Optional<HttpResponse<String>> getResponse(HttpRequest request) throws IOException, InterruptedException {
        return Optional.ofNullable(HttpClient.newBuilder()
                .build()
                .send(request, java.net.http.HttpResponse.BodyHandlers.ofString()));
    }

    protected void evaluateStatusCode(int statusCode) throws WrongAccessKeyException, ApiRequestException {
        if (statusCode == 401) {
            throw new WrongAccessKeyException(WRONG_ACCESS_KEY_MSG + statusCode);
        }

        if (statusCode != 200) {
            throw new ApiRequestException(STATUS_CODE_ERROR_MSG + statusCode);
        }
    }

    protected Integer readStatusCode(Optional<HttpResponse<String>> response) {
        return response.map(mpResponse -> mpResponse.statusCode())
            .orElseThrow(
                () -> new EmptyResultException(RESULT_EMPTY_MSG)
            );
    }
}
