package com.app.zinkworks.account.model.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WithdrawAmountRequest {	
	
	
	@JsonProperty("Amount")
	private BigDecimal amount;
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("Pin")
	private int pin;
	
}
