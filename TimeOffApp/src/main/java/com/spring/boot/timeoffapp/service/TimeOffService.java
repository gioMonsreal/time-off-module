package com.spring.boot.timeoffapp.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.boot.timeoffapp.dto.TimeOffDTO;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOff;
import com.spring.boot.timeoffapp.requests.TimeOffPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestBody;
import com.spring.boot.timeoffapp.util.PagedResult;

@Service
public interface TimeOffService {
	
	PagedResult<TimeOffDTO> findAll(Specification<TimeOff> spec, Pageable pageable) throws ResourceNotFoundException;
	
	TimeOff getTimeOffById(Long timeOffId) throws ResourceNotFoundException;

	TimeOffDTO saveTimeOff(TimeOffRequestBody timeOffRequestBody);

	TimeOffDTO patchTimeOff(Long timeOffId, TimeOffPatchRequestBody timeOffDetails) throws ResourceNotFoundException;
	
	
}
