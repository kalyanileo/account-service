package com.app.zinkworks.account.model.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@Getter
@Setter
public class BalanceResponse {
	
	@JsonProperty("AccountNumber")
	private Integer accountNumber;
	
	@JsonProperty("Balance")
	private BigDecimal balance;

	@JsonProperty("OverDraft")
	private BigDecimal overDraft;
	
	
}
