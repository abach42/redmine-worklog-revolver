package com.abach42.redmineworklogrevolver.ApiAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedHashMap;

import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class RedmineAdapteeTest {

    private AutoCloseable closeable;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    ApiRequestable apiRequest;

    @InjectMocks
    RedmineAdaptee subject;


    @Before
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After 
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Produces valid url")
    public void concatenateSegmentsProducesValidUrl() {

        LinkedHashMap<String, String> dummyMap = new LinkedHashMap<>();
        dummyMap.put("http://example.com/", "operation_id");
        dummyMap.put("?key=", "value");
        dummyMap.put("&foo=", "123");

        String actual = subject.concatenateSegments(dummyMap);

        String expected = "http://example.com/" + "operation_id" + "?key=" +  "value" + "&foo=" + "123";
        
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Returns request body String")
    public void getRequesStringReturnsString() {
        when(subject.apiRequest.handleRequest().body()).thenReturn("foo");

        String actual = subject.getResponseBody();

        assertThat(actual).isEqualTo("foo");

    }

    @Test
    @DisplayName("calls withParameter")
    public void listMyTimeEntriesCallsParameterSe() {
        LinkedHashMap<String, String> segments = new LinkedHashMap<>();
        segments.put("foo", "bar");
        
        subject.getResult(segments,"baz");

        verify(apiRequest).withParameter(
            "foobar", 
            RedmineAdaptee.HEADER_AUTH_KEY,
            "baz",
            RedmineAdaptee.HEADER_CONTENT_TYPE_KEY,
            RedmineAdaptee.HEADER_CONTENT_TYPE_VALUE
        );
    }
}
