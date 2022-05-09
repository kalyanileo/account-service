package com.app.zinkworks.account.model.request;

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
public class AccountRequest {
	
	@JsonProperty("AccountNumber")
	private int accountNumber;
	
	@JsonProperty("Pin")
	private int pin;

}
