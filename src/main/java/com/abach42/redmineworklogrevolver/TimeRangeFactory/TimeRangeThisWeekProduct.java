package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;

/**
 * concrete product: first day of actual week to today
 */
public class TimeRangeThisWeekProduct implements TimeRangeable {
	protected LocalDate from;
	protected LocalDate to;

	@Override
	public void setFrom() {
		from = getFirstDayOfWeek(getDateNow());
	}

	@Override
	public LocalDate getFrom() {
		return from;
	}

	@Override
	public void setTo() {
		to = getDateNow();
	}

	@Override
	public LocalDate getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "this actual week";
	}
}