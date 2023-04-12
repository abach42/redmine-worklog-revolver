package com.abach42.redmineworklogrevolver.TimeRangeFactory;

/* This factory method produces a time range to be sent to Redmine API request. */
public interface TimeRangeFactoryInterface {
    TimeRangeInterface getTimeRange(Integer numberInput);
}