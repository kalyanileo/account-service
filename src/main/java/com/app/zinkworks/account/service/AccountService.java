package com.app.zinkworks.account.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.zinkworks.account.adapter.*;
import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.request.AccountRequest;
import com.app.zinkworks.account.model.request.RollBackWithdrawRequest;
import com.app.zinkworks.account.model.request.WithdrawAmountRequest;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;
import com.app.zinkworks.account.repository.AccountRepository;

@Service
public class AccountService {
	
	@Autowired
	private ResponseAdapter responseAdapter;
	
	@Autowired
	private AccountRepository accountRepository;
	
	
	public ResponseEntity<BalanceResponse> getBalance(AccountRequest balanceRequest) throws AccountException{
		
		Account accountDetails = getAccountDetails(balanceRequest.getAccountNumber());
		if(!validatePin(accountDetails.getPin(),balanceRequest.getPin()))
			throw new AccountException("Invalid Pin");
	
		BalanceResponse balanceResponse = BalanceResponse.builder()
											.accountNumber(accountDetails.getAccountNumber())
											.balance(accountDetails.getBalance())
											.maxWithdrawalAmount(accountDetails.getBalance().add(accountDetails.getOverdraft()))
											.build();
		
		return responseAdapter.buildResponse(balanceResponse);		
	}
	
	private boolean validatePin(int accountPin, int pin) throws AccountException {
		boolean isValidPin = (accountPin == pin) ? true : false;		
		return isValidPin;
	}
	
	public ResponseEntity<WithdrawAmountResponse> withdrawAmount(WithdrawAmountRequest withdrawAmountRequest) throws AccountException{
		
		int accountNumber = withdrawAmountRequest.getAccountNumber();
		int pin = withdrawAmountRequest.getPin();
		BigDecimal amount = withdrawAmountRequest.getAmount();
		
		Account accountDetails = getAccountDetails(accountNumber);
		
		if(!validatePin(accountDetails.getPin(),pin))
			throw new AccountException("Invalid Pin");
		if(accountDetails.getBalance().add(accountDetails.getOverdraft()).compareTo(amount) < 0)
            throw new AccountException("Insufficient Balance");
		
		BigDecimal prevBalance = accountDetails.getBalance();
        BigDecimal newBalance = prevBalance;
        BigDecimal prevOverdraft = accountDetails.getOverdraft();
        BigDecimal newOverdraft = prevOverdraft;

        if(prevBalance.compareTo(amount) >= 0) 
            newBalance = prevBalance.subtract(amount);
        
        else {
            
        	BigDecimal excessAmount = amount.subtract(prevBalance);
        	newOverdraft = prevOverdraft.subtract(excessAmount);
            newBalance = prevBalance.add(excessAmount).subtract(amount);
           
        }
        
        accountDetails.setBalance(newBalance);
        accountDetails.setOverdraft(newOverdraft);
        accountDetails = accountRepository.save(accountDetails);
        
        WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
        												.accountNumber(accountNumber)
        												.prevBalance(prevBalance)
        												.prevOverdraft(prevOverdraft)
        												.newBalance(newBalance)
        												.newOverdraft(newOverdraft)
        												.withdrawAmount(amount)
        												.build();
      		
        return responseAdapter.buildResponse(withdrawAmountResponse);	
		
		
	}
	
	public ResponseEntity<BalanceResponse> rollbackWithdrawal(RollBackWithdrawRequest rollBackWithdrawRequest) throws AccountException{
		
		int accountNumber =  rollBackWithdrawRequest.getAccountNumber();		
		BigDecimal amount = rollBackWithdrawRequest.getAmount();
		BigDecimal overdraft = rollBackWithdrawRequest.getOverdraft();
				
		Account accountDetails = getAccountDetails(accountNumber);
      
        accountDetails.setBalance(amount);
        accountDetails.setOverdraft(overdraft);  
        accountDetails = accountRepository.save(accountDetails);
        
        BalanceResponse balanceResponse = BalanceResponse.builder()
        												.accountNumber(accountDetails.getAccountNumber())
        												.balance(accountDetails.getBalance())
        												.maxWithdrawalAmount(accountDetails.getBalance().add(accountDetails.getOverdraft()))
        												.build();
      		
        return responseAdapter.buildResponse(balanceResponse);	
		
		
	}

	
	private Account getAccountDetails(int accountNumber) throws AccountException {
		Optional<Account> accountDetails = accountRepository.findById(accountNumber);
		if(!accountDetails.isPresent())
			throw new AccountException(String.format("Account with number %s does not exist.", accountNumber));
		
		return accountDetails.get();
		
	}
}
