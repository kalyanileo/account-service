package com.app.zinkworks.account.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

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
import com.app.zinkworks.account.dao.AccountDao;
import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;
import com.app.zinkworks.account.repository.AccountRepository;
import com.app.zinkworks.account.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	
	@InjectMocks
	AccountService accountService;
	
	@Mock
	private AccountDao accountDao;
	
	@Mock
	private ResponseAdapter responseAdapter;	
	
	
	@Test
	public void checkBalanceValidAccount() throws AccountException {
		
		Account account = new Account();
		account.setAccountNumber(123456789);
		account.setBalance(new BigDecimal(800));
		account.setOverdraft(new BigDecimal(200));
		account.setPin(1234);		
		when(accountDao.getAccountDetails(123456789)).thenReturn(account);
		
		BalanceResponse balanceResponse = new BalanceResponse();
		balanceResponse.setAccountNumber(123456789);
		balanceResponse.setBalance(new BigDecimal(800));
		balanceResponse.setOverDraft(new BigDecimal(200));
		
		when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));
		ResponseEntity<BalanceResponse> response = accountService.getBalance(123456789, 1234);	
				
		assertEquals(new BigDecimal(800),response.getBody().getBalance());
		
		
		
	}
	
	@Test
	public void checkBalanceInValidPin() throws AccountException {
		
		when(accountDao.getAccountDetails(123456789)).thenReturn(new Account(123456789,"Savings",new BigDecimal(800),new BigDecimal(200)
				,"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now())));
		
		try {
			ResponseEntity<BalanceResponse> balanceResponse = accountService.getBalance(123456789, 5678);
		} catch (AccountException ex) {			
			assertEquals("Invalid Pin",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountInValidPin() throws AccountException {
		
		when(accountDao.getAccountDetails(123456789)).thenReturn(new Account(123456789,"Savings",new BigDecimal(800),new BigDecimal(200)
				,"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now())));
		
		try {
			ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(123456789, 5678, new BigDecimal(200));
		} catch (AccountException ex) {			
			assertEquals("Invalid Pin",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountInSufficientBalance() throws AccountException {
		
		when(accountDao.getAccountDetails(123456789)).thenReturn(new Account(123456789,"Savings",new BigDecimal(800),new BigDecimal(200)
				,"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now())));
		
		try {
			ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(123456789, 1234, new BigDecimal(2000));
		} catch (AccountException ex) {			
			assertEquals("Insufficient Balance",ex.getMessage());
		}
		
	}
	
	@Test
	public void withdrawAmountValidPin() throws AccountException {
		
		when(accountDao.getAccountDetails(123456789)).thenReturn(new Account(123456789,"Savings",new BigDecimal(800),new BigDecimal(200)
				,"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now())));
	
        WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse();
        withdrawAmountResponse.setAccountNumber(123456789);
        withdrawAmountResponse.setNewBalance(new BigDecimal(600));
        withdrawAmountResponse.setNewOverdraft(new BigDecimal(200));
        
        when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));
		ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(123456789, 1234, new BigDecimal(200));
		assertEquals(new BigDecimal(600),response.getBody().getNewBalance());
		assertEquals(new BigDecimal(200),response.getBody().getNewOverdraft());
		
	}
	
	@Test
	public void withdrawAmountOverDraft() throws AccountException {
		
		when(accountDao.getAccountDetails(123456789)).thenReturn(new Account(123456789,"Savings",new BigDecimal(800),new BigDecimal(200)
				,"Active",1234,Timestamp.from(Instant.now()),Timestamp.from(Instant.now())));
	
        WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse();
        withdrawAmountResponse.setAccountNumber(123456789);
        withdrawAmountResponse.setNewBalance(new BigDecimal(0));
        withdrawAmountResponse.setNewOverdraft(new BigDecimal(100));
        
        when(responseAdapter.buildResponse(any())).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));
		ResponseEntity<WithdrawAmountResponse> response = accountService.withdrawAmount(123456789, 1234, new BigDecimal(900));
		assertEquals(new BigDecimal(0),response.getBody().getNewBalance());
		assertEquals(new BigDecimal(100),response.getBody().getNewOverdraft());
		
	}

}

