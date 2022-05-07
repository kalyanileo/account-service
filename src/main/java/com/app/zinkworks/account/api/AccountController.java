package com.app.zinkworks.account.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.request.AccountRequest;
import com.app.zinkworks.account.model.request.WithdrawAmountRequest;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;
import com.app.zinkworks.account.service.AccountService;

@RestController
@RequestMapping("/v1")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping(value = "/account/balance", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BalanceResponse> getBalance(@RequestBody AccountRequest balanceRequest
	//public ResponseEntity<String> getBalance(@RequestBody Integer accountNumber
			)		throws AccountException {
		
		return accountService.getBalance(balanceRequest.getAccountNumber(),balanceRequest.getPin());
		//return new ResponseEntity<>("Successful",HttpStatus.OK);
		//return "Success";

	}
	
	@PostMapping(value = "/account/withdrawal", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<WithdrawAmountResponse> withdrawAmount(@RequestBody WithdrawAmountRequest withdrawAmountRequest	
			)		throws AccountException {
		
		return accountService.withdrawAmount(withdrawAmountRequest.getAccountNumber(),withdrawAmountRequest.getPin(),withdrawAmountRequest.getAmount());		

	}

	

}
