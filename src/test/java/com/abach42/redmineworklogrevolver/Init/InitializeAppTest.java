package com.abach42.redmineworklogrevolver.Init;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abach42.redmineworklogrevolver.Context.*;
import com.abach42.redmineworklogrevolver.Display.TerminalInputable;
import com.abach42.redmineworklogrevolver.Exception.ConfigFileConnectorException;
import com.abach42.redmineworklogrevolver.Exception.InitializeAppException;
import com.abach42.redmineworklogrevolver.Exception.InitializeValidationException;
import com.abach42.redmineworklogrevolver.Exception.WrongAccessKeyException;

public class InitializeAppTest {

    public static String DUMMY_CONSOLE_INPUT_KEY = "foo";

    //TODO make test mocked again
    public class DummyConsole implements TerminalInputable {

        @Override
        public String getStringFromUser() {
            return DUMMY_CONSOLE_INPUT_KEY;
        }
        
        @Override
        public int getIntFromUser() {
            return 0;
        }
        
    }

    @Nested
    @DisplayName("unmocked")
    class UnMocked {

        public class DummyConfigFileConnector implements ConfigFileConnectable {

            @Override
            public void setup(String folder, String filenName) throws ConfigFileConnectorException {
            
            }

            @Override
            public Map<String, String> readConfiguration() {
            return new HashMap<>();
            }

            @Override
            public void writeConfiguration(Map<String, String> configurationMap) {

            }
            
        }

        public InitializeApp subject = new InitializeApp(new DummyConsole(), new DummyConfigFileConnector());

        private static Stream<Arguments> defaultConfigurationKeysDataProvider() {
            return Stream.of(InitializeApp.DefaultKeys.values())
                .map(Enum::name)
                .map(Arguments::of);
        }

        //TODO repitition
        @ParameterizedTest
        @DisplayName("When values not exist: throws any Exception")
        @MethodSource("defaultConfigurationKeysDataProvider")
        public void testValidateConfigurationThrowsException(InitializeApp.DefaultKeys candidate) {
            Map<String, String> givenConfigurationMap = new HashMap<>();
            givenConfigurationMap.put("config.default_uri", "foo");
            givenConfigurationMap.put("config.default_limit", "foo");
            givenConfigurationMap.put("config.date_format", "foo");
            givenConfigurationMap.put("config.api_access_key", "foo");
            givenConfigurationMap.remove(candidate.getConfigKey());

            assertThrows(Exception.class, () -> subject.validateConfiguration(givenConfigurationMap));
        }
        
        //TODO repitition
        @Test
        @DisplayName("When access key not set: throws certain Exception")
        public void testValidateConfigurationNoAccessKeyThrowsException() {
            Map<String, String> givenConfigurationMap = new HashMap<>();
            givenConfigurationMap.put("config.default_uri", "foo");
            givenConfigurationMap.put("config.default_limit", "foo");
            givenConfigurationMap.put("config.date_format", "foo");
            givenConfigurationMap.put("config.api_access_key", "");

            Exception exception = assertThrows(WrongAccessKeyException.class, 
                () -> subject.validateConfiguration(givenConfigurationMap));

            assertEquals(InitializeApp.WRONG_ACCESS_KEY_MSG, exception.getMessage());
        }

        //TODO repitition
        @Test
        @DisplayName("Should return true, when all is well.")
        public void testValidateConfigurationAllIsWell() {
            Map<String, String> givenConfigurationMap = new HashMap<>();
            givenConfigurationMap.put("config.default_uri", "foo");
            givenConfigurationMap.put("config.default_limit", "foo");
            givenConfigurationMap.put("config.date_format", "foo");
            givenConfigurationMap.put("config.api_access_key", "foo");

            boolean actual = subject.validateConfiguration(givenConfigurationMap);

            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("Should return string of input.")
        public void testAskUserForKey() {
            String expected = DUMMY_CONSOLE_INPUT_KEY;
            String actual = subject.askUserForKey("any");

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should add string to configuration, sanitized")
        public void testPutAccessKeyToConfiguration() {
            String expected = "foo";
            Map<String, String> actualConfigurationMap = new HashMap<>();
            subject.putAccesKeyToConfiguration(actualConfigurationMap, expected + "{}()[]");

            assertThat(actualConfigurationMap.get(InitializeApp.DefaultKeys.API_ACCESS_KEY.getConfigKey())).isEqualTo(expected);
        }
        
        @ParameterizedTest
        @DisplayName("Should be complete")
        @MethodSource("defaultConfigurationKeysDataProvider")
        public void getDefaultValuesBeComplete(InitializeApp.DefaultKeys candidate) {
            Map<String, String> actualConfigurationMap = subject.getDefaultValues();

            assertThat(actualConfigurationMap).containsKey(candidate.getConfigKey());
        }

        
        @ParameterizedTest
        @DisplayName("Should be all set")
        @MethodSource("defaultConfigurationKeysDataProvider")
        public void getDefaultValuesBeAllSet(InitializeApp.DefaultKeys candidate) {
            Map<String, String> actualConfigurationMap = subject.getDefaultValues();

            assertThat(actualConfigurationMap.get(candidate.getConfigKey())).isNotBlank();
        }

        @Test
        @DisplayName("Should be all set")
        public void writeConfigurationToObject() {
            ContextInterface context = new Context();
            AppConfig appConfig = new AppConfig();
            context.setAppConfig(appConfig);
            subject.withContext(context);

            Map<String, String> actualConfigurationMap = subject.getDefaultValues();
            subject.writeConfigurationToObject(actualConfigurationMap);

            assertThat(subject.context.getAppConfig().getBaseUri()).isNotBlank();
            assertThat(subject.context.getAppConfig().getDefaultLimit()).isNotNull();
            assertThat(subject.context.getAppConfig().getApiKey()).isNotBlank();
        }
    }

    @Nested
    @DisplayName("mocked")
    @ExtendWith(MockitoExtension.class)
    class Mocked {
        
        @Mock 
        public ConfigFileConnectable connection;

        @Mock 
        public TerminalInputable console;

        public ContextInterface context;

        @InjectMocks
        @Spy
        public InitializeApp subject = Mockito.mock(
            InitializeApp.class, Mockito.CALLS_REAL_METHODS);

        @Test
        @DisplayName("Connection fails, throws exception")
        public void initializeConnectionThrows() {

            doThrow(new ConfigFileConnectorException("foo")).when(connection).setup(anyString(), anyString());
            assertThrows(InitializeAppException.class, () -> subject.initialize());
        }

        @Test
        @DisplayName("Initialization fails, default Values are renderd")
        public void initializeFails() {

            Map<String, String> configurationMap = new HashMap<>();
            doThrow(new InitializeValidationException("foo")).when(subject).validateConfiguration(configurationMap);
            
            ContextInterface context = new Context();
            subject.withContext(context);

            subject.initialize();
            verify(subject).getDefaultValues();
        }

        @Test
        @DisplayName("No api access key, ask for and put it to config")
        public void initializeNoKey() {
            doThrow(new WrongAccessKeyException("message")).when(subject).validateConfiguration(anyMap());
            doReturn("fookey").when(subject).askUserForKey("message");

            ContextInterface context = new Context();
            subject.withContext(context);

            subject.initialize();
            verify(subject).putAccesKeyToConfiguration(anyMap(), contains("fookey"));
        }
    }
}