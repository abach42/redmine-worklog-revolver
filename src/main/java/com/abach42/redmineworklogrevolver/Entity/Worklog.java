package com.abach42.redmineworklogrevolver.Entity;

import java.time.LocalDate;

/**
 * Main entity. 
 * 
 * Has an instance of {@code LocalDate} of work day in format of yyyy-mm-dd, compatible to use in API url parameter.
 * Everything will be immutable, except revolverIdentifier.
 * 
 * @param id Number identifier of related Redmine-API-issue, to identify further data for further API requests.
 * @param date {@code LocalDate} date of work log entry.
 * @param hours Logged working hours in decimal format.
 * @param revolverIdentifier String of Revolver-Identifier like {@value 0109-2889-F} or {@value -1213-C (Clubs & PETs & Trends)}
 *  By this value output will be grouped by, summing up work hours. 
 *  Revolver ID default set is, that first all revolver ids are "not provided", worklog task id {@this.id} is used
 *  later in flow, this revolver id will be replaced by a value iterated searched in system, or left as it is.
 * 
 * 
 */
public class Worklog {

	private int id;
	private LocalDate date;
	private double hours;

	public static final String EMPTY_REVOLVER_ID_TEXT = "Id not provided for #";

	private String revolverIdentifier = EMPTY_REVOLVER_ID_TEXT;

	public Worklog(int id, LocalDate date, double hours) {
		this.id = id;
		this.date = date;
		this.hours = hours;
		// default not provided an issue id of time entry (worklog). Automatic int - String conversion used. 
		this.revolverIdentifier += id;
	}

	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public double getHours() {
		return hours;
	}

	public String getRevolverIdentifier() {
		return revolverIdentifier;
	}

	public void setRevolverIdentifier(String revolverIdentifier) {
		this.revolverIdentifier = revolverIdentifier;
	}
}