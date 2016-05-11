package com.blog.samples.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CreditCheckFailedException extends WebApplicationException {
    
	private static final long serialVersionUID = 1L;

	public CreditCheckFailedException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).
        		       entity(message).
        		       type(MediaType.APPLICATION_JSON).build());
    }
}