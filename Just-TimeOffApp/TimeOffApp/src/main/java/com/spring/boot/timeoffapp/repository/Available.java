package com.spring.boot.timeoffapp.repository;

import com.spring.boot.timeoffapp.enums.TimeUnit;

public interface Available {
	
	int getTimeOffId();
	String getNameTimeOff();
	int getTotalAmount();
	int getAvailableTime();
	int getPendingTime();
	TimeUnit getTimeUnit();
	

}
