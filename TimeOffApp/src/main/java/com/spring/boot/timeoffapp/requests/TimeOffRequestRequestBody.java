package com.spring.boot.timeoffapp.requests;

import java.sql.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.spring.boot.timeoffapp.annotations.CompareDate;
import com.spring.boot.timeoffapp.annotations.DateRange;

@CompareDate(before = "startDate", after = "endDate", message = "startDate must happen before or in the same day as endDate")
@DateRange(startDate = "startDate", endDate = "endDate", hiringDate = "hiringDate", message = "startDate or/and endDate are not valid "
		+ "for one of these reasons: 1) The date entered happens before today's date, 2) The date does not fall in between "
		+ "the range for this valid year. (e.g. hiringDate = 2010-01-01, so valid range of dates is between 2022(current year)-01-01 "
		+ "and 2023(current year)-01-01")
public class TimeOffRequestRequestBody {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long timeOffRequestID;

	@NotNull(message = "timeOffID can't be null")
	@Min(value = 1, message = "timeOffId should be equal or greater than 1")
	private long timeOffID;
	
	@NotNull(message = "startDate can't be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	private Date startDate;
	
	@NotNull(message = "endDate can't be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	private Date endDate;
	
	@NotNull(message = "requestedTime can't be null")
	@Min(value = 1, message = "requestedTime should be equal or greater than 1")
	private int requestedTime;
	
	@NotNull(message = "hiringDate can't be null")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
	private Date hiringDate;
	
	public Date getHiringDate() {
		return hiringDate;
	}

	public void setHiringDate(Date hiringDate) {
		this.hiringDate = hiringDate;
	}

	/*public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getManagerId() {
		return managerId;
	}

	public void setManagerId(long managerId) {
		this.managerId = managerId;
	}*/

	public long getTimeOffId() {
		return timeOffID;
	}

	public void setTimeOffId(long timeOffId) {
		this.timeOffID = timeOffId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getRequestedTime() {
		return requestedTime;
	}

	public void setRequestedTime(int requestedTime) {
		this.requestedTime = requestedTime;
	}

}
