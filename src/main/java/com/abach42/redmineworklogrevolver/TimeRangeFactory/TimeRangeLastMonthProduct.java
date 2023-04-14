package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;

/**
 * concrete product: first day of last month to last day of last month, from today
 */
public class TimeRangeLastMonthProduct implements TimeRangeable {
    protected LocalDate from;
    protected LocalDate to;

    @Override
    public void setFrom() {
        from = getLastMonthStart();
    }

    @Override
    public LocalDate getFrom() {
        return from;
    }

    @Override
    public void setTo() {
        LocalDate lastMonthStart = getLastMonthStart();
        to = lastMonthStart.withDayOfMonth(lastMonthStart.lengthOfMonth());
    }

    @Override
    public LocalDate getTo() {
        return to;
    }

    private LocalDate getLastMonthStart() {
        return getFirstDayOfMonth(getDateNow().minusMonths(1));
    }

    @Override
    public String toString() {
        return "last month";
    }
}
