package com.blog.samples.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CreditCheckResult {

	private int customerId;
	private Integer creditScore;
	private Boolean eligbleForCredit;
	private String description;

	public CreditCheckResult() {
	}

	public CreditCheckResult(int customerId, Integer creditScore, Boolean eligbleForCredit, String description) {
		this.customerId = customerId;
		this.creditScore = creditScore;
		this.eligbleForCredit = eligbleForCredit;
		this.description = description;
	}

	@JsonProperty
	public int getCustomerId() {
		return customerId;
	}

	@JsonProperty
	public Integer getCreditScore() {
		return creditScore;
	}

	@JsonProperty
	public Boolean getEligbleForCredit() {
		return eligbleForCredit;
	}

	@JsonProperty
	public String getDescription() {
		return description;
	}

}