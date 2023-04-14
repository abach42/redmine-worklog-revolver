package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;

/**
 * concrete product: first day of actual month to today
 */
public class TimeRangeThisMonthProduct implements TimeRangeable {
	protected LocalDate from;
	protected LocalDate to;

	@Override
	public void setFrom() {
		from = getFirstDayOfMonth(getDateNow());
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
		return "this actual month";
	}
}
