package com.spring.boot.timeoffapp.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TimeOffPatchRequestBody {
	
	@NotNull(message = "amount can't be null")
	@Min(value=1, message="amount must be equal or greater than 1")  
	private int amount;
	
	@NotNull(message = "isActive can't be null")
	@Min(value = 1, message = "isActive can only be a 1 or 2")
	@Max(value = 2, message = "isActive can only be a 1 or 2")
	private int isActive;

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
