package com.app.zinkworks.account.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.zinkworks.account.exception.AccountException;

import lombok.Data;

@RestControllerAdvice
public class ExceptionHandlerAdvice {
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AccountException.class)
	@ResponseBody
	public ErrorResponse handle(final AccountException ex) {
		return new ErrorResponse(ex.getErrorCode(),ex.getMessage());
	}
	
	@Data
	public static class ErrorResponse {
		private final String code;
		private final String message;
	}

}
