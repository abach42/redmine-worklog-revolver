package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeRangeThisWeekProductTest {
    
    public TimeRangeable subject = mock(TimeRangeThisWeekProduct.class, 
        withSettings()
            .defaultAnswer(CALLS_REAL_METHODS));

    /*
     *  January 1970      
        Mo Tu We Th Fr Sa Su  
                  1  2  3  4  
         5  6  7  8  9 10 11  
        12 13 14 15 16 17 18  
        19 20 21 22 23 24 25  
        26 27 28 29 30 31 
    */
    private static final int ACTUAL_YEAR = 1970;
    private static final int ACTUAL_MONTH = 1;
    //first full week in 1970 was > 5
    private static final int ACTUAL_DAY = 7;

    private LocalDate testCaseNow = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, ACTUAL_DAY);

    @BeforeEach
    public void setUp() {
            doReturn(testCaseNow).when(subject).getDateNow();
    }

    @Test
    @DisplayName("Begin of some actual week")
    public void testGetFrom() {
        //first monday in 1970 was the 5th
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, 5);
        subject.setFrom();
        LocalDate actual = subject.getFrom();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testGetToIsToday() {
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, ACTUAL_DAY);
        subject.setTo();
        LocalDate actual = subject.getTo();

        assertThat(actual).isEqualTo(expected);
    }
}
