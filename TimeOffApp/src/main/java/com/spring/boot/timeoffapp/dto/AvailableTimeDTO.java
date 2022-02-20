package com.spring.boot.timeoffapp.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.spring.boot.timeoffapp.enums.TimeUnit;

public class AvailableTimeDTO {
	
	private int timeOffId;
	
	private String name;
	 
	private int availableTime;
	
	private int totalTime;
	
	private int pendingTime;
	
	@Enumerated(EnumType.STRING)
	private TimeUnit timeUnit;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAvailableTime() {
		return availableTime;
	}
	public void setAvailableTime(int availableTime) {
		this.availableTime = availableTime;
	}
	public int getPendingTime() {
		return pendingTime;
	}
	public void setPendingTime(int pendingTime) {
		this.pendingTime = pendingTime;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public int getTimeOffId() {
		return timeOffId;
	}
	public void setTimeOffId(int timeOffId) {
		this.timeOffId = timeOffId;
	}
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	
	
	
	
}

