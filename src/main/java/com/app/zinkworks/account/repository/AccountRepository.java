package com.app.zinkworks.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.app.zinkworks.account.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>{

}
