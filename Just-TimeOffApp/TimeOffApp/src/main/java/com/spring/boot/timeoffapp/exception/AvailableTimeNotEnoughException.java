package com.spring.boot.timeoffapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AvailableTimeNotEnoughException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public AvailableTimeNotEnoughException (String message) {
		super(message);
	}
	
	
}