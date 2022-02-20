package com.spring.boot.timeoffapp.exception;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	public static final String DEFAULT_ERROR_VIEW = "Something went wrong";
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = InvocationTargetException.class)
    public ResponseEntity<Object> invocationTargetException(InvocationTargetException ex) {
        String error = "Expected 2 arguments: requestedBefore & requestedAfter ";
		WrongBody wrongBody = new WrongBody();
	  	wrongBody.setMessage(error);
	  	List<String> list = new ArrayList<>();
	  	list.add(ex.getCause().getMessage());
	  	wrongBody.setErrors(list);
		return new ResponseEntity<>(wrongBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({PropertyReferenceException.class}) 
	protected ResponseEntity<Object> handlePropertyReferenceException(PropertyReferenceException ex, WebRequest request){
		String error = "No property reference found (Could not sort according to value given";
		WrongBody wrongBody = new WrongBody();
	  	wrongBody.setMessage(error);
	  	List<String> list = new ArrayList<>();
	  	list.add(ex.getMessage());
	  	wrongBody.setErrors(list);
		return new ResponseEntity<>(wrongBody, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({UnsatisfiedServletRequestParameterException.class})
		@ResponseStatus(value = HttpStatus.NOT_FOUND)
		protected ResponseEntity<Object> handleRequestException(Exception ex) {
			String error = "Unsatisfied request parameters";
			WrongBody wrongBody = new WrongBody();
		  	wrongBody.setMessage(error);
		  	List<String> list = new ArrayList<>();
		  	list.add(ex.getMessage());
		  	wrongBody.setErrors(list);
		return new ResponseEntity<>(wrongBody, HttpStatus.NOT_FOUND);
		}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@Override
    protected ResponseEntity <Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "No handler found";
		WrongBody wrongBody = new WrongBody();
	  	wrongBody.setMessage(error);
	  	List<String> list = new ArrayList<>();
	  	list.add(ex.getMessage());
	  	wrongBody.setErrors(list);
		
		return new ResponseEntity<>(wrongBody, HttpStatus.NOT_FOUND);
    }
    
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       
		String error = "Malformed JSON request";
		WrongBody wrongBody = new WrongBody();
	  	wrongBody.setMessage(error);
	  	List<String> list = new ArrayList<>();
	  	list.add(ex.getMessage());
	  	wrongBody.setErrors(list);
	    
	    return new ResponseEntity<>(wrongBody, HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       
		String error = "Http request method not supported";
		WrongBody wrongBody = new WrongBody();
	  	wrongBody.setMessage(error);
	  	List<String> list = new ArrayList<>();
	  	list.add(ex.getMessage());
	  	wrongBody.setErrors(list);
	 
	    return new ResponseEntity<>(wrongBody, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
		@ResponseStatus(HttpStatus.BAD_REQUEST)
	  @Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(
	      MethodArgumentNotValidException ex, HttpHeaders headers,
	      HttpStatus status, WebRequest request) {
	
	    List<String> errors = ex.getBindingResult()
	        .getAllErrors()
	        .stream()
	        .map(DefaultMessageSourceResolvable::getDefaultMessage)
	        .collect(Collectors.toList());
	    
	    WrongBody wrongBody = new WrongBody();
	    wrongBody.setMessage("Request body not correct, check that all the attributes are included and in the right format");
	    wrongBody.setErrors(errors);
	
	    return new ResponseEntity<>(wrongBody, HttpStatus.BAD_REQUEST);
	  }
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
	    Map<String, String> body = new HashMap<>();
	
	    body.put("message", ex.getMessage());
	
	    List<String> list = new ArrayList<>();
	  	list.add(ex.getMessage());
	    WrongBody wrongBody = new WrongBody();
	    wrongBody.setMessage("Resource not found");
	    wrongBody.setErrors(list);
	    
	    return new ResponseEntity<>(wrongBody, HttpStatus.NOT_FOUND);
	  }
	
	@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
	@ExceptionHandler(AvailableTimeNotEnoughException.class)
	public ResponseEntity<?> availableTimeNotEnoughException(AvailableTimeNotEnoughException ex, WebRequest request) {
		    List<String> list = new ArrayList<>();
		  	list.add(ex.getMessage());
		    WrongBody wrongBody = new WrongBody();
		    wrongBody.setMessage("Available time not enough");
		    wrongBody.setErrors(list);
		    
		    return new ResponseEntity<>(wrongBody, HttpStatus.NOT_ACCEPTABLE);
		  }
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
		public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
	    List<String> errors = new ArrayList<>();
	    ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

	    Map<String, List<String>> result = new HashMap<>();
	    result.put("errors", errors);

	    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	  }
  
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
	  public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
	
		  	WrongBody wrongBody = new WrongBody();
		  	wrongBody.setMessage("Argument type mismatch");
		  	List<String> list = new ArrayList<>();
		  	list.add(ex.getMessage());
		  	wrongBody.setErrors(list);
		    return new ResponseEntity<>(wrongBody, HttpStatus.BAD_REQUEST);
		  }
}
