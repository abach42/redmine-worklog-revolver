package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class TimeRangeThisMonthProductTest {
    public TimeRangeable subject = mock(TimeRangeThisMonthProduct.class, 
        withSettings()
            .defaultAnswer(CALLS_REAL_METHODS));

    private static final int ACTUAL_YEAR = 1970;
    private static final int ACTUAL_MONTH = 1;
    private static final int ACTUAL_DAY = 13;

    private LocalDate testCaseNow = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, ACTUAL_DAY);

    @BeforeEach
    public void setUp() {
        doReturn(testCaseNow).when(subject).getDateNow();
    }

    @Test
    @DisplayName("Begin of some Month")
    public void testSetFrom() {
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, 1);
        subject.setFrom();
        LocalDate actual = subject.getFrom();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("End is some todays date")
    public void testSetTo() {
        LocalDate expected = LocalDate.of(ACTUAL_YEAR, ACTUAL_MONTH, ACTUAL_DAY);
        subject.setTo();
        LocalDate actual = subject.getTo();

        assertThat(actual).isEqualTo(expected);
    }
}
