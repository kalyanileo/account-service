package com.app.zinkworks.account.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.zinkworks.account.adapter.ResponseAdapter;
import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.request.AccountRequest;
import com.app.zinkworks.account.model.request.WithdrawAmountRequest;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;
import com.app.zinkworks.account.repository.AccountRepository;
import com.app.zinkworks.account.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	
	@InjectMocks
	AccountService accountService;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Mock
	private ResponseAdapter responseAdapter;	
	
	
	@Test
	public void checkBalanceValidAccount() throws AccountException {
		
		Account account = Account.builder()
						  .accountNumber(123456789)
						  .balance(new BigDecimal(800))
						  .overdraft(new BigDecimal(200))
						  .overdraftLimit(new BigDecimal(200))
						  .pin(1234)
						  .build();					  
	
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(account));
		
		AccountRequest accountRequest = AccountRequest.builder()
										.accountNumber(123456789)
										.pin(1234)
										.build();
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
										  .accountNumber(123456789)
										  .balance(new BigDecimal(800))
										  .maxWithdrawalAmount(new BigDecimal(1000))
										  .build();
		
		when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));
		ResponseEntity<BalanceResponse> response = accountService.getBalance(accountRequest);	
				
		assertEquals(new BigDecimal(800),response.getBody().getBalance());
		
		
		
	}
	
	@Test
	public void checkBalanceInvalidPin() throws AccountException {
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(new Account(123456789,"Savings",new BigDecimal(800),
				new BigDecimal(200) ,new BigDecimal(200),"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()))));
		
		AccountRequest accountRequest = AccountRequest.builder()
										.accountNumber(123456789)
										.pin(12345)
										.build();
		try {
			ResponseEntity<BalanceResponse> balanceResponse = accountService.getBalance(accountRequest);
		} catch (AccountException ex) {			
			assertEquals("Invalid Pin",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountInvalidPin() throws AccountException {
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(new Account(123456789,"Savings",new BigDecimal(800),
				new BigDecimal(200), new BigDecimal(200),"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()))));
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
													  .accountNumber(123456789)
													  .amount(new BigDecimal(200))
													  .pin(5678)
													  .build();
		try {
			ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(withdrawAmountRequest);
		} catch (AccountException ex) {			
			assertEquals("Invalid Pin",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountInSufficientBalance() throws AccountException {
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(new Account(123456789,"Savings",new BigDecimal(800),
				new BigDecimal(200),new BigDecimal(200),"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()))));
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
				  .accountNumber(123456789)
				  .amount(new BigDecimal(2000))
				  .pin(1234)
				  .build();
		
		try {
			ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(withdrawAmountRequest);
		} catch (AccountException ex) {			
			assertEquals("Insufficient Balance",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountValidPin() throws AccountException {
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(new Account(123456789,"Savings",new BigDecimal(800),
				new BigDecimal(200),new BigDecimal(200),"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()))));
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
				  .accountNumber(123456789)
				  .amount(new BigDecimal(200))
				  .pin(1234)
				  .build();
				  
        WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
        												.accountNumber(123456789)
        												.newBalance(new BigDecimal(600))
        												.newOverdraft(new BigDecimal(200))
        												.build();
                
        when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));
		ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(withdrawAmountRequest);
		assertEquals(new BigDecimal(600),response.getBody().getNewBalance());
		assertEquals(new BigDecimal(200),response.getBody().getNewOverdraft());
		
	}
	
	@Test
	public void withdrawAmountOverDraft() throws AccountException {
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(new Account(123456789,"Savings",new BigDecimal(800),
				new BigDecimal(200),new BigDecimal(200),"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now()))));
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
				  .accountNumber(123456789)
				  .amount(new BigDecimal(900))
				  .pin(1234)
				  .build();
		
        WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse();
        withdrawAmountResponse.setAccountNumber(123456789);
        withdrawAmountResponse.setNewBalance(new BigDecimal(0));
        withdrawAmountResponse.setNewOverdraft(new BigDecimal(100));
        
        when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));
		ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(withdrawAmountRequest);
		assertEquals(new BigDecimal(0),response.getBody().getNewBalance());
		assertEquals(new BigDecimal(100),response.getBody().getNewOverdraft());
		
	}

}

