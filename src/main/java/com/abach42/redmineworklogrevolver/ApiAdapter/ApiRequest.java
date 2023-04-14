package com.abach42.redmineworklogrevolver.ApiAdapter;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
public class ApiRequest implements ApiRequestable {

	protected String url;
	protected String[] headers; 

	protected HttpResponse<String> response;  

	public void withParameter(String url, String... headers) {
		this.url = url;
		this.headers = headers; 
	}

	//fluent interface train wreck, but handy shortcut
	@Override
	public HttpResponse<String> handleRequest() throws ApiRequestException {
		HttpResponse<String> response;

		try {
			HttpRequest request = request();
			response = response(request);
			
			evaluateStatusCode(response);

		} catch (ConnectException e) {
			throw new ApiRequestException("Connection error during call redmine API: " + e.getCause().getCause());

		} catch (IOException | InterruptedException e) {
			throw new ApiRequestException("Error during call redmine API: " + e.getCause());

		} 
			
		return response;
	}

	@Override
	public String toString() throws EmptyResultException{
		String responseString = response.body().toString();

		if(responseString.isBlank()) {
			throw new EmptyResultException("Result empty.");
		}

		return responseString;
	}

	protected HttpRequest request() {
		return HttpRequest.newBuilder()
			.uri(URI.create(url))
			.headers(headers)
			.GET()
			.build();
	}
	
	protected HttpResponse<String> response(HttpRequest request) throws IOException, InterruptedException {
		return HttpClient.newBuilder()
			.build()
			.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
	}

	//TODO test this
	protected void evaluateStatusCode(HttpResponse<String> response) throws WrongAccessKeyException, ApiRequestException {
		int statusCode = readStatusCode(response);
		
		if(statusCode == 401) {
			throw new WrongAccessKeyException("Access key does not match, http status code " + statusCode);
		}

		if(statusCode != 200) {
			throw new ApiRequestException("Can not read data from API, http status code " + statusCode);
		}
	}

	private Integer readStatusCode(HttpResponse<String> response) {
		return response.statusCode();
	}
}
