package com.app.zinkworks.account.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountException extends Exception {
	
	public static final String DEFAULT_ERROR_CODE = "-1";
	
	private final String errorCode;
	
	public AccountException() {
		errorCode = DEFAULT_ERROR_CODE;		
	}
	
	public AccountException(String message) {
		super(message);		
		errorCode = DEFAULT_ERROR_CODE;
	}

}
