package com.abach42.redmineworklogrevolver.ApiAdapter;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.EmptyResultException;
import com.abach42.redmineworklogrevolver.Exception.WrongAccessKeyException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class ApiRequestTest {

    @Mock
    protected HttpResponse<String> responseValue;

    protected ApiRequest realSubject;

    @Spy
    protected ApiRequest subject;

    @Mock
    protected HttpRequest request;

    @BeforeEach
    public void setUp() {
       realSubject = new ApiRequest();
    }

    @Test
    @DisplayName("Should set members url and headers in class.")
    void testWithParameter() {
        String expectedUrl = "foo"; 
        String expectedHeaders[] = new String[]{"bar", "baz"};

        realSubject.withParameter(expectedUrl, expectedHeaders);

        assertThat(realSubject.url).isEqualTo(expectedUrl);
        assertThat(realSubject.headers).hasSize(2)
            .containsExactly(expectedHeaders);
    }

    @Test
    @DisplayName("Should throw various exceptions")
    void testEvaluateStatusCode() {
        final int statusCodeForbidden = 401;
        Exception exception = assertThrows(WrongAccessKeyException.class, () ->
            realSubject.evaluateStatusCode(statusCodeForbidden));
        
        assertEquals(ApiRequest.WRONG_ACCESS_KEY_MSG + statusCodeForbidden, exception.getMessage());

        final int statusCodeAny = 999;
        exception = assertThrows(ApiRequestException.class, () ->
            realSubject.evaluateStatusCode(statusCodeAny));
        
        assertEquals(ApiRequest.STATUS_CODE_ERROR_MSG + statusCodeAny, exception.getMessage());

        assertDoesNotThrow(() -> realSubject.evaluateStatusCode(200));
    }

    @Test
    @DisplayName("Should throw if response empty")
    void testReadStatusCodeThrowIfEmpty() {
        Optional<HttpResponse<String>> actualResponse = Optional.empty();

        Exception exception = assertThrows(EmptyResultException.class, () -> 
            realSubject.readStatusCode(actualResponse));

        assertEquals(ApiRequest.RESULT_EMPTY_MSG, exception.getMessage());
    }

    @Test
    @DisplayName("Return if not empty response")
    void testReadStatusCodeReturnsStatusCode() {
        int expectedCode = 999;

        Optional<HttpResponse<String>> actualResponse = Optional.of(responseValue);
        doReturn(expectedCode).when(responseValue).statusCode();
        int actualCode = realSubject.readStatusCode(actualResponse);

        assertThat(actualCode).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("Should throw if response empty")
    void testSetBodyThrowsIfEmpty() {
        
        Optional<HttpResponse<String>> response = Optional.empty();

        Exception exception = assertThrows(EmptyResultException.class, () -> 
            realSubject.setBody(response));

        assertEquals(ApiRequest.RESULT_EMPTY_MSG, exception.getMessage());
    }

    @Test
    @DisplayName("Sets body in case response not empty")
    void testSetBodySetsBody() {
        String expectedBody = "foo";

        Optional<HttpResponse<String>> actualResponse = Optional.of(responseValue);
        doReturn(expectedBody).when(responseValue).body();
        realSubject.setBody(actualResponse);
        String actualBody = realSubject.body;

        assertThat(actualBody).isEqualTo(expectedBody);
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    @DisplayName("Should throw if body is blank")
    void testGetBodyThrows(String body) {
        
        realSubject.body = body;

        Exception exception = assertThrows(EmptyResultException.class, () -> 
            realSubject.getBody());
        
        assertThat(exception.getMessage()).isEqualTo(ApiRequest.RESULT_EMPTY_MSG);
    }

    @Test
    @DisplayName("Returns body when not blank")
    void testGetBodyReturnsBody() {
        String expectedBody = "foo";

        realSubject.body = expectedBody;
        String actualBody = realSubject.getBody();

        assertThat(actualBody).isEqualTo(expectedBody);
    }

    @Test
    @DisplayName("sets body")
    void testHandleRequestSetsBody() throws IOException, InterruptedException {
        
        Optional<HttpResponse<String>> optionalResponse = Optional.ofNullable(responseValue);
        
        doReturn(request).when(subject).getRequest();
        
        doReturn(optionalResponse).when(subject).getResponse(request);
        
        doNothing().when(subject).setBody(optionalResponse);

        doNothing().when(subject).evaluateStatusCode(anyInt());

        subject.handleRequest();

        verify(subject).setBody(optionalResponse);
    }

    @Test
    @DisplayName("throws api exception on getResponse failure")
    void testHandleRequestThrowsOnResponseFailure() throws IOException, InterruptedException {
        doReturn(request).when(subject).getRequest();

        doThrow(new IOException("foo", new Exception("foo")))
            .doThrow(new InterruptedException("foo"))
               // .doThrow(new ConnectException("foo"))
        .when(subject).getResponse(any(HttpRequest.class));

        Exception exception = assertThrows(ApiRequestException.class, () -> 
            subject.handleRequest());
        
        assertThat(exception.getMessage()).startsWith(ApiRequest.API_ERROR_MSG);
    }

    @Test
    @DisplayName("throws api exception on ConnectException")
    void testHandleRequestThrowsOnConnectException() throws IOException, InterruptedException {
        doReturn(request).when(subject).getRequest();

        doThrow(new ConnectException("foo")).when(subject).getResponse(any(HttpRequest.class));

        Exception exception = assertThrows(ApiRequestException.class, () -> 
            subject.handleRequest());
        
        assertThat(exception.getMessage()).startsWith(ApiRequest.CONNECTION_ERROR_MSG);
    }

    @Test
    @DisplayName("should return some content")
    public void testGetRequest() {

        String url = "https://foo.bar";
        realSubject.withParameter(url, "Content-Type", "application/json");

        HttpRequest request = realSubject.getRequest();

        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("response should be present")
    void testGetResponse() throws IOException, InterruptedException  {
        // Set up mock server
        MockWebServer mockServer = new MockWebServer();
 
        mockServer.start();
        String url = mockServer.url("/test").toString();
        String[] headers = {"Accept", "application/json", "Authorization", "Bearer abc123"};
        
        realSubject.withParameter(url, headers);
        
        // Set up a mock response from the server
        String responseBody = "{ \"message\": \"Hello, World!\" }";
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(responseBody));

        HttpRequest request = realSubject.getRequest();

        // Call the method being tested
        Optional<HttpResponse<String>> response = realSubject.getResponse(request);
        
        assertThat(response).isPresent();

        // Verify that the request object was built correctly
        assertEquals(mockServer.url("/test").toString(), request.uri().toString());
        assertEquals("application/json", request.headers().firstValue("Accept").orElse(null));
        mockServer.shutdown();
        mockServer.close();
    }

}
