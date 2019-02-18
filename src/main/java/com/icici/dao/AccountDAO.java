package com.icici.dao;

import com.icici.model.Account;

public interface AccountDAO {
		public abstract boolean fundTransfer(Account sbiAccount, Account iciciAccount,int amount);
		public abstract boolean fundsTransfer(Account fromICICIAccount, Account toSBIAccount,int amount);
		public abstract Account getAccountData(Account account);
}
