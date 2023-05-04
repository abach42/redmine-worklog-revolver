package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.spy;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class TimeRangeInterfaceTest {

    public TimeRangeable subject = spy(TimeRangeable.class);

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
    private static final int ACTUAL_DAY = 13;

    private LocalDate testCaseNow = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, ACTUAL_DAY);

    @Test
    @DisplayName("Returns todays date")
    public void testGetDateNow() {
        //this is not good: if you test it on midnight, it would fail
        LocalDate expected = LocalDate.now();
    
        LocalDate actual = subject.getDateNow();
    
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Returns first day of a week")
    public void testGetFirstDayOfWeek() {
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, 12);
        LocalDate actual = subject.getFirstDayOfWeek(testCaseNow);
    
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Returns first day of a Month")
    public void testGetFirstDayOfMonth() {
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, 1);
        subject.setFrom();
        LocalDate actual = subject.getFirstDayOfMonth(testCaseNow);

        assertThat(actual).isEqualTo(expected);
    }
}
