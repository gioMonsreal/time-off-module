package com.spring.boot.timeoffapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.boot.timeoffapp.model.TimeOffEmployee;

@Repository
public interface TimeOffEmployeeRepository extends JpaRepository<TimeOffEmployee, Long>{
	
	@Query(value ="select * from time_off_employee where employee_id=?1 and time_off_id=?2 "
			+ "and creation_date>= ?3 and creation_date <?4", nativeQuery = true)
	public List<TimeOffEmployee> getTimeOffEmployees(long employeeId, long timeOffId, Date startDate, Date endDate); 
	
	@Query(value = "select f.time_off_id as timeOffId, f.name as nameTimeOff, \r\n"
			+ "CASE\r\n"
			+ "	WHEN b.time_off_id IS NOT NULL THEN b.amount\r\n"
			+ "	ELSE f.amount\r\n"
			+ "	END as totalAmount,\r\n"
			+ "	((CASE\r\n"
			+ "	WHEN b.time_off_id IS NOT NULL THEN b.amount\r\n"
			+ "	ELSE f.amount\r\n"
			+ "	END) - COALESCE((SELECT SUM(requested_time) as total_time\r\n"
			+ "			  from time_off_requests where employee_id = ?1\r\n"
			+ "			  and status = 'APPROVED' \r\n"
			+ "			  and time_off_employee_id = b.time_off_employee_id\r\n"
			+ "			  and start_date >= ?2 and end_date < ?3),0)) as availableTime,\r\n"
			+ "	(SELECT COALESCE((SELECT SUM(requested_time) \r\n"
			+ "		from time_off_requests where employee_id = ?1\r\n"
			+ "		and status = 'PENDING' and time_off_employee_id = b.time_off_employee_id\r\n"
			+ "		and start_date >= ?2 and end_date < ?3),0) as pendingTime),\r\n"
			+ "f.time_unit as timeUnit\r\n"
			+ "from time_off f\r\n"
			+ "LEFT JOIN time_off_employee b\r\n"
			+ "on (f.time_off_id = b.time_off_id\r\n"
			+ "   	and b.creation_date >= ?2 \r\n"
			+ "	and b.creation_date < ?3\r\n"
			+ "	and b.employee_id = ?1)\r\n"
			+ "where f.is_active = 1 ORDER BY timeOffId ASC", nativeQuery = true)
	public List<Available> getAvailableHours(long employeeId, Date startDate, Date endDate); 
	
	@Query(value = "SELECT * FROM (select f.time_off_id as timeOffId, f.name as nameTimeOff, \r\n"
			+ "CASE\r\n"
			+ "	WHEN b.time_off_id IS NOT NULL THEN b.amount\r\n"
			+ "	ELSE f.amount\r\n"
			+ "	END as totalAmount,\r\n"
			+ "	((CASE\r\n"
			+ "	WHEN b.time_off_id IS NOT NULL THEN b.amount\r\n"
			+ "	ELSE f.amount\r\n"
			+ "	END) - COALESCE((SELECT SUM(requested_time) as total_time\r\n"
			+ "			  from time_off_requests where employee_id = ?1\r\n"
			+ "			  and status = 'APPROVED' \r\n"
			+ "			  and time_off_employee_id = b.time_off_employee_id\r\n"
			+ "			  and start_date >= ?2 and end_date < ?3),0)) as availableTime,\r\n"
			+ "	(SELECT COALESCE((SELECT SUM(requested_time) \r\n"
			+ "		from time_off_requests where employee_id = ?1\r\n"
			+ "		and status = 'PENDING' and time_off_employee_id = b.time_off_employee_id\r\n"
			+ "		and start_date >= ?2 and end_date < ?3),0) as pendingTime),\r\n"
			+ "f.time_unit as timeUnit\r\n"
			+ "from time_off f\r\n"
			+ "LEFT JOIN time_off_employee b\r\n"
			+ "on (f.time_off_id = b.time_off_id\r\n"
			+ "   	and b.creation_date >= ?2 \r\n"
			+ "	and b.creation_date < ?3\r\n"
			+ "	and b.employee_id = ?1)\r\n"
			+ "where f.is_active = 1)\r\n"
			+ "AS y where y.timeOffId = ?4", nativeQuery = true)
	public Available getAvailableHoursById(long employeeId, Date startDate, Date endDate, long timeOffID); 
}
