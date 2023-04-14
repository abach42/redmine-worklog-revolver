package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.abach42.redmineworklogrevolver.Exception.TimeRangeFactoryException;


public class TimeRangeFactoryTest {
    public TimeRangeFactoryInterface subject = new TimeRangeFactory();

    @Test
    @DisplayName("Should throw if input is null")
    public void testGetTimeRangeInputNullException() {
        assertThrows(TimeRangeFactoryException.class, () ->
            subject.getTimeRange(null));
    }

    @Test
    @DisplayName("Should throw if input is invalid")
    public void testGetTimeRangeThrowsWhenInputNonsense() {
        assertThrows(TimeRangeFactoryException.class, () ->
            subject.getTimeRange(666));
    }
    
    @ParameterizedTest
    @DisplayName("Returns according to integer key") 
    @MethodSource("getFactoryMapping")
    public void testGetTimeRangeReturnsType(TimeRangeFactory.TimeRangeTypes types) {
        TimeRangeable actualProduct = subject.getTimeRange(types.inputKey);
        
        assertThat(actualProduct).isInstanceOf(TimeRangeFactory.factoryTypes.get(types.inputKey).getClass());
    }

    private static Stream<Arguments> getFactoryMapping() {
        return Stream.of(TimeRangeFactory.TimeRangeTypes.values())
            .map(Enum::name)
            .map(Arguments::of);
    }
}
