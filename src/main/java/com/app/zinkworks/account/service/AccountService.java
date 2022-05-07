package com.app.zinkworks.account.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.app.zinkworks.account.adapter.*;
import com.app.zinkworks.account.dao.AccountDao;
import com.app.zinkworks.account.exception.AccountException;
import com.app.zinkworks.account.model.Account;
import com.app.zinkworks.account.model.request.WithdrawAmountRequest;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;

@Service
public class AccountService {
	
	@Autowired
	private ResponseAdapter responseAdapter;
	
	@Autowired
	private AccountDao accountDao;
	
	
	public ResponseEntity<BalanceResponse> getBalance(int accountNumber, int pin) throws AccountException{
		Account accountDetails = accountDao.getAccountDetails(accountNumber);			
		if(!validatePin(accountDetails.getPin(),pin))
			throw new AccountException("Invalid Pin");
		BalanceResponse balanceResponse = new BalanceResponse();
		balanceResponse.setAccountNumber(accountNumber);
		balanceResponse.setBalance(accountDetails.getBalance());
		balanceResponse.setOverDraft(accountDetails.getOverdraft());
		return responseAdapter.buildResponse(balanceResponse);		
	}
	
	private boolean validatePin(int accountPin, int pin) throws AccountException {
		boolean isValidPin = (accountPin == pin) ? true : false;		
		return isValidPin;
	}
	
	public ResponseEntity<WithdrawAmountResponse> withdrawAmount(int accountNumber, int pin, BigDecimal amount) throws AccountException{
		Account accountDetails = accountDao.getAccountDetails(accountNumber);		
		if(!validatePin(accountDetails.getPin(),pin))
			throw new AccountException("Invalid Pin");
		if(accountDetails.getBalance().add(accountDetails.getOverdraft()).compareTo(amount) < 0)
            throw new AccountException("Insufficient Balance");
		
		BigDecimal prevBalance = accountDetails.getBalance();
        BigDecimal newBalance = prevBalance;
        BigDecimal prevOverdraftBalance = accountDetails.getOverdraft();
        BigDecimal newOverdraftBalance = prevOverdraftBalance;

        if(prevBalance.compareTo(amount) >= 0) {
            newBalance = prevBalance.subtract(amount);
        } else {
            BigDecimal excessAmount = amount.subtract(prevBalance);
            //if(newOverdraftBalance.compareTo(excessAmount) >= 0){
                newOverdraftBalance = prevOverdraftBalance.subtract(excessAmount);
                newBalance = prevBalance.add(excessAmount).subtract(amount);

            //}
        }
        accountDetails.setBalance(newBalance);
        accountDetails.setOverdraft(newOverdraftBalance);
        accountDetails = accountDao.updateAccount(accountDetails);
        
        WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse();
        withdrawAmountResponse.setAccountNumber(accountNumber);
        withdrawAmountResponse.setPrevBalance(prevBalance);
        withdrawAmountResponse.setNewBalance(newBalance);
        withdrawAmountResponse.setPrevOverdraft(prevOverdraftBalance);
        withdrawAmountResponse.setNewOverdraft(newOverdraftBalance);
		
        return responseAdapter.buildResponse(withdrawAmountResponse);	
		
		
	}
}
