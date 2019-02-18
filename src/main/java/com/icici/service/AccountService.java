package com.icici.service;

import com.icici.model.Account;

public interface AccountService {
	public abstract boolean moneyTransferToICICI(Account sbiAccount, Account iciciAccount,int amount);
	public abstract boolean moneyTransfer(Account fromICICIAccount, Account toSBIAccount,int amount);
	public abstract Account getICICIAccountDetails(Account account);
	
}
