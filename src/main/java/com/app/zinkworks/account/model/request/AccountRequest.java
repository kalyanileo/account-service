package com.app.zinkworks.account.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountRequest {
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("Pin")
	private int pin;

}
