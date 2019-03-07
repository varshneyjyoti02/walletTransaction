package com.capgemini.beans;

import java.math.BigDecimal;

public class Transaction {
	private String mobile;
	private BigDecimal wallet;

	public BigDecimal getWallet() {
		return wallet;
	}
	public void setWallet(BigDecimal wallet) {
		this.wallet = wallet;
	}
	public String getMobile() {
		return mobile;
	}
	@Override
	public String toString() {
		return "Transaction [mobile=" + mobile + ", wallet=" + wallet + "]";
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
