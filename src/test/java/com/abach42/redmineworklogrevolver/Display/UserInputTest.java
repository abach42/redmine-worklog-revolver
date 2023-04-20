package com.abach42.redmineworklogrevolver.Display;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.mockito.Mockito;
import org.mockito.Spy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

//TODO clear up
public class UserInputTest {

    @Spy
    public UserInput subject = Mockito.mock(
        UserInput.class, Mockito.CALLS_REAL_METHODS);

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Rule
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();
    
    @BeforeEach
    public void setStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void restoreInitialStreams() {
        System.setOut(originalOut);
    }

    @AfterAll
    public static void tearDown() {
        System.out.close();
    }
    
    @Test
    @Disabled
    @DisplayName("Should print message")
    public void testConsoleLog() {
        String expected = "foo";
        //TODO move subject.consoleLog(expected);
        String actual = out.toString();

        //TODO \n is not cross plattform
        assertEquals(expected + "\n", actual);
    }

    @Test
    @DisplayName("Input string returns string or fallback if blank")
    public void testGetStringFromUser() {
        doReturn("foo")
            .doReturn("").when(subject).contactInputScanner();

        String actual = subject.getStringFromUser();
        assertThat(actual).isEqualTo("foo");
        
        actual = subject.getStringFromUser();
        assertThat(actual).isEqualTo(UserInput.DEFAULT_INPUT_STRING);
    }

    @Test
    @DisplayName("Input string returns string or fallback if blank")
    public void testGetIntFromUser() {
        doReturn("2")
            .doReturn("").when(subject).contactInputScanner();

        Integer actual = subject.getIntFromUser();
        assertThat(actual).isEqualTo(2);

        actual = subject.getIntFromUser();
        assertThat(actual).isEqualTo(UserInput.DEFAULT_INPUT_INT);
    }

    @Nested
    @Disabled
    @DisplayName("Test console input functionally")
    class ConsoleInput {
        //TODO Mock scanner input
        private final InputStream systemIn = System.in;
        private ByteArrayInputStream testIn;

        public String input = "foo";

        @BeforeEach
        public void setUpInput() {
            testIn = new ByteArrayInputStream(input.getBytes());
            System.setIn(testIn);
        }

        @AfterEach
        public void restoreSystemInputOutput() {
            System.setIn(systemIn);
        }

        @Test
        @DisplayName("Return user input as String or fallback value")
        public void testGetStringFromUser() {
            TerminalInputable subject = new UserInput();
            String userInput = subject.getStringFromUser();
            assertEquals(input, userInput);
        }
    }
}
