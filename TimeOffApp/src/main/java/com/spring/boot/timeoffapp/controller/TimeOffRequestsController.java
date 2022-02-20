package com.spring.boot.timeoffapp.controller;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.spring.boot.timeoffapp.dto.TimeOffRequestDTO;
import com.spring.boot.timeoffapp.enums.Status;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOffRequest;
import com.spring.boot.timeoffapp.requests.TimeOffRequestPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestRequestBody;
import com.spring.boot.timeoffapp.service.TimeOffRequestsService;
import com.spring.boot.timeoffapp.util.PagedResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.kaczmarzyk.spring.data.jpa.domain.DateBetween;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@SuppressWarnings("deprecation")
@RestController
@Tag(name = "Time Off Requests")
public class TimeOffRequestsController {

	@Autowired
	private TimeOffRequestsService timeOffRequestsService;
	
	/////////////////////////////////////// GET ALL REQUESTS FROM EMPLOYEES BY MANAGER
	@Operation(summary = "Get time off requests by manager Id", responses = {
			@ApiResponse(description = "Get all time off requests from the employees of a managers", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestDTO.class))),
			@ApiResponse(description = "Time Off requests for manager id given not found, ",  responseCode = "404", content = @Content)})
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping(value="/managers/{managerID}/timeOffRequests", method = RequestMethod.GET)
	public PagedResult<TimeOffRequestDTO> findTimeOffRequestByManager (
			
			@PathVariable(value = "managerID") Long managerID,
			
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			@RequestParam(value = "requestedAfter",required = false) Date requestedAfter,
			
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			@RequestParam(value = "requestedBefore",required = false) Date requestedBefore,
			@RequestParam(value = "status",required = false) Status status,
			@And({
				@Spec(pathVars = "managerID", spec = Equal.class, path = "managerId"),
				@Spec(path = "status", params="status",spec = Equal.class),
				@Spec(
						path = "startDate",
						params = { "requestedAfter", "requestedBefore" },
						spec = DateBetween.class
						)
				
				})
			Specification<TimeOffRequest> spec, @SortDefault(sort = "timeOffRequestId", direction = Sort.Direction.ASC) Sort sort, Pageable pageable)
			throws ResourceNotFoundException{
		
		return timeOffRequestsService.findAll(spec, pageable);		
	}
	
	////////////////////////////////////GET ALL REQUESTS FROM ONE EMPLOYEE BY MANAGER
	@Operation(summary = "Get time off requests by manager id and employee id", responses = {
			@ApiResponse(description = "Get time off requests based on ", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestDTO.class))),
			@ApiResponse(description = "Time Off requests for manager id and employee id not found",  responseCode = "404", content = @Content)})
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	@RequestMapping(value="/managers/{managerID}/employees/{employeeID}/timeOffRequests", method = RequestMethod.GET)
	public ResponseEntity<Object> findTimeOffRequestByManagerAndEmployee(
			@PathVariable(value = "managerID") Long managerID,
			@PathVariable(value = "employeeID") Long employeeID,
			
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			@RequestParam(value = "requestedAfter",required = false) Date requestedAfter,
			
			@DateTimeFormat(pattern = "yyyy-MM-dd") 
			@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			@RequestParam(value = "requestedBefore",required = false) Date requestedBefore,
			@RequestParam(value = "status",required = false) Status status,
			
			//@RequestParam(value="hiringDate", required=false) Date hiringDate,
			
			@And({
				@Spec(pathVars = "managerID", spec = Equal.class, path = "managerId"),
				@Spec(pathVars = "employeeID", spec = Equal.class, path = "employeeId"),
				@Spec(path = "status", params="status",spec = Equal.class),
				@Spec(
						path = "startDate",
						params = { "requestedAfter", "requestedBefore" },
						spec = DateBetween.class
						)})
			Specification<TimeOffRequest> spec, @SortDefault(sort = "timeOffRequestId", direction = Sort.Direction.ASC) Pageable pageable)
	throws ResourceNotFoundException  {
		return new ResponseEntity<Object>(timeOffRequestsService.findAll(spec, pageable),HttpStatus.OK);	
		
	}

	
	/////////////////////////////////////// 3 ENDPOINT
	@Operation(summary = "Get time off request by id", responses = {
			@ApiResponse(description = "Get time off request with manager id, employee id and time off request id", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestDTO.class))),
			@ApiResponse(description = "Time Off requests for manager id and employee id with time off request id given not found",  responseCode = "404", content = @Content)})
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	@RequestMapping(value="/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.GET)
	public ResponseEntity<Object> findTimeOffRequestByManagerAndEmployeeAndTimeOffRequestId(
			@PathVariable(value = "managerID") Long managerID, 
			@PathVariable(value = "employeeID") Long employeeID, 
			@PathVariable(value = "timeOffRequestID") Long timeOffRequestID) 
			throws ResourceNotFoundException{
		return new ResponseEntity<Object>(timeOffRequestsService.findTimeOffRequestById(managerID, employeeID, timeOffRequestID),HttpStatus.OK);		
	}
	
	/////////////////////////////////////// DELETE ENDPOINT
	@Operation(summary = "Delete pending time off requests by id", responses = {
	@ApiResponse(description = "The employee can delete his/her own time off request while still pending", responseCode = "200",
	content = @Content(mediaType = "application/json")),
	@ApiResponse(description = "Time Off requests for employee id and time off request id given not found",  responseCode = "404", content = @Content)})
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	@RequestMapping(value="/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteTimeOffRequestByEmployeeAndTimeOffRequestId( 
	@PathVariable(value = "employeeID") Long employeeID, 
	@PathVariable(value = "timeOffRequestID") Long timeOffRequestID) 
	throws ResourceNotFoundException{
		timeOffRequestsService.deleteTimeOffRequest(timeOffRequestID, employeeID);
		return ("Time Off request from employee successfully deleted!");
		//return new ResponseEntity<Object>("Time Off request from employee successfully deleted!",HttpStatus.NO_CONTENT);		
	}
	
	
	///////////////////////////////PATCH
	@Operation(summary = "Update status of time off request", responses = {
			@ApiResponse(description = "Update status of an employee´s time off request from manager", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestDTO.class))),
			@ApiResponse(description = "Time Off requests for manager id and time off request not found",  responseCode = "404", content = @Content)})
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	@RequestMapping(value = "/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.PATCH)
	public ResponseEntity<Object> updateStatusFromRequestFromAManager(@PathVariable(value = "managerID") Long managerID, 
			@PathVariable(value = "timeOffRequestID") Long timeOffRequestID,  
			@PathVariable(value = "employeeID") Long employeeID,
			@Valid @RequestBody TimeOffRequestPatchRequestBody timeOffRequestPatchRequestBody)
			throws ResourceNotFoundException{
		return new ResponseEntity<Object>(timeOffRequestsService.updateTimeOffRequest(timeOffRequestPatchRequestBody, timeOffRequestID, managerID, employeeID),HttpStatus.OK);		
	}
	
	
	///////////////////////////////POST
	@Operation(summary = "Create new time off request", responses = {
			@ApiResponse(description = "Create new time off request with given employee and manager id", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestRequestBody.class))),
			@ApiResponse(description = "Time Off not found",  responseCode = "400", content = @Content),
			@ApiResponse(description = "Malformed Json body",  responseCode = "404", content = @Content)})
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	@RequestMapping(value = "/managers/{managerID}/employees/{employeeID}/timeOffRequests", method = RequestMethod.POST)
	public ResponseEntity<Object> createTimeOffRequest(@Valid @RequestBody TimeOffRequestRequestBody timeOffRequestRequestBody,
			@PathVariable(value = "managerID") Long managerID, 
			@PathVariable(value = "employeeID") Long employeeID) throws Exception {
		return new ResponseEntity<Object>(timeOffRequestsService.saveTimeOffRequest(timeOffRequestRequestBody, managerID, employeeID), HttpStatus.CREATED);
	}
	
	/////////////////////////////GET AVAILABLE TIME
	@RequestMapping(value = "/employees/{employeeID}/availableTimes", params = "hiringDate", method = RequestMethod.GET)
	public ResponseEntity<Object> getAvailableTimeByEmployeeId(
			@PathVariable(value = "employeeID") Long employeeID, 
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			Date hiringDate) throws ParseException, ResourceNotFoundException {
    		return new ResponseEntity<Object>(timeOffRequestsService.findAvailableTime(employeeID, hiringDate),HttpStatus.OK);
    }
	
	@Operation(summary = "Get time off available times by employee", responses = {
			@ApiResponse(description = "Get time off available times of an employee", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestDTO.class)))})
	@RequestMapping(value = "/employees/{employeeID}/availableTimes", params = {"hiringDate", "timeOffID"}, method = RequestMethod.GET)
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	public ResponseEntity<Object> getAvailableTimeByEmployeeIdAndTimeOffId(@PathVariable(value = "employeeID") Long employeeID,
			@RequestParam(required = false) Long timeOffID,
    		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE) 
			Date hiringDate) throws ParseException, ResourceNotFoundException {
    		return new ResponseEntity<Object>(timeOffRequestsService.findAvailableTimeById(employeeID, timeOffID, hiringDate),HttpStatus.OK);
    }
	
	
}