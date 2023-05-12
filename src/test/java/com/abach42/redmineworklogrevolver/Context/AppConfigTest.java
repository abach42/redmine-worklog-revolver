package com.abach42.redmineworklogrevolver.Context;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class AppConfigTest {

    public AppConfig subject = new AppConfig();

    private static Stream<TestCase> oneMemberFails() {
        return Stream.of(
            new TestCase(null, "1", "succeeds", "succeeds"),
            new TestCase("succeeds", null, "succeeds", "succeeds"),
            new TestCase("succeeds", "fails", "succeeds", "succeeds"),
            new TestCase("succeeds", "1", null, "succeeds"),
            new TestCase("succeeds", "1", "succeeds", null),
            
            new TestCase("", "1", "succeeds", "succeeds"),
            new TestCase("succeeds", "", "succeeds", "succeeds"),
            new TestCase("succeeds", "1", "", "succeeds"),
            new TestCase("succeeds", "1", "succeeds", "")
        );
    }

    private static class TestCase {
        private final String baseUri;
        private final String defaultLimit;
        private final String datePattern;
        private final String apiKey;

        public TestCase(String baseUri, String defaultLimit, String datePattern, String apiKey) {
            this.baseUri = baseUri;
            this.defaultLimit = defaultLimit;
            this.datePattern = datePattern;
            this.apiKey = apiKey;
        }
    }

    @ParameterizedTest
    @MethodSource("oneMemberFails")
    public void testSetInitializedIsFalse(TestCase testCase) {
        subject.setBaseUri(testCase.baseUri);
        subject.setDefaultLimitFromString(testCase.defaultLimit);
        subject.setDatePattern(testCase.datePattern);
        subject.setApiKey(testCase.apiKey);

        subject.setInitialized();

        assertThat(subject.isInitialized()).isFalse();
    }

    @Test
    @DisplayName("is true, when all is set up well")
    public void testSetInitialized() {
        String foo = "foo";

        subject.setBaseUri(foo);
        subject.setDefaultLimitFromString("666");
        subject.setApiKey(foo);
        subject.setDatePattern(foo);
        
        subject.setInitialized();

        boolean actual = subject.isInitialized();

        assertThat(actual).isTrue();
    }

}
