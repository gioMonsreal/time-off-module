package com.spring.boot.timeoffapp.model;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.spring.boot.timeoffapp.enums.Status;

@Entity
@Table(name="time_off_requests")
public class TimeOffRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long timeOffRequestId;
	
	@Column(updatable = false)
	private long employeeId;
	
	@Column(updatable = false)
	private long managerId;
	
	@Column(updatable = false)
	private Date startDate;
	
	@Column(updatable = false)
	private Date endDate;
	
	@Column(updatable = false)
	private int requestedTime;
	
	@Column(updatable = false)
	private long timeOffEmployeeId;
	
	@Enumerated(EnumType.STRING)
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public long getTimeOffRequestId() {
		return timeOffRequestId;
	}
	public void setTimeOffRequestId(long timeOffRequestId) {
		this.timeOffRequestId = timeOffRequestId;
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
	public long getTimeOffEmployeeId() {
		return timeOffEmployeeId;
	}
	public void setTimeOffEmployeeId(long timeOffEmployeeId) {
		this.timeOffEmployeeId = timeOffEmployeeId;
	}

}
