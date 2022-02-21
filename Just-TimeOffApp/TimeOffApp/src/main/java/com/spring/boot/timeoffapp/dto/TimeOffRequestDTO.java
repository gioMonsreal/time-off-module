package com.spring.boot.timeoffapp.dto;

import java.sql.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.spring.boot.timeoffapp.enums.Status;

public class TimeOffRequestDTO {
	
private long timeOffRequestId;
	
	private long employeeId;
	
	private long managerId;
	
	private Date startDate;
	
	private Date endDate;

	private int requestedTime;
	
	private long timeOffEmployeeId;
	
	@Enumerated(EnumType.STRING)
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getEmployeeId() {
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
	}

	public long getTimeOffRequestId() {
		return timeOffRequestId;
	}

	public void setTimeOffRequestId(long timeOffRequestId) {
		this.timeOffRequestId = timeOffRequestId;
	}

	public long getTimeOffEmployeeId() {
		return timeOffEmployeeId;
	}

	public void setTimeOffEmployeeId(long l) {
		this.timeOffEmployeeId = l;
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
