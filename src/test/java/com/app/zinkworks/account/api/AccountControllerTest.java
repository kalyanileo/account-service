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
		
		AccountRequest accountRequest = AccountRequest.builder()
										.accountNumber(123456789)
										.pin(1234)
										.build();
		
		ObjectMapper mapper = new ObjectMapper();  
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/v1/account/balance")	
				.content(mapper.writeValueAsString(accountRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		BalanceResponse balanceResponse = BalanceResponse.builder()
										  .accountNumber(123456789)
										  .balance(new BigDecimal(800))
										  .maxWithdrawalAmount(new BigDecimal(1000))
										  .build();
		
		when(accountService.getBalance(accountRequest)).thenReturn(new ResponseEntity<>(balanceResponse, HttpStatus.OK));		
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())				
				.andReturn();
		
		assertEquals(mapper.writeValueAsString(balanceResponse), result.getResponse().getContentAsString());
		
	}
	
	@Test
	public void checkWithdrawAmount() throws Exception {
		
		WithdrawAmountRequest withdrawAmountRequest = WithdrawAmountRequest.builder()
													  .accountNumber(123456789)
													  .amount(new BigDecimal(200))
													  .pin(1234)
													  .build();
		
		ObjectMapper mapper = new ObjectMapper();  
		
		RequestBuilder request = MockMvcRequestBuilders
				.post("/v1//account/withdrawal")	
				.content(mapper.writeValueAsString(withdrawAmountRequest))
				.contentType(MediaType.APPLICATION_JSON_VALUE);
		
		WithdrawAmountResponse withdrawAmountResponse = WithdrawAmountResponse.builder()
														.accountNumber(123456789)
														.newBalance(new BigDecimal(600))
														.newOverdraft(new BigDecimal(200))
														.prevBalance(new BigDecimal(800))
														.prevOverdraft(new BigDecimal(200))
														.build();
		
		when(accountService.withdrawAmount(withdrawAmountRequest)).thenReturn(new ResponseEntity<>(withdrawAmountResponse, HttpStatus.OK));		
		
		MvcResult result = mockMvc.perform(request)
				.andExpect(status().isOk())				
				.andReturn();
		
		assertEquals(mapper.writeValueAsString(withdrawAmountResponse), result.getResponse().getContentAsString());
		
	}

}
