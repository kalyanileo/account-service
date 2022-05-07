package com.app.zinkworks.account.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.repository.AccountRepository;

@Service
public class AccountDao {
	
	@Autowired
	private AccountRepository accountRepository;
	

	public Account getAccountDetails(Integer accountNumber) throws AccountException {
		Optional<Account> accountDetails = accountRepository.findById(accountNumber);
		if(!accountDetails.isPresent())
			throw new AccountException(String.format("Account with number %s does not exist.", accountNumber));
		
		return accountDetails.get();
		
	}
	
	/*public Integer getPin(Integer accountNumber) { 
		return accountRepository.findById(accountNumber).get().getPin();
		
	}*/
	
	public Account updateAccount(Account account) throws AccountException {
		Account accountDetails = accountRepository.save(account);		
		
		return accountDetails;
		
	}
	
	/*public Card getCardDetails(Integer cardNumber) { 
		Card card = new Card();
		card.setCardNumber(cardNumber);
		return cardRepository.findById(cardNumber).get();
		
	}*/
	
	/*public List<AtmBalanceDetails> getAtmBalanceDetails(Integer atmId) {
		return atmBalanceDetailsRepository.
				findById(atmId).get();
	}*/
}
