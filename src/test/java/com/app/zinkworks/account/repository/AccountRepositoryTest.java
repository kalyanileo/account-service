package com.app.zinkworks.account.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.repository.AccountRepository;

@DataJpaTest
public class AccountRepositoryTest {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	public void testNoAccounts() {
		List<Account> accounts = accountRepository.findAll();
		assertEquals(true,accounts.isEmpty());
	}
	
	@Test
	public void testFindAll() {
		accountRepository.save(
        		Account.builder()
                .accountNumber(123456789)
                .type("Savings")
                .pin(1234)
                .balance(new BigDecimal(800))
                .overdraft(new BigDecimal(200))
                .overdraftLimit(new BigDecimal(200))
                .build());

		accountRepository.save(
        		Account.builder()
                .accountNumber(987654321)
                .type("Savings")
                .pin(4321)
                .balance(new BigDecimal(1230))
                .overdraft(new BigDecimal(150))
                .overdraftLimit(new BigDecimal(150))
                .build());
		List<Account> accounts = accountRepository.findAll();
		assertEquals(2,accounts.size());
	}
	
	@Test
	public void testFindOne() {
		accountRepository.save(
        		Account.builder()
                .accountNumber(123456789)
                .type("Savings")
                .pin(1234)
                .balance(new BigDecimal(800))
                .overdraft(new BigDecimal(200))
                .overdraftLimit(new BigDecimal(200))
                .build());

		accountRepository.save(
        		Account.builder()
                .accountNumber(987654321)
                .type("Savings")
                .pin(4321)
                .balance(new BigDecimal(1230))
                .overdraft(new BigDecimal(150))
                .overdraftLimit(new BigDecimal(200))
                .build());
		Account account = accountRepository.findById(Integer.valueOf(123456789)).get();		
		assertEquals(123456789,account.getAccountNumber());
	}
}
	