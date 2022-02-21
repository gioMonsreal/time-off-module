package edu.itk.project.security.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.SortDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import edu.itk.project.security.requests.*;
import edu.itk.project.security.exceptions.GlobalExceptionHandler;
import edu.itk.project.security.model.*;
import net.kaczmarzyk.spring.data.jpa.domain.DateBetween;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@SuppressWarnings("deprecation")
@RestController
public class ClientServerController {

	@Autowired
	private WebClient webClient;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping("/")
	public Map<String, String> setSecurityContextHolder(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient) {		
		String user = oauth2AuthorizedClient.getPrincipalName();
		
		String query = "SELECT authorities.authority FROM users, authorities WHERE users.username = authorities.username AND users.username = ?;";
		String role = (String) jdbcTemplate.queryForObject(query, new Object[] { user }, String.class);
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(role));

		Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);  
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		Map<String, String> json = new LinkedHashMap<>();
		json.put("user", user);
		json.put("role", role);
		
		return json;
	}
	
	
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////// TIME OFF ENDPOINTS
		@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		@GetMapping("/timeOffs/{id}")
		public Object getTimeOffById(
				@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
				@PathVariable(value = "id") Long id) {
			return this.webClient.get().uri("http://10.5.0.7:8090/timeOffs/{id}", id)
					.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
						if (response.statusCode().isError()) {
							return response.bodyToMono(GlobalExceptionHandler.class);
						} else {
							return response.bodyToMono(Object.class);
						}
					}).block();
		}
		
		/////////************************
		@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		@GetMapping("/timeOffs")
		public Object findTimesOffs(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
				@RequestParam(value = "isActive",required = false) Optional<Integer> isActive,
				@RequestParam(value = "timeUnit",required = false) Optional<TimeUnit> timeUnit,
				@RequestParam(value = "sort",required = false) Optional<?> sort,
				@RequestParam(value = "page",required = false) Optional<Integer> page,
				@RequestParam(value = "size",required = false) Optional<Integer> size,
				@And({
					@Spec(path = "timeUnit", params="timeUnit", spec = Equal.class),
					@Spec(path = "isActive", params="isActive", spec = Equal.class)})
		Specification<TimeOffRequest> spec, @SortDefault(sort = "timeOffRequestId", direction = Sort.Direction.ASC) 
	    Pageable pageable){
			return this.webClient.get().uri("http://10.5.0.7:8090/timeOffs", 
					uri -> uri
					.queryParamIfPresent("timeUnit", timeUnit)
					.queryParamIfPresent("isActive", isActive)
					.queryParamIfPresent("sort", sort)
					.queryParamIfPresent("page", page)
					.queryParamIfPresent("size", size)
					.build())
					.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
						if (response.statusCode().isError() || response == null) {
							return response.bodyToMono(GlobalExceptionHandler.class);
						} else {
							return response.bodyToMono(Object.class);
						}
					}).block();

		}
				
		/////////////////////////CREATE TIME OFF
		@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
		@PostMapping("/timeOffs")
		public Object createTimeOff(
				@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
				@RequestBody TimeOffRequestBody timeOffRequestBody) {

			return this.webClient.post().uri("http://10.5.0.7:8090/timeOffs").contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).body(Mono.just(timeOffRequestBody), TimeOffRequestBody.class)
					.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
						if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
							return response.bodyToMono(GlobalExceptionHandler.class);
						} else {
							return response.bodyToMono(Object.class);
						}
					}).block();
		}
		
		////////////UPDATE TIME OFF
		 @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
		 @PatchMapping("/timeOffs/{id}")
		 public Object updateAmount(
				@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
		@PathVariable(value = "id") Long timeOffId,
		    @RequestBody TimeOffPatchRequestBody timeOffPatchRequestBody) {
		
		return this.webClient.patch().uri("http://10.5.0.7:8090/timeOffs/{id}", timeOffId).contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON).body(Mono.just(timeOffPatchRequestBody), TimeOffPatchRequestBody.class)
					.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
						if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
							return response.bodyToMono(GlobalExceptionHandler.class);
						} else {
							return response.bodyToMono(Object.class);
						}
					}).block();
		}
		
		//////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////// TIME OFF REQUEST ENDPOINTS
		  
		 /////////************************
		 //////GET TIME OFF REQUESTS BY MANAGER
		@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		@RequestMapping(value="/managers/{managerID}/timeOffRequests", method = RequestMethod.GET)
			public Object findTimeOffRequestByManager (
					@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
					@PathVariable(value = "managerID") Long managerID,
					@RequestParam(value = "requestedAfter",required = false) Optional<String> requestedAfter,
					@RequestParam(value = "requestedBefore",required = false) Optional<String> requestedBefore,
					@RequestParam(value = "status",required = false) Optional<String> status,
					@RequestParam(value = "sort",required = false) Optional<?> sort,
					@RequestParam(value = "page",required = false) Optional<Integer> page,
					@RequestParam(value = "size",required = false) Optional<Integer> size,
					@And({
						@Spec(pathVars = "managerID", spec = Equal.class, path = "managerId"),
						@Spec(path = "status", params="status",spec = Equal.class),
						@Spec(
								path = "startDate",
								params = { "requestedAfter", "requestedBefore" },
								spec = DateBetween.class
								)
						})
					Specification<TimeOffRequest> spec,  Pageable pageable){
			
			return this.webClient
	                .get()
	                .uri(uriBuilder -> uriBuilder.scheme("http").host("10.5.0.7").port(8090)
	                        .path("/managers/{managerID}/timeOffRequests") //Base-path for invoking the 3rd party service.
	                        .queryParamIfPresent("requestedAfter", requestedAfter)
	                        .queryParamIfPresent("requestedBefore", requestedBefore)
	                        .queryParamIfPresent("status", status)
	                        .queryParamIfPresent("sort", sort)
	    					.queryParamIfPresent("page", page)
	    					.queryParamIfPresent("size", size)
	                        .build(managerID))
	                .attributes(oauth2AuthorizedClient(oauth2AuthorizedClient))
	                .accept(MediaType.APPLICATION_JSON).exchangeToMono(response -> {
						if (response.statusCode().isError()) {
							return response.bodyToMono(GlobalExceptionHandler.class);
						} else {
							return response.bodyToMono(Object.class);
						}
					}).block();
					
			}
		 
		 	///////////////////GET TIME OFF REQUEST BY EMPLOYEE AND MANAGER
		 	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		 	@RequestMapping(value="/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.GET)
			public Object findTimeOffRequestByManagerAndEmployeeAndTimeOffRequestId(
					@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
					@PathVariable(value = "managerID") Long managerID, 
					@PathVariable(value = "employeeID") Long employeeID, 
					@PathVariable(value = "timeOffRequestID") Long timeOffRequestID) {
				return this.webClient.get().uri("http://10.5.0.7:8090/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", managerID,employeeID,timeOffRequestID)
						.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
							if (response.statusCode().isError()) {
								return response.bodyToMono(GlobalExceptionHandler.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
			}
		 	
		 	/////////************************
		//////////////////DELETE PENDING TIME OFF REQUEST
		 	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		 	@RequestMapping(value="/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.DELETE)
			public Object deleteTimeOffRequestByEmployeeAndTimeOffRequestId( 
					@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
					@PathVariable(value = "employeeID") Long employeeID, 
					@PathVariable(value = "timeOffRequestID") Long timeOffRequestID) {
		 		return this.webClient.delete().uri("http://10.5.0.7:8090/employees/{employeeID}/timeOffRequests/{timeOffRequestID}",employeeID,timeOffRequestID)
						.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
							if (response.statusCode().isError()) {
								return response.bodyToMono(Object.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
					
			}
			
			//////////////////UPDATE STATUS OF TIME OFF REQUEST
		 	@PreAuthorize("hasAnyRole('ROLE_MANAGER')")
		 	@RequestMapping(value = "/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", method = RequestMethod.PATCH)
			public Object updateStatusFromRequestFromAManager(@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
					@PathVariable(value = "managerID") Long managerID, 
					@PathVariable(value = "timeOffRequestID") Long timeOffRequestID,  
					@PathVariable(value = "employeeID") Long employeeID,
					@RequestBody TimeOffRequestPatchRequestBody timeOffRequestPatchRequestBody){
		 		return this.webClient.patch().uri("http://10.5.0.7:8090/managers/{managerID}/employees/{employeeID}/timeOffRequests/{timeOffRequestID}", 
		 				managerID,employeeID,timeOffRequestID).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).body(Mono.just(timeOffRequestPatchRequestBody), TimeOffRequestPatchRequestBody.class)
						.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
							if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
								return response.bodyToMono(GlobalExceptionHandler.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
			}
		 	
		 	
		 	///////////////////CREATE TIME OFF REQUEST
		 	
		 	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		 	@RequestMapping(value = "/managers/{managerID}/employees/{employeeID}/timeOffRequests", method = RequestMethod.POST)
			public Object createTimeOffRequest(
					@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
					@RequestBody TimeOffRequestRequestBody timeOffRequestRequestBody,
					@PathVariable(value = "managerID") Long managerID, 
					@PathVariable(value = "employeeID") Long employeeID) throws Exception {
		 		return this.webClient.post().uri("http://10.5.0.7:8090/managers/{managerID}/employees/{employeeID}/timeOffRequests",managerID,employeeID).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON).body(Mono.just(timeOffRequestRequestBody), TimeOffRequestRequestBody.class)
						.attributes(oauth2AuthorizedClient(oauth2AuthorizedClient)).exchangeToMono(response -> {
							if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
								return response.bodyToMono(GlobalExceptionHandler.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
		 	}
		 	
		 	///////////////////GET AVAILABLE TIME FROM EMPLOYEE
		 	
		 	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		 	@RequestMapping(value = "/employees/{employeeID}/availableTimes", params = "hiringDate", method = RequestMethod.GET)
		 	public Object getAvailableTimeByEmployeeId(
		 			@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
		 			@PathVariable(value = "employeeID") Long employeeID,
		 			@RequestParam(value = "hiringDate") String hiringDate
					) {
		 		
		 		return this.webClient
		                .get()
		                .uri(uriBuilder -> uriBuilder.scheme("http").host("10.5.0.7").port(8090)
		                        .path("/employees/{employeeID}/availableTimes") //Base-path for invoking the 3rd party service.
		                        .queryParam("hiringDate", hiringDate)
		                        .build(employeeID))
		                .attributes(oauth2AuthorizedClient(oauth2AuthorizedClient))
		                .accept(MediaType.APPLICATION_JSON).exchangeToMono(response -> {
							if (response.statusCode().isError()) {
								return response.bodyToMono(GlobalExceptionHandler.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
		    }
		 	
		 	
		 	///////////////////GET AVAILABLE TIME FROM EMPLOYEE AND BY TIME OFF ID 
		 	
		 	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
		 	@RequestMapping(value = "/employees/{employeeID}/availableTimes", params = {"hiringDate", "timeOffID"}, method = RequestMethod.GET)
		 	public Object getAvailableTimeByEmployeeIdAndTimeOffId(
		 			@RegisteredOAuth2AuthorizedClient("itk-client-authorization-code") OAuth2AuthorizedClient oauth2AuthorizedClient,
		 			@PathVariable(value = "employeeID") Long employeeID,
		 			@RequestParam(value = "hiringDate") String hiringDate,
		 			@RequestParam(required = false) Optional<Long> timeOffID
					) {
		 		
		 		return this.webClient
		                .get()
		                .uri(uriBuilder -> uriBuilder.scheme("http").host("10.5.0.7").port(8090)
		                        .path("/employees/{employeeID}/availableTimes") //Base-path for invoking the 3rd party service.
		                        .queryParam("hiringDate", hiringDate)
		                        .queryParamIfPresent("timeOffID", timeOffID)
		                        .build(employeeID))
		                .attributes(oauth2AuthorizedClient(oauth2AuthorizedClient))
		                .accept(MediaType.APPLICATION_JSON).exchangeToMono(response -> {
							if (response.statusCode().isError()) {
								return response.bodyToMono(GlobalExceptionHandler.class);
							} else {
								return response.bodyToMono(Object.class);
							}
						}).block();
		    }
		 	
			
}
