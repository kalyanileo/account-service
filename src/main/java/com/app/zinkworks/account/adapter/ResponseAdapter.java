package com.app.zinkworks.account.adapter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.zinkworks.account.exception.AccountException;

@Service
public class ResponseAdapter {
	public <T> ResponseEntity<T> buildResponse(T response) {
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	public void throwException(HttpStatus status) throws AccountException {
		if (!HttpStatus.OK.equals(status))
			throw new AccountException("An unexpected internal server error occured");
	}

}
