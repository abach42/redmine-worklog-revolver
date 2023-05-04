package com.abach42.redmineworklogrevolver.Context;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class ContextTest {

    @Spy
    public Context subject;

    @Test
    @DisplayName("getter gets")
    public void getApiDemand() {
       ApiDemand actual = subject.getApiDemand();

       ApiDemand expected = subject.apiDemand;

       assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("setter sets")
    public void setApiDemand() {
        ApiDemand expected = new ApiDemand();
        subject.setApiDemand(expected);

        ApiDemand actual = subject.apiDemand;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("getter gets")
    public void getAppConfig() {
        AppConfig actual = subject.getAppConfig();

        AppConfig expected = subject.appConfig;
 
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("setter sets")
    public void setAppConfig( ) {
        AppConfig expected = new AppConfig();
        subject.setAppConfig(expected);

        AppConfig actual = subject.appConfig;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("getter gets")
    public void getWorklogList() {
        WorklogList actual = subject.getWorklogList();

        WorklogList expected = subject.worklogList;
 
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("setter sets")
    public void setWorklogList() {
        WorklogList worklogList = new WorklogList();
        subject.setWorklogList(worklogList);

        WorklogList actual = subject.worklogList;

        assertThat(actual).isEqualTo(worklogList);
    }

    @Test
    @DisplayName("restets to default")
    public void resetApiDemand() {
       AppConfig appConfig = new AppConfig();
       appConfig.setBaseUri("foo");
       appConfig.setDefaultLimit(0);
       appConfig.setApiKey("bar");

       subject.setAppConfig(appConfig);

       subject.resetApiDemand();

       assertThat(subject.apiDemand.getRequestUrl()).isEqualTo("foo");
       assertThat(subject.apiDemand.getLimit()).isEqualTo(0);
       assertThat(subject.apiDemand.getOffset()).isEqualTo(ApiDemand.DEFAULT_OFFSET);
       assertThat(subject.apiDemand.getApiKey()).isEqualTo("bar");
    }

}
