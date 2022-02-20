package com.spring.boot.timeoffapp.model;

import javax.persistence.*;

import com.spring.boot.timeoffapp.enums.TimeUnit;

@Table(name="time_off")
@Entity
public class TimeOff {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long timeOffId;

	private String name;
	
	private String description;
	  
	private int amount;

	@Enumerated(EnumType.STRING)
	private TimeUnit timeUnit;
	
	private int isActive;
	
    public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	public int getIsActive() {
		return isActive;
	}
	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}
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
	/*public String getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}*/

	

}
