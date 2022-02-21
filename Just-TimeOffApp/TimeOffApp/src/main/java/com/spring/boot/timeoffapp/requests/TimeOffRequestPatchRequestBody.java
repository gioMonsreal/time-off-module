package com.spring.boot.timeoffapp.requests;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

import com.spring.boot.timeoffapp.enums.Status;

public class TimeOffRequestPatchRequestBody {
	
	@NotNull(message = "status can't be empty, accepted values are: [PENDING,APPROVED,DENNIED]")
	@Enumerated(EnumType.STRING)
	private Status status;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
