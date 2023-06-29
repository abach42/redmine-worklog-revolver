package com.abach42.redmineworklogrevolver.ApiAdapter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abach42.redmineworklogrevolver.Context.ApiDemand;
import com.abach42.redmineworklogrevolver.Context.WorklogList;
import com.abach42.redmineworklogrevolver.Entity.Worklog;
import com.abach42.redmineworklogrevolver.Exception.ApiRequestException;
import com.abach42.redmineworklogrevolver.Exception.ApplicationException;

@ExtendWith(MockitoExtension.class)
public class WorklogRedmineAdapterTest {

    private AutoCloseable closeable;

    @Mock
    protected ApiRequestable apiRequest; 

    @InjectMocks
    @Spy
    protected RedmineAdaptee adaptee;

    @Mock
    protected RedmineAdaptee adapteeInject;
    
    @Mock
    protected JsonExtractable jsonFormatter;

    @InjectMocks
    @Spy
    protected WorklogRedmineAdapter subject;

    @Mock
    protected ApiDemand apiDemand;

    @BeforeEach
    public void openMocks() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        closeable.close();
    }


    @Test
    @DisplayName("Should return WorklogList")
    void testBuildWorklogListReturnsWorklogList() {
        doReturn(1).when(jsonFormatter).getInt(any(), any());
        doReturn("1970-01-01").when(jsonFormatter).getString(anyString(), any(JSONObject.class));
        doReturn(1.00).when(jsonFormatter).getDouble(anyString(), any(JSONObject.class));

        WorklogList actual = subject.buildWorklogList(new JSONArray("[{'foo': 'bar'}]"));
        WorklogList expected = new WorklogList();
        expected.add(new Worklog(1, LocalDate.parse("1970-01-01"), 1.00));

        assertThat(actual.get(0).getId()).isEqualTo(expected.get(0).getId());
        assertThat(actual.get(0).getDate()).isEqualTo(expected.get(0).getDate());
        assertThat(actual.get(0).getHours()).isEqualTo(expected.get(0).getHours());
    }

    @Test
    @DisplayName("Should build some segments")
    void testComposeQueryReturnsSomeSegements() {
        doReturn("foo").when(subject).dateFormatForRequest(any());
        LinkedHashMap<String, String> actual = subject.composeQuery(apiDemand);

        assertThat(actual.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("Should call jsonFormatter method")
    void testConvertLimit() {
        subject.convertLimit(new JSONObject());
        verify(jsonFormatter).getInt(any(), any());
    }

    @Test
    @DisplayName("Should call jsonFormatter method")
    void testConvertResponse() {
        subject.convertResponse(any());
        verify(jsonFormatter).convertToJson(any());
    }
    
    @Test
    @DisplayName("Should call jsonFormatter method")
    void testExtractTimeEntries() {
        subject.extractTimeEntries(any(JSONObject.class));
        verify(jsonFormatter).getJsonArray(any(), any());
    }

    @Test
    @DisplayName("Should Return WorklogList")
    void testGetWorklogFromApiReturnsWorklogList() {
        JSONArray jsonArray = new JSONArray("[{'foo': 'bar'}]");
        doReturn(jsonArray).when(subject).processResultPages(any(ApiDemand.class)); 
        WorklogList expected = new WorklogList();
        expected.add(new Worklog(1, LocalDate.parse("1970-01-01"), 1.00));
        doReturn(expected).when(subject).buildWorklogList(jsonArray); 

        WorklogList actual = subject.listWorklog(apiDemand);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Prozess request page iterates over pages")
    void testProcessResultPagesIterates() {
        doReturn(0)
            .doReturn(1)
                .when(apiDemand).getOffset();
        
        LinkedHashMap<String, String> query = new LinkedHashMap<>();
        query.put("foo", "bar");
        doReturn(query).when(subject).composeQuery(any(ApiDemand.class));
        doReturn("bar").when(subject).sendRequest(any(), any());
        doReturn(new JSONObject("{'foo': 'bar'}")).when(subject).convertResponse("bar");
        doReturn(new JSONArray("[{'foo': 'bar'}]")).when(subject).extractTimeEntries(any());
        doReturn(1).when(subject).paginateDemand(any(ApiDemand.class), any(JSONObject.class));

        JSONArray actual = subject.processResultPages(apiDemand);

        assertThat(actual).isNotEmpty();
    }

    @Test
    @DisplayName("Should catch and throw exception")
    void testProcessResultPagesCathAndThrow() {
        doReturn(0)
                .when(apiDemand).getOffset();
        
        LinkedHashMap<String, String> query = new LinkedHashMap<>();
        query.put("foo", "bar");
        doReturn(query).when(subject).composeQuery(any(ApiDemand.class));
        doThrow(ApiRequestException.class).when(subject).sendRequest(any(), any());

        assertThrows(ApplicationException.class, () ->
            subject.processResultPages(apiDemand));
    }

    @Test
    @DisplayName("Should call send Request to return value")
    void testSentRequest() {
        doReturn("foo").when(subject.adaptee).getResult(any(), any());
        String actual = subject.sendRequest(any(), any());
        
        assertThat(actual).isEqualTo("foo");
    }
}
