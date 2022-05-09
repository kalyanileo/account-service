/*package com.app.zinkworks.account.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.zinkworks.account.dao.AccountDao;
import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.repository.AccountRepository;
import com.app.zinkworks.account.service.AccountService;

@ExtendWith(MockitoExtension.class)
public class AccountDaoTest {
	
	@InjectMocks
	AccountDao atmDao;
	
	@Mock
	private AccountRepository accountRepository;
	
	@Test
	public void checkValidAccount() throws AccountException {
		
		Account account = Account.builder()
						  .accountNumber(123456789)
						  .balance(new BigDecimal(800))
						  .overdraft(new BigDecimal(200))
						  .overdraftLimit(new BigDecimal(200))
						  .pin(1234)
						  .build();
		
		when(accountRepository.findById(123456789)).thenReturn(Optional.of(account));
	
		Account response = atmDao.getAccountDetails(123456789);	
				
		assertEquals(123456789,response.getAccountNumber());	
		
	}
	
	@Test
	public void checkInvalidAccount() throws AccountException {
		
		Optional<Account> account = Optional.empty();	
		
		when(accountRepository.findById(12345678)).thenReturn(account);		
		
		try {
			Account response = atmDao.getAccountDetails(12345678);			
		} catch (AccountException ex) {			
			assertEquals("Account with number 12345678 does not exist.",ex.getMessage());
		}		
		
		
	}
	
	@Test
	public void checkUpdateAccountSuccess() throws AccountException {
		
		Account account = Account.builder()
						  .accountNumber(123456789)
						  .balance(new BigDecimal(800))
						  .overdraft(new BigDecimal(200))
						  .overdraftLimit(new BigDecimal(200))
						  .pin(1234)
						  .build();
		
		when(accountRepository.save(account)).thenReturn(account);		
		
		Account response = atmDao.updateAccount(account);			
		assertEquals(123456789,response.getAccountNumber());		
		
	}
}
*/