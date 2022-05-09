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
public class WithdrawAmountResponse {
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("PrevOverdraft")
    private BigDecimal prevOverdraft;
	
	@JsonProperty("NewOverdraft")
    private BigDecimal newOverdraft;
	
	@JsonProperty("PrevBalance")
    private BigDecimal prevBalance;
	
	@JsonProperty("NewBalance")
    private BigDecimal newBalance;
	
	@JsonProperty("WithdrawAmount")
    private BigDecimal withdrawAmount;


}
