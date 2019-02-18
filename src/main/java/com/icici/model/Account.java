package com.icici.model;

public class Account {
	private String name;
	private int account_no;
	private int balance;
	private String email;
	private String account_type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAccount_no() {
		return account_no;
	}

	public void setAccount_no(int account_no) {
		this.account_no = account_no;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + ", account_no=" + account_no + ", balance=" + balance + ", email=" + email
				+ ", account_type=" + account_type + "]";
	}

}
