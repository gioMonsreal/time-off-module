package com.spring.boot.timeoffapp.util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class GetDateRange {
	
	
	public StringBuilder[] getValidDatesRange(java.util.Date hiringDate) {
	 
		//////get month and date from hiring date
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(hiringDate);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		////////get current date
		LocalDate now = LocalDateTime.now().toLocalDate();
		
		////create date with month and day from hiring date and year of current year
		String dateString = ""+now.getYear()+"-"+month+"-"+day;
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
		LocalDate hiringDateNow = LocalDate.parse(dateString, dateFormatter);
		
		StringBuilder startDate = new StringBuilder("");
		StringBuilder endDate = new StringBuilder("");

		if(now.compareTo(hiringDateNow)>=0) {
			startDate.append(hiringDateNow.toString());
			endDate.append(hiringDateNow.plusYears(1).toString());
		}
		else if(now.compareTo(hiringDateNow)<0) {
			startDate.append(hiringDateNow.minusYears(1).toString());
			endDate.append(hiringDateNow.toString());
		}
		
		StringBuilder[] dates = {startDate,endDate};
		
		return dates;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*//////get month and date from hiring date
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(hiringDate);
	int month = calendar.get(Calendar.MONTH) + 1;
	int day = calendar.get(Calendar.DAY_OF_MONTH);
	
	////////get current date
	LocalDate now = LocalDateTime.now().toLocalDate();
	
	////create date with month and day from hiring date and year of current year
	String dateString = ""+now.getYear()+"-"+month+"-"+day;
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
	LocalDate hiringDateNow = LocalDate.parse(dateString, dateFormatter);
	
	StringBuilder startDate = new StringBuilder("");
	StringBuilder endDate = new StringBuilder("");

	if(now.compareTo(hiringDateNow)>=0) {
		startDate.append(hiringDateNow.toString());
		endDate.append(hiringDateNow.plusYears(1).toString());
	}
	else if(now.compareTo(hiringDateNow)<0) {
		startDate.append(hiringDateNow.minusYears(1).toString());
		endDate.append(hiringDateNow.toString());
	}*/


}
