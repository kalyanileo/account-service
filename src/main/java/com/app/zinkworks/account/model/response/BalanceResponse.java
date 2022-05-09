package com.app.zinkworks.account.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class BalanceResponse {
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("Balance")
	private BigDecimal balance;

	@JsonProperty("MaxWithdrawalAmount")
	private BigDecimal maxWithdrawalAmount;
	
	
}
