package com.blog.samples.exception;

import java.net.SocketTimeoutException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<Throwable>
{  
    @Override
    public Response toResponse(Throwable exception)
    {    	    	
    	if(exception.getCause() instanceof SocketTimeoutException){
    		return Response.status(Response.Status.SERVICE_UNAVAILABLE).
			        entity("Credit Check Service Timed Out").
			        type(MediaType.APPLICATION_JSON).build();
    	}
    	else{
    		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
			        entity("Error occurred calling Check Service").
			        type(MediaType.APPLICATION_JSON).build();	    	
    	}    		
    }
}