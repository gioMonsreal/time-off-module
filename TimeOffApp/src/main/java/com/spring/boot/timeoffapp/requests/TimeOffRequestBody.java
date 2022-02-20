package com.spring.boot.timeoffapp.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.spring.boot.timeoffapp.enums.TimeUnit;


public class TimeOffRequestBody {
	
	@NotEmpty(message = "name can't be empty")
	@Size(max = 30, message = "name can't be greater than 30 characters")
	private String name;
	
	@NotEmpty(message = "description can't be empty")
	@Size(min = 20, max = 200, message = "The length for the description should be between 20 and 200 characters")
	private String description;
	
	@NotNull(message = "amount can't be null")
	@Min(value=1, message="amount must be equal or greater than 1")  
	private int amount;
	
	@NotNull(message = "timeUnit can't be empty, accepted values are: [HOURS,DAYS]")
	private TimeUnit timeUnit;
	
    public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
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
