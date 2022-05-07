package com.app.zinkworks.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.zinkworks.account.exception.AccountException;

@SpringBootApplication
public class AccountApplication {

	public static void main(String[] args) throws AccountException{
		SpringApplication.run(AccountApplication.class, args);
	}

}
