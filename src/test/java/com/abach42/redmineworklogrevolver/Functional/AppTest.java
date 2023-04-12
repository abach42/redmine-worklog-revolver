package com.abach42.redmineworklogrevolver.Functional;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.*;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;


import com.abach42.redmineworklogrevolver.App;
import com.abach42.redmineworklogrevolver.ProcedureChain.ChooseTimeRangeHandler;

public class AppTest {
    
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

    @Disabled
    @Test
    public void out() {
        //TODO add fixtures
        String expectedOutput = ChooseTimeRangeHandler.VOCATIVE_MSG + "\n"; 
        App.main();

       //TODO use user Entry systemInMock.provideLines("1");

        assertEquals(expectedOutput, out.toString());
    }
}
