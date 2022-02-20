package com.spring.boot.timeoffapp.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.spring.boot.timeoffapp.enums.TimeUnit;

public class TimeOffDTO {
	
	private long timeOffId;
	
	private String name;
	
	private String description;
 
	private int amount;

	//private String timeUnit;
	@Enumerated(EnumType.STRING)
	private TimeUnit timeUnit;
	
    public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	private int isActive;
	
	public long getTimeOffId() {
		return timeOffId;
	}
	public void setTimeOffId(long timeOffId) {
		this.timeOffId = timeOffId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

}
