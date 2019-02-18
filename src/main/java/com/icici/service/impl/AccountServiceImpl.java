package com.icici.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dao.AccountDAO;
import com.icici.model.Account;
import com.icici.service.AccountService;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountDAO dao;
	@Override
	
	public boolean moneyTransferToICICI(Account sbiAccount, Account iciciAccount, int amount) {
		// TODO Auto-generated method stub
		return dao.fundTransfer(sbiAccount, iciciAccount, amount);
	}
	@Override
	public boolean moneyTransfer(Account fromICICIAccount, Account toSBIAccount, int amount) {
		// TODO Auto-generated method stub
		return dao.fundsTransfer(fromICICIAccount, toSBIAccount, amount);
	}
	@Override
	public Account getICICIAccountDetails(Account account) {
		// TODO Auto-generated method stub
		return dao.getAccountData(account);
	}


}
