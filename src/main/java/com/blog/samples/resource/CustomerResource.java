package com.blog.samples.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;

import com.blog.samples.exception.CreditCheckFailedException;
import com.blog.samples.model.CreditCheckResult;
import com.blog.samples.model.Customer;

@Path("/customer")
public class CustomerResource {

	private String creditCheckServerUrl;
	private Client client;
	
    public CustomerResource(String creditCheckServerUrl) {
    	
    	this.creditCheckServerUrl = creditCheckServerUrl;	
    	client = ClientBuilder.newClient();
    	client.property(ClientProperties.CONNECT_TIMEOUT, 1000);
        client.property(ClientProperties.READ_TIMEOUT,    3000);
    }
    
    @POST
    @Path("/creditcheck")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response performCustomerCreditCheck(Customer customer) throws Exception {
    	
    	WebTarget webTarget = client.target(creditCheckServerUrl + "/creditcheck");    	 
    	Response response = webTarget.request().post(Entity.entity(customer, MediaType.APPLICATION_JSON));
    	
    	if(response.getStatus() == 200){
    		return Response.ok(response.readEntity(CreditCheckResult.class), MediaType.APPLICATION_JSON).build();    		
    	}
    	
    	throw new CreditCheckFailedException("Error occurred calling Check Service");
    }
}