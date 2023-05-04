package com.abach42.redmineworklogrevolver.Context;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class ApiDemandTest {

    @Spy
    public ApiDemand subject;

    @Test
    @DisplayName("Getter gets")
    public void getFrom() {
        LocalDate expected = LocalDate.of(1970, 01, 01);
        subject.setFrom(expected);
        
        LocalDate actual = subject.getFrom();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Getter gets")
    public void getTo() {
        LocalDate expected = LocalDate.of(1970, 01, 01);
        subject.setTo(expected);

        LocalDate actual = subject.getTo();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Getter gets")
    public void getIssueId() {
        Integer expected = 1;
        subject.setIssueId(expected);
        
        Integer actual = subject.getIssueId();

        assertThat(actual).isEqualTo(expected);
    }
}
