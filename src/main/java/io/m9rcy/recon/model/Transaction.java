package io.m9rcy.recon.model;

import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@ToString
public class Transaction {

	// 1 'D'
	private String type;
	// 1 'P' OR 'R'
	private String transactionType;
	// 15
	private String mobileNumber;
	// 11
	private BigDecimal amount;
	// 50
	private String paymentId;
	// 32
	private String merchantId;
	// 14 CCYYMMDDHHMMSS
	private Date creationDateTime;
	// 16 0212341234567123
	private String settlementAccount;
	// 2 '50' OR '00'
	private String transactionCode;
	// 13
	private String transactionId;
	// 16 0212341234567123
	private String localAccountNumber;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public Date getCreationDateTime() {
		return creationDateTime;
	}

	public void setCreationDateTime(Date creationDateTime) {
		this.creationDateTime = creationDateTime;
	}

	public String getSettlementAccount() {
		return settlementAccount;
	}

	public void setSettlementAccount(String settlementAccount) {
		this.settlementAccount = settlementAccount;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getLocalAccountNumber() {
		return localAccountNumber;
	}

	public void setLocalAccountNumber(String localAccountNumber) {
		this.localAccountNumber = localAccountNumber;
	}
}
