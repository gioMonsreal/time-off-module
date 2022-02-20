package com.spring.boot.timeoffapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.spring.boot.timeoffapp.dto.TimeOffDTO;
import com.spring.boot.timeoffapp.exception.ResourceNotFoundException;
import com.spring.boot.timeoffapp.model.TimeOff;
import com.spring.boot.timeoffapp.repository.TimeOffRepository;
import com.spring.boot.timeoffapp.requests.TimeOffPatchRequestBody;
import com.spring.boot.timeoffapp.requests.TimeOffRequestBody;
import com.spring.boot.timeoffapp.service.TimeOffService;
import com.spring.boot.timeoffapp.util.PagedResult;

@Service
public class TimeOffServiceImpl implements TimeOffService{
	
	@Autowired
	private TimeOffRepository timeOffRepository;
	
	@Override
	public PagedResult<TimeOffDTO> findAll(Specification<TimeOff> spec, Pageable pageable) throws ResourceNotFoundException {
		Page<TimeOff> page = timeOffRepository.findAll(spec,pageable);
		if(page.getContent().isEmpty()) {
			throw new ResourceNotFoundException("Time Off not found for given data");
		}
		PagedResult<TimeOffDTO> pagedResult = new PagedResult<>();
		List<TimeOffDTO> timeOffDTOs = page.getContent().stream().map(this::convertTimeOffToDTO).collect(Collectors.toList());
		pagedResult.setContent(timeOffDTOs);
		pagedResult.setPageNumber(page.getNumber());
		pagedResult.setPageSize(page.getSize());
		pagedResult.setTotalElements(page.getTotalElements());
		pagedResult.setTotalPages(page.getTotalPages());
		
		return pagedResult;
	}
	
	//////////////////GET TIME OFF BY ID
	@Override
	public TimeOff getTimeOffById(Long timeOffId) throws ResourceNotFoundException{
		TimeOff timeOff = timeOffRepository.findById(timeOffId)
				.orElseThrow(() -> new ResourceNotFoundException("Time Off not found for this id : " + timeOffId));
		return timeOff;
	}
	
	///////////////////SAVE TIME OFF
	@Override
	public TimeOffDTO saveTimeOff(TimeOffRequestBody timeOffRequestBody) {
		TimeOff timeOff = new TimeOff();
		timeOff.setName(timeOffRequestBody.getName());
		timeOff.setDescription(timeOffRequestBody.getDescription());
		timeOff.setAmount(timeOffRequestBody.getAmount());
		timeOff.setTimeUnit(timeOffRequestBody.getTimeUnit());
		timeOff.setIsActive(1);
		timeOffRepository.save(timeOff);
		return convertTimeOffToDTO(timeOff);
	}
	
	////////////PATCH TIME OFF 
	@Override
	public TimeOffDTO patchTimeOff(Long timeOffId, TimeOffPatchRequestBody timeOffPatchRequestBody) throws ResourceNotFoundException {
		TimeOff timeOff = timeOffRepository.findById(timeOffId)
				.orElseThrow(() -> new ResourceNotFoundException("Time Off not found on : "+ timeOffId));
		timeOff.setAmount((timeOffPatchRequestBody.getAmount()));
		timeOff.setIsActive(timeOffPatchRequestBody.getIsActive());
		timeOffRepository.save(timeOff);
		return convertTimeOffToDTO(timeOff);
	}

	////////////CONVERT TIME OFF TO DTO
	public TimeOffDTO convertTimeOffToDTO(TimeOff timeOff) {
		TimeOffDTO timeOffDTO = new TimeOffDTO();
		timeOffDTO.setTimeOffId(timeOff.getTimeOffId());
		timeOffDTO.setName(timeOff.getName());
		timeOffDTO.setDescription(timeOff.getDescription());
		timeOffDTO.setAmount(timeOff.getAmount());
		timeOffDTO.setTimeUnit(timeOff.getTimeUnit());
		timeOffDTO.setIsActive(timeOff.getIsActive());
		return timeOffDTO;
	}
	

}
