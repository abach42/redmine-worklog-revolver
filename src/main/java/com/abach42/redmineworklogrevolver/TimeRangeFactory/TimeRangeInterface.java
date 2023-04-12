package com.abach42.redmineworklogrevolver.TimeRangeFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * products of factory-method 
 */
public interface TimeRangeInterface {
	
  public void setFrom();
  public LocalDate getFrom();

  public void setTo();
  public LocalDate getTo();


  default LocalDate getDateNow() {
    return LocalDate.now();
  }

  default LocalDate getFirstDayOfWeek(LocalDate date) {
    return date.with(DayOfWeek.MONDAY);
  }

  default LocalDate getFirstDayOfMonth(LocalDate date) {
    return date.withDayOfMonth(1);
  }
  
}