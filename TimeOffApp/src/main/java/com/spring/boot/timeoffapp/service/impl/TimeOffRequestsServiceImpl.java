package com.spring.boot.timeoffapp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.boot.timeoffapp.dto.AvailableTimeDTO;
import com.spring.boot.timeoffapp.dto.TimeOffRequestDTO;
import com.spring.boot.timeoffapp.enums.Status;
import com.spring.boot.timeoffapp.exception.AvailableTimeNotEnoughException;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOff;
import com.spring.boot.timeoffapp.model.TimeOffEmployee;
import com.spring.boot.timeoffapp.model.TimeOffRequest;
import com.spring.boot.timeoffapp.repository.Available;
import com.spring.boot.timeoffapp.repository.TimeOffEmployeeRepository;
import com.spring.boot.timeoffapp.repository.TimeOffRequestsRepository;
import com.spring.boot.timeoffapp.requests.TimeOffRequestPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestRequestBody;
import com.spring.boot.timeoffapp.service.TimeOffRequestsService;
import com.spring.boot.timeoffapp.service.TimeOffService;
import com.spring.boot.timeoffapp.util.GetDateRange;
import com.spring.boot.timeoffapp.util.PagedResult;

@Service
public class TimeOffRequestsServiceImpl implements TimeOffRequestsService{

	@Autowired
	private TimeOffRequestsRepository timeOffRequestsRepository;
	
	@Autowired
	private TimeOffEmployeeRepository timeOffEmployeeRepository;
	
	@Autowired 
	private TimeOffService timeOffService;
	
	@Override
	public TimeOffRequest findTimeOffRequestById(Long managerID, Long employeeID, Long timeOffRequestID)
			throws ResourceNotFoundException {
		TimeOffRequest timeOffRequests = timeOffRequestsRepository.findByTimeOffRequestIdAndManagerIdAndEmployeeId(timeOffRequestID, managerID, employeeID);
		if(timeOffRequests == null) {
			throw new ResourceNotFoundException("Time Off request not found for given data");
		}
		return timeOffRequestsRepository.findByTimeOffRequestIdAndManagerIdAndEmployeeId(timeOffRequestID, managerID, employeeID);
	}
	

	
	@Override
	public void deleteTimeOffRequest(Long timeOffRequestId, Long employeeId) throws ResourceNotFoundException {
		TimeOffRequest timeOffRequest = timeOffRequestsRepository.findByTimeOffRequestIdAndEmployeeIdAndStatus(timeOffRequestId, employeeId, Status.PENDING);
		if(timeOffRequest != null) {
			timeOffRequestsRepository.deleteById(timeOffRequestId);
		}
		else {
			throw new ResourceNotFoundException("Time off request with id: " + timeOffRequestId + " not found for employee id: " + employeeId + " with PENDING status");
		}
		
		
	}

	@Override
	public PagedResult<TimeOffRequestDTO> findAll(Specification<TimeOffRequest> spec, Pageable pageable) throws ResourceNotFoundException {
		Page<TimeOffRequest> page = timeOffRequestsRepository.findAll(spec, pageable);
		if(page.getContent().isEmpty()) {
			throw new ResourceNotFoundException("Time Off requests not found for given data");
		}
		PagedResult<TimeOffRequestDTO> pagedResult = new PagedResult<>();
		List<TimeOffRequestDTO> timeOffRequestDTOs = page.getContent().stream().map(this::convertTimeOffRequestToDTO).collect(Collectors.toList());
		pagedResult.setContent(timeOffRequestDTOs);
		pagedResult.setPageNumber(page.getNumber());
		pagedResult.setPageSize(page.getSize());
		pagedResult.setTotalElements(page.getTotalElements());
		pagedResult.setTotalPages(page.getTotalPages());
		return pagedResult;
	}


	@Override
	public TimeOffRequestDTO updateTimeOffRequest(TimeOffRequestPatchRequestBody timeOffRequestPatchRequestBody,
			Long timeOffRequestId, Long managerID, Long employeeID) throws ResourceNotFoundException {
		TimeOffRequest timeOffRequest = timeOffRequestsRepository.findByTimeOffRequestIdAndManagerIdAndEmployeeId(timeOffRequestId, managerID, employeeID);
		if(timeOffRequest == null) {
			throw new ResourceNotFoundException("Time off request not found for id: " + timeOffRequestId + " and manager id: " + managerID);
		}
		timeOffRequest.setStatus(timeOffRequestPatchRequestBody.getStatus());
        timeOffRequestsRepository.save(timeOffRequest);
        return convertTimeOffRequestToDTO(timeOffRequest);
	}
	
	public TimeOffRequestDTO convertTimeOffRequestToDTO(TimeOffRequest timeOffRequest) {
		TimeOffRequestDTO timeOffRequestDTO = new TimeOffRequestDTO();
		timeOffRequestDTO.setTimeOffRequestId(timeOffRequest.getTimeOffRequestId());
		timeOffRequestDTO.setEmployeeId(timeOffRequest.getEmployeeId());
		timeOffRequestDTO.setManagerId(timeOffRequest.getManagerId());
		timeOffRequestDTO.setStartDate(timeOffRequest.getStartDate());
		timeOffRequestDTO.setEndDate(timeOffRequest.getEndDate());
		timeOffRequestDTO.setRequestedTime(timeOffRequest.getRequestedTime());
		timeOffRequestDTO.setTimeOffEmployeeId(timeOffRequest.getTimeOffEmployeeId());
		timeOffRequestDTO.setStatus(timeOffRequest.getStatus());
		return timeOffRequestDTO;
	}

	@Override
	public TimeOffRequestDTO saveTimeOffRequest(TimeOffRequestRequestBody timeOffRequestRequestBody, Long managerID, Long employeeID)
			throws ResourceNotFoundException, ParseException, Exception {
		long timeOffEmployeeId = this.getTimeOffEmployeeId(timeOffRequestRequestBody, employeeID);
		TimeOffRequest timeOffRequest = new TimeOffRequest();
		timeOffRequest.setEmployeeId(employeeID);
		timeOffRequest.setManagerId(managerID);
		timeOffRequest.setStartDate(timeOffRequestRequestBody.getStartDate());
		timeOffRequest.setEndDate(timeOffRequestRequestBody.getEndDate());
		timeOffRequest.setRequestedTime(timeOffRequestRequestBody.getRequestedTime());
		timeOffRequest.setTimeOffEmployeeId(timeOffEmployeeId);
		timeOffRequest.setStatus(Status.PENDING);
		timeOffRequestsRepository.save(timeOffRequest);
		return convertTimeOffRequestToDTO(timeOffRequest);
	}

	//////////GET TIME OFF EMPLOYEE ID
	public long getTimeOffEmployeeId(TimeOffRequestRequestBody timeOffRequestRequestBody, Long employeeID) throws ParseException, AvailableTimeNotEnoughException, ResourceNotFoundException {
		////////// CALCULATE AVAILABLE HOURS
		AvailableTimeDTO availableHours = this.findAvailableTimeById(employeeID, 
				timeOffRequestRequestBody.getTimeOffId(), timeOffRequestRequestBody.getHiringDate());
		////////////////// CHECK IF AMOUNT REQUESTED IS POSSIBLE
		if(timeOffRequestRequestBody.getRequestedTime()>(availableHours.getAvailableTime()-availableHours.getPendingTime())) {
			throw new AvailableTimeNotEnoughException("requestedTime is greater than available time");
		}
		/////////////////GENERATE TIME OFF EMPLOYEE AND GET ID
			Date[] dates = this.getValidDates(timeOffRequestRequestBody.getHiringDate());
		
			List<TimeOffEmployee> timeOffEmployee = timeOffEmployeeRepository.getTimeOffEmployees(employeeID, 
					timeOffRequestRequestBody.getTimeOffId(), dates[0], dates[1]);
			int timeOffEmployeeId = 0;
			
			if(timeOffEmployee.isEmpty()) {
				TimeOffEmployee timeOffEmployeeSave = this.timeOffRequestIdToTimeOffEmployee(timeOffRequestRequestBody, employeeID);
				timeOffEmployeeId = (int) timeOffEmployeeSave.getTimeOffEmployeeId();
			}
			else {
				TimeOffEmployee timeOffEmployeeGet = timeOffEmployee.get(0);
				timeOffEmployeeId = (int) timeOffEmployeeGet.getTimeOffEmployeeId();
			}
			return timeOffEmployeeId;
		}
	
	@Override
	public AvailableTimeDTO findAvailableTimeById(long employeeID, long timeOffID, Date hiringDate) 
			throws ParseException, ResourceNotFoundException {
		Date[] dates = this.getValidDates(hiringDate);
		Available available = timeOffEmployeeRepository.getAvailableHoursById(employeeID, dates[0], dates[1], timeOffID);
		return convertAvailableToDTO(available,timeOffID);
	}
	
	public TimeOffEmployee timeOffRequestIdToTimeOffEmployee(TimeOffRequestRequestBody timeOffRequestRequestBody, Long employeeID) throws ResourceNotFoundException {
		TimeOff timeOff = timeOffService.getTimeOffById(timeOffRequestRequestBody.getTimeOffId());
		TimeOffEmployee timeOffEmployee = new TimeOffEmployee();
		timeOffEmployee.setTimeOffId(timeOff.getTimeOffId());
		timeOffEmployee.setEmployeeId(employeeID);
		timeOffEmployee.setName(timeOff.getName());
		timeOffEmployee.setDescription(timeOff.getDescription());
		timeOffEmployee.setAmount(timeOff.getAmount());
		timeOffEmployee.setTimeUnit(timeOff.getTimeUnit());
		TimeOffEmployee timeOffEmployeeSave = timeOffEmployeeRepository.save(timeOffEmployee);
		return timeOffEmployeeSave;
	}
	
	@Override
	public Date[] getValidDates(Date hiringDate) throws ParseException {
		GetDateRange dateRange = new GetDateRange();
		StringBuilder[] validRange = dateRange.getValidDatesRange(hiringDate);
		Date startDate =new SimpleDateFormat("yyyy-M-d").parse(validRange[0].toString()); 
		Date endDate =new SimpleDateFormat("yyyy-M-d").parse(validRange[1].toString());
		Date[] valid = {startDate,endDate};
		return valid;
	}
	
	@Override
	public List<AvailableTimeDTO> findAvailableTime(long employeeID, Date hiringDate) throws ParseException, ResourceNotFoundException {
		Date[] dates = this.getValidDates(hiringDate);
		List<Available> availables = timeOffEmployeeRepository.getAvailableHours(employeeID, dates[0], dates[1]);
		List<AvailableTimeDTO> availableTimeDTOs = new ArrayList<>();
		for (Available available : availables) {
			availableTimeDTOs.add(convertAvailableToDTO(available,0));
		}
		return availableTimeDTOs;
	}
	
	public AvailableTimeDTO convertAvailableToDTO(Available available, long timeOffId) throws ResourceNotFoundException {
		AvailableTimeDTO availableTimeDTO = new AvailableTimeDTO();
		try {
			availableTimeDTO.setTimeOffId(available.getTimeOffId());
			availableTimeDTO.setName(available.getNameTimeOff());
			availableTimeDTO.setTotalTime(available.getTotalAmount());
			availableTimeDTO.setAvailableTime(available.getAvailableTime());
			availableTimeDTO.setPendingTime(available.getPendingTime());
			availableTimeDTO.setTimeUnit(available.getTimeUnit());
			return availableTimeDTO;
		} catch (Exception e) {
			throw new ResourceNotFoundException("Time off not found for id : " + timeOffId);
		}
	}

	

	
}



//////////////////////////////////////////
