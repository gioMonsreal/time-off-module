package edu.itk.project.security.requests;

import java.sql.Date;

import org.springframework.data.annotation.Id;

public class TimeOffRequestRequestBody {
	
	@Id
	private long timeOffRequestID;

	private long timeOffID;
	
	private Date startDate;
	
	private Date endDate;

	private int requestedTime;
	
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
