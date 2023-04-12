package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * concrete product: first day of last week to last day of last week, from today
 */
public class TimeRangeLastWeekProduct implements TimeRangeInterface {
	protected LocalDate from;
	protected LocalDate to;

	@Override
	public void setFrom() {
		from = getFirstDayOfWeek(getDateNow().minusWeeks(1));
	}

	@Override
	public LocalDate getFrom() {
		return from;
	}

	@Override
	public void setTo() {
		to = getLastDayOfWeek(getDateNow().minusWeeks(1));
	}
	
	@Override
	public LocalDate getTo() {
		return to;
	}

	private LocalDate getLastDayOfWeek(LocalDate date) {
		return date.with(DayOfWeek.SUNDAY);
	}

	@Override
	public String toString() {
		return "last week";
	}
}	
	
