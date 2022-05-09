package com.app.zinkworks.account.model.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RollBackWithdrawRequest {
	
	@NonNull
	@JsonProperty("Amount")
    private BigDecimal amount;
	
	@NonNull
    @JsonProperty("AccountNumber")
    private int accountNumber;
	
	@NonNull
	@JsonProperty("Overdraft")
    private BigDecimal overdraft;


}

