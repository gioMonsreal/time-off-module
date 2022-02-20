package com.spring.boot.timeoffapp.annotations;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.boot.timeoffapp.service.TimeOffRequestsService;

public class DateRangeValidator implements ConstraintValidator <DateRange, Object>{
	
	private String startDateFieldName;
	private String endDateFieldName;
	private String hiringDateFieldName;
	
	@Autowired
	private TimeOffRequestsService timeOffRequestService;

	
	@Override
	  public void initialize(final DateRange constraintAnnotation) {
		startDateFieldName = constraintAnnotation.startDate();
		endDateFieldName = constraintAnnotation.endDate();
		hiringDateFieldName = constraintAnnotation.hiringDate();
	  }

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
	    try {
	        final Field startDateField = value.getClass().getDeclaredField(startDateFieldName);
	        startDateField.setAccessible(true);

	        final Field endDateField = value.getClass().getDeclaredField(endDateFieldName);
	        endDateField.setAccessible(true);
	        
	        final Field hirindDateField = value.getClass().getDeclaredField(hiringDateFieldName);
	        hirindDateField.setAccessible(true);

	        final Date beforeDate = (Date) startDateField.get(value);
	        final Date afterDate = (Date) endDateField.get(value);
	        final Date hiringDate = (Date) hirindDateField.get(value);
	        
	        ZoneId defaultZoneId = ZoneId.systemDefault();
	        Date[] dates = timeOffRequestService.getValidDates(hiringDate);
	        //StringBuilder dateEndBuilder = dateStringBuilder[1];
	        //StringBuilder startEndBuilder = dateStringBuilder[1];
	        Date dateEnd = dates[1];
	        //Date dateEnd =new SimpleDateFormat("yyyy-M-d").parse(dateEndBuilder.toString());  
	        //Date dateStart =new SimpleDateFormat("yyyy-M-d").parse(startEndBuilder.toString());  
	        
			LocalDate now = LocalDateTime.now().toLocalDate();
			Date current = Date.from(now.atStartOfDay(defaultZoneId).toInstant());
			
			System.out.println(beforeDate.after(current) && beforeDate.before(dateEnd) && afterDate.after(current) && afterDate.before(dateEnd));
			System.out.println("beforeDateInput: " +beforeDate);
			System.out.println("endDateInput: " +afterDate);
			System.out.println("currentDate: " + now);
			System.out.println("endHiring: " + dateEnd);
	        return beforeDate.after(current) && beforeDate.before(dateEnd) && afterDate.after(current) && afterDate.before(dateEnd);
	        //return true;
	        
	      } catch (final Exception e) {
	        e.printStackTrace();
	        return false;
	      }
	}

}
