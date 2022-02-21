package com.spring.boot.timeoffapp.model;

import javax.persistence.*;

import com.spring.boot.timeoffapp.enums.TimeUnit;


@Table(name="time_off_employee")
@Entity
public class TimeOffEmployee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long timeOffEmployeeId;
	
	private long timeOffId;
	
	private long employeeId;

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
	
	public long getTimeOffEmployeeId() {
		return timeOffEmployeeId;
	}
	public void setTimeOffEmployeeId(long timeOffEmployeeId) {
		this.timeOffEmployeeId = timeOffEmployeeId;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
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

}
