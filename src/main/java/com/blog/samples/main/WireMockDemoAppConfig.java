package com.blog.samples.main;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class WireMockDemoAppConfig  extends Configuration {
    
	@NotEmpty
    private String creditCheckUrl;

    @JsonProperty
    public String getCreditCheckUrl() {
        return creditCheckUrl;
    }

    @JsonProperty
    public void setCreditCheckUrl(String creditCheckUrl) {
        this.creditCheckUrl = creditCheckUrl;
    }
}