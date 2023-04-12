package com.abach42.redmineworklogrevolver.Context;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AppConfigTest {

    public AppConfig subject = new AppConfig();

    private static Stream<TestCase> oneMemberFails() {
        return Stream.of(
            new TestCase(null, "1", "succeeds"),
            new TestCase("succeeds", null, "succeeds"),
            new TestCase("succeeds", "fails", "succeeds"),
            new TestCase("succeeds", "1", null),
            
            new TestCase("", "1", "succeeds"),
            new TestCase("succeeds", "", "succeeds"),
            new TestCase("succeeds", "1", "")
        );
    }

    private static class TestCase {
        private final String baseUri;
        private final String defaultLimit;
        private final String apiKey;

        public TestCase(String baseUri, String defaultLimit, String apiKey) {
            this.baseUri = baseUri;
            this.defaultLimit = defaultLimit;
            this.apiKey = apiKey;
        }
    }

    @ParameterizedTest
    @MethodSource("oneMemberFails")
    public void testSetInitializedIsFalse(TestCase testCase) {
        subject.setBaseUri(testCase.baseUri);
        subject.setDefaultLimitFromString(testCase.defaultLimit);
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


        subject.setInitialized();

        boolean actual = subject.isInitialized();

        assertThat(actual).isTrue();
    }

}
