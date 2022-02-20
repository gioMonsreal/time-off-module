package com.spring.boot.timeoffapp.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.boot.timeoffapp.dto.AvailableTimeDTO;
import com.spring.boot.timeoffapp.dto.TimeOffRequestDTO;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOffRequest;
import com.spring.boot.timeoffapp.requests.TimeOffRequestPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestRequestBody;
import com.spring.boot.timeoffapp.util.PagedResult;

@Service
public interface TimeOffRequestsService {

	List<AvailableTimeDTO> findAvailableTime(long employeeID, Date hiringDate)
			throws ParseException, ResourceNotFoundException;

	Date[] getValidDates(Date hiringDate) throws ParseException;

	AvailableTimeDTO findAvailableTimeById(long employeeID, long timeOffID, Date hiringDate)
			throws ParseException, ResourceNotFoundException;

	TimeOffRequestDTO saveTimeOffRequest(TimeOffRequestRequestBody timeOffRequestRequestBody, Long employeeID,
			Long managerID) throws ResourceNotFoundException, ParseException, Exception;


	TimeOffRequest findTimeOffRequestById(Long managerID, Long employeeID, Long timeOffRequestID)
			throws ResourceNotFoundException;

	PagedResult<TimeOffRequestDTO> findAll(Specification<TimeOffRequest> spec, Pageable pageable)
			throws ResourceNotFoundException;


	TimeOffRequestDTO updateTimeOffRequest(TimeOffRequestPatchRequestBody timeOffRequestPatchRequestBody,
			Long timeOffRequestId, Long managerID, Long employeeID) throws ResourceNotFoundException;


	void deleteTimeOffRequest(Long timeOffRequestId, Long employeeId) throws ResourceNotFoundException;

	
	
}
