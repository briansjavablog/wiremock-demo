package com.blog.samples.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.blog.samples.main.WireMockDemoApp;
import com.blog.samples.main.WireMockDemoAppConfig;
import com.blog.samples.model.CreditCheckResult;
import com.blog.samples.model.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;


public class CustomerResourceTest {

	@ClassRule    
	public static final DropwizardAppRule<WireMockDemoAppConfig> RULE = 
		                   new DropwizardAppRule<WireMockDemoAppConfig>(WireMockDemoApp.class, 
								                   ResourceHelpers.resourceFilePath("config.yml"));
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8090);
	
	private Client client = ClientBuilder.newClient();;    	
	private ObjectMapper mapper = new ObjectMapper();

	
    @Test
    public void testCustomerCreditCheckSuccessResponse() throws Exception {

    	stubFor(post(urlEqualTo("/creditcheck"))
                .withHeader("Content-Type", WireMock.equalTo("application/json"))
                .withRequestBody(WireMock.equalTo(getCustomerJson()))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(getCreditCheckJson())));
    	   	
    	WebTarget webTarget = client.target("http://localhost:8080/customer/creditcheck");
    	Response response = webTarget.request(MediaType.APPLICATION_JSON).
    								  post(Entity.entity(getCustomer(), MediaType.APPLICATION_JSON));
    	
    	assertThat(response.getStatus(), equalTo(200));    	
    	assertThat(response.readEntity(CreditCheckResult.class), equalTo(getCreditCheckResult()));
    }
    
    @Test
    public void testCustomerCreditCheckErrorResponse() throws Exception {

    	stubFor(post(urlEqualTo("/creditcheck"))
                .withHeader("Content-Type", WireMock.equalTo("application/json"))
                .withRequestBody(WireMock.equalTo(getCustomerJson()))
                .willReturn(aResponse()
                    .withStatus(503)
                    .withHeader("Content-Type", "application/json")));
                    
    	WebTarget webTarget = client.target("http://localhost:8080/customer/creditcheck");
    	Response response = webTarget.request(MediaType.APPLICATION_JSON).
    								  post(Entity.entity(getCustomer(), MediaType.APPLICATION_JSON));
    	
    	assertThat(response.getStatus(), equalTo(500));    	
    	assertThat(response.readEntity(String.class), equalTo("Error occurred calling Check Service"));
    }
    
    @Test
    public void testCustomerCreditCheckServiceTimeout() throws Exception {

    	int creditCheckServiceDelayMillis = 6000;
    	
    	stubFor(post(urlEqualTo("/creditcheck"))
                .withHeader("Content-Type", WireMock.equalTo("application/json"))
                .withRequestBody(WireMock.equalTo(getCustomerJson()))
                .willReturn(WireMock.aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")                    
                    .withBody(getCreditCheckJson())
                    .withFixedDelay(creditCheckServiceDelayMillis)));
    	
    	WebTarget webTarget = client.target("http://localhost:8080/customer/creditcheck");
    	
    	long startMillis = DateTime.now().getMillis();
    	Response response = webTarget.request(MediaType.APPLICATION_JSON).
    								  post(Entity.entity(getCustomer(), MediaType.APPLICATION_JSON));
    	long endMillis = DateTime.now().getMillis();
    	
    	assertThat((int)(endMillis - startMillis), is(lessThan(creditCheckServiceDelayMillis)));    	
    	assertThat(response.getStatus(), equalTo(503));
    	assertThat(response.readEntity(String.class), equalTo("Credit Check Service Timed Out"));
    }
    
    private Customer getCustomer() throws JsonProcessingException {
    	return new Customer(1, "Steve", "Jones", "stevejones@gmail.com");    	    	    		
    }
    
    private String getCustomerJson() throws JsonProcessingException {    	
    	return mapper.writeValueAsString(getCustomer());    	
    }
    
    private CreditCheckResult getCreditCheckResult(){	
    	return new CreditCheckResult(1, 1234, false, "Outstanding CCJs");
    }
    
    private String getCreditCheckJson() throws JsonProcessingException {    	
    	return mapper.writeValueAsString(getCreditCheckResult());	
    }
}