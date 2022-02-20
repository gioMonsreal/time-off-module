package com.spring.boot.timeoffapp.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.spring.boot.timeoffapp.enums.Status;
import com.spring.boot.timeoffapp.model.TimeOffRequest;

@Repository
public interface TimeOffRequestsRepository extends PagingAndSortingRepository<TimeOffRequest, Long>, JpaSpecificationExecutor<TimeOffRequest>{
	
	TimeOffRequest findByTimeOffRequestIdAndEmployeeIdAndStatus(Long timeOffRequestId, Long employeeID, Status status);

	TimeOffRequest findByTimeOffRequestIdAndManagerIdAndEmployeeId(Long timeOffRequestID, Long managerID,
			Long employeeID);
	
	@Query(value="DELETE FROM time_off_requests WHERE time_off_request_id=?1 AND employee_id=?2 AND status='PENDING'", nativeQuery=true)
	void deleteTimeOffRequestByEmployee(Long timeOffRequestId, Long employeeId);
}















/*List<TimeOffRequest> findByEmployeeId(Long employeeID);

@Query(value = "select * from time_off_requests where employee_id=?1 and start_date >= ?2", nativeQuery = true)
List<TimeOffRequest> findByEmployeeAndDate(Long employeeID, Date sinceDate);

List<TimeOffRequest> findByManagerId(Long managerID);

@Query(value = "select * from time_off_requests where manager_id=?1 and start_date >= ?2", nativeQuery = true)
List<TimeOffRequest> findByManagerIdAndDate(Long managerID, Date sinceDate);

@Query(value = "select * from time_off_requests where manager_id=?1 and employee_id=?2 and start_date >= ?3", nativeQuery = true)
List<TimeOffRequest> findAllManagerIdAndEmployeeIdWithDate(Long managerID, Long employeeID, Date sinceDate);



List<TimeOffRequest> findByManagerIdAndEmployeeId(Long managerId, Long employeeId);

TimeOffRequest findByTimeOffRequestIdAndManagerIdAndEmployeeId(Long timeOffRequestId, Long managerID, Long employeeID);

//Object findAll(Specification<TimeOffRequest> timeOffSpecification);

//List<TimeOffRequest> findByManagerIdAnd*/
