package com.spring.boot.timeoffapp.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.spring.boot.timeoffapp.model.TimeOff;

@Repository
public interface TimeOffRepository extends PagingAndSortingRepository<TimeOff, Long>, JpaSpecificationExecutor<TimeOff>{

	
}