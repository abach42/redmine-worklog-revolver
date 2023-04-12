package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;

/**
 * concrete product: from today to today
 */
public class TimeRangeTodayProduct implements TimeRangeInterface {
	protected LocalDate from;
	protected LocalDate to;
	
	@Override
	public void setFrom() {
		from = getDateNow();
	}

	@Override
	public LocalDate getFrom() {
		return from;
	}

	@Override
	public void setTo() {
		setFrom();
		to = from;
	}

	@Override
	public LocalDate getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "today";
	}
}
