package com.app.zinkworks.account.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Account {
	
	@JsonProperty("AccountNumber")
	@Id
	@Column
	private int accountNumber;
	
	@JsonProperty("Type")
	@Column
	private String type;
	
	@JsonProperty("Balance")
	@Column
	private BigDecimal balance;
	
	@JsonProperty("Overdraft")
	@Column
    private BigDecimal overdraft;
	
	@JsonProperty("OverdraftLimit")
	@Column
    private BigDecimal overdraftLimit;
	
	@JsonProperty("Status")
	@Column
	private String status;
	
	@JsonProperty("Pin")
	@Column
	private int pin;
	
	@JsonProperty("CreateDate")
	@CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;
	
	@JsonProperty("UpdateDate")
    @UpdateTimestamp
    @Column
    private Timestamp updateDate;
	
}
