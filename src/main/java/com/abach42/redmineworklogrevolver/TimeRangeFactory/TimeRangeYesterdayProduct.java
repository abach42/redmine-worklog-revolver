package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.LocalDate;
/**
 * concrete product: from yesterday to yesterday
 */
public class TimeRangeYesterdayProduct implements TimeRangeInterface {
	protected LocalDate from;
	protected LocalDate to;

	@Override
	public void setFrom() {
		from = getYesterday(getDateNow());
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
	
	private LocalDate getYesterday(LocalDate now) {
		return now.minusDays(1);
	}

	@Override
	public String toString() {
		return "yesterday";
	}
}
