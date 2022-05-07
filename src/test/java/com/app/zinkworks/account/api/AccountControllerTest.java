package com.app.zinkworks.account.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.app.zinkworks.account.adapter.ResponseAdapter;
import com.app.zinkworks.account.api.AccountController;
import com.app.zinkworks.account.model.request.AccountRequest;
import com.app.zinkworks.account.model.request.WithdrawAmountRequest;
import com.app.zinkworks.account.model.response.BalanceResponse;
import com.app.zinkworks.account.model.response.WithdrawAmountResponse;
import com.app.zinkworks.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AccountService accountService;
	
	@Mock
	private ResponseAdapter responseAdapter;
	
	@Test
	public void checkGetBalanceSuccess() throws Exception {
		
		AccountRequest accountRequest = new AccountRequest();
		accountRequest.setAccountNumber(123456789);
		accountRequest.setPin(1234);
		
		ObjectMapper mapper = new ObjectMapper();  
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/v1/account/balance")	
				.content(mapper.writeValueAsString(accountRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		BalanceResponse balanceResponse = new BalanceResponse();
		balanceResponse.setAccountNumber(123456789);
		balanceResponse.setBalance(new BigDecimal(800));
		balanceResponse.setOverDraft(new BigDecimal(200));
		
		when(accountService.getBalance(123456789,1234)).thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));		
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())				
				.andReturn();
		
		assertEquals(mapper.writeValueAsString(balanceResponse), result.getResponse().getContentAsString());
		
	}
	
	@Test
	public void checkWithdrawAmount() throws Exception {
		
		WithdrawAmountRequest withdrawAmountRequest = new WithdrawAmountRequest();
		withdrawAmountRequest.setAccountNumber(123456789);
		withdrawAmountRequest.setAmount(new BigDecimal(200));		
		withdrawAmountRequest.setPin(1234);
		
		ObjectMapper mapper = new ObjectMapper();  
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/v1//account/withdrawal")	
				.content(mapper.writeValueAsString(withdrawAmountRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		WithdrawAmountResponse withdrawAmountResponse = new WithdrawAmountResponse();
		withdrawAmountResponse.setAccountNumber(123456789);
		withdrawAmountResponse.setNewBalance(new BigDecimal(600));
		withdrawAmountResponse.setNewOverdraft(new BigDecimal(200));
		withdrawAmountResponse.setPrevBalance(new BigDecimal(800));
		withdrawAmountResponse.setPrevOverdraft(new BigDecimal(200));
		
		when(accountService.withdrawAmount(123456789,1234,new BigDecimal(200))).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));		
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())				
				.andReturn();
		
		assertEquals(mapper.writeValueAsString(withdrawAmountResponse), result.getResponse().getContentAsString());
		
	}

}
