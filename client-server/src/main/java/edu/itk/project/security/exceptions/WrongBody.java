package edu.itk.project.security.exceptions;

import java.util.List;

public class WrongBody {

	private String message;
	private List<String> errors;
	
	
	public List<String> getErrors() {
		return errors;
	}
	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
	
	
	
}
