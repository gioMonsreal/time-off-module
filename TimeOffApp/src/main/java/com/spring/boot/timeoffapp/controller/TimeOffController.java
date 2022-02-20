package com.spring.boot.timeoffapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.timeoffapp.dto.TimeOffDTO;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOff;
import com.spring.boot.timeoffapp.requests.TimeOffPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestBody;
import com.spring.boot.timeoffapp.service.TimeOffService;

import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

@RestController
@Tag(name="Time Offs")
public class TimeOffController {
	
	@Autowired
	private TimeOffService timeOffService;
	
	////////////////////////////////////////GET TIME OFFS
	@Operation(summary = "Get time offs", responses = {
			@ApiResponse(description = "Get all active and inactive time offs, results can be sorted by any attribute (e.g. sort=name)", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffDTO.class)))})
	@GetMapping("/timeOffs")
	public ResponseEntity<Object> findTimeOffs(
			@RequestParam(value = "isActive",required = false) Integer isActive,
			@RequestParam(value = "timeUnit",required = false) String timeUnit,
			@And({
				@Spec(path = "timeUnit", params="timeUnit", spec = Equal.class),
				@Spec(path = "isActive", params="isActive", spec = Equal.class)})
			Specification<TimeOff> spec, @SortDefault(sort = "timeOffId", direction = Sort.Direction.ASC) @ApiParam(name = "Pageable filter", required = false) Pageable pageable) throws ResourceNotFoundException {
	    return new ResponseEntity<Object>(timeOffService.findAll(spec, pageable),HttpStatus.OK);
	}
	
	////////////////////////////////////////GET TIME OFF BY ID
	@GetMapping("/timeOffs/{id}")
	@Operation(summary = "Get time off by id", responses = {
			@ApiResponse(description = "Get active or inactive time off by id", responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffDTO.class))),
			@ApiResponse(description = "Time Off not found",  responseCode = "404", content = @Content)})
	public ResponseEntity<Object> getTimeOffById(@PathVariable(value = "id") Long timeOffId)
			throws ResourceNotFoundException{
		return new ResponseEntity<Object>(timeOffService.getTimeOffById(timeOffId), HttpStatus.OK);
	}
	
	////////////////////////////////////////CREATE TIME OFF
	@PostMapping("/timeOffs")
	@Operation(summary = "Add a time off", responses = {
			@ApiResponse(description = "Create a new time off type, by default is active", responseCode = "201",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffRequestBody.class))), 
			@ApiResponse(description = "Malformed json body",  responseCode = "400", content = @Content)})
	public ResponseEntity<Object> createTimeOff(@Valid @RequestBody TimeOffRequestBody timeOffRequestBody) {
		return new ResponseEntity<Object>(timeOffService.saveTimeOff(timeOffRequestBody), HttpStatus.CREATED);
	}
	
	
	////////////////////////////////////////UPDATE TIME OFF BY ID 
	@PatchMapping("/timeOffs/{id}")
	@Operation(summary = "Update a time off", responses = {
			@ApiResponse(description = "Update the amount of time off or update status that shows if a time off is active or not", 
					responseCode = "200",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = TimeOffDTO.class))), 
			@ApiResponse(description = "Time Off not found",  responseCode = "404", content = @Content)})
    public ResponseEntity<Object> updateAmount(
    @PathVariable(value = "id") Long timeOffId,
    @Valid @RequestBody TimeOffPatchRequestBody timeOffPatchRequestBody) throws ResourceNotFoundException {
		return new ResponseEntity<Object>(timeOffService.patchTimeOff(timeOffId, timeOffPatchRequestBody), HttpStatus.CREATED);
   }
	
	

}