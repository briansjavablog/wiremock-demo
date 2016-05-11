package com.blog.samples.main;

import com.blog.samples.exception.ApplicationExceptionMapper;
import com.blog.samples.exception.CreditCheckFailedException;
import com.blog.samples.resource.CustomerResource;

import io.dropwizard.Application;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;

public class WireMockDemoApp extends Application<WireMockDemoAppConfig> {

    public static void main(String[] args) throws Exception {
        new WireMockDemoApp().run(args);
    }

    @Override
    public void run(WireMockDemoAppConfig config, Environment environment) {
        
    	((DefaultServerFactory)config.getServerFactory()).setRegisterDefaultExceptionMappers(false);    	   
    	environment.jersey().register(new CustomerResource(config.getCreditCheckUrl()));        
        environment.jersey().register(new ApplicationExceptionMapper());
    }
}