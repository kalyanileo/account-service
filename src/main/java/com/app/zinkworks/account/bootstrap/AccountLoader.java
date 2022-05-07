package com.app.zinkworks.account.bootstrap;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.repository.AccountRepository;

@Component
public class AccountLoader implements CommandLineRunner {
	
	@Autowired
	private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        loadAccount();
    }

    /**
     * Loads the ATM on the startup
     */
    private void loadAccount() {
    		accountRepository.save(
            		Account.builder()
                    .accountNumber(123456789)
                    .type("Savings")
                    .pin(1234)
                    .balance(new BigDecimal(800))
                    .overdraft(new BigDecimal(200))
                    .build());

    		accountRepository.save(
            		Account.builder()
                    .accountNumber(987654321)
                    .type("Savings")
                    .pin(4321)
                    .balance(new BigDecimal(1230))
                    .overdraft(new BigDecimal(150))
                    .build());

            
        
    }

}
