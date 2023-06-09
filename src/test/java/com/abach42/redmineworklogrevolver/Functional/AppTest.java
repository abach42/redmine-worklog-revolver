package com.abach42.redmineworklogrevolver.Functional;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import com.abach42.redmineworklogrevolver.App;
import com.abach42.redmineworklogrevolver.ProcedureChain.ChooseTimeRangeHandler;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class AppTest {
    
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
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
        //TODO \n is not cross plattform
        String expectedOutput = ChooseTimeRangeHandler.VOCATIVE_MSG + "\n"; 
        App.main();

       //TODO use user Entry systemInMock.provideLines("1");

        assertEquals(expectedOutput, out.toString());
    }
}
