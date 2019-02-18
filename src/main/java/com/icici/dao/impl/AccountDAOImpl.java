package com.icici.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.icici.customexceptions.InsufficientBalanceException;
import com.icici.dao.AccountDAO;
import com.icici.mappers.AccountMapper;
import com.icici.model.Account;

@Repository
public class AccountDAOImpl implements AccountDAO {
	@Autowired
	JdbcTemplate jdbctemplate;

	@Override
	@Transactional
	public boolean fundTransfer(Account sbiAccount, Account iciciAccount, int amount) {
		// TODO Auto-generated method stub

		// Now we have data from both SBI & ICICI

		System.out.println("SBI ############" + sbiAccount);
		int debit = debit(sbiAccount, amount);
		System.out.println("debit is : " + debit);
		if (debit == 1) {
			String baseUrl = "http://localhost:8080/api/sbi/account/details/" + sbiAccount.getAccount_no();
			RestTemplate template = new RestTemplate();
			Account sbiaccount = template.getForObject(baseUrl, Account.class);
			System.out.println("Balance after update to SBI account is " + sbiaccount.getBalance());
			Account fetchICICIAccountDetails = fetchAccountDetails(iciciAccount);
			System.out.println("ICIC ###########" + fetchICICIAccountDetails);
			int credit = credit(iciciAccount, amount, fetchICICIAccountDetails.getBalance());
			if (credit == 1) {
				System.out.println("Balance after update to ICICI accound is " + fetchAccountDetails(iciciAccount));
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	private int credit(Account iciciAccount, int amount, int originalamt) {
		// TODO Auto-generated method stub
		String UPDATE_SQL = "UPDATE ICICI_ACCOUNT SET CUSTOMER_BALANCE=? WHERE CUSTOMER_ACCOUNT_NO=? ";
		return jdbctemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
			int getOriginalAmount = originalamt;

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, amount + getOriginalAmount);
				ps.setInt(2, iciciAccount.getAccount_no());
			}
		});

	}

	private int debit(Account sbiAccount, int amount) {
		// TODO Auto-generated method stub
		if (sbiAccount.getBalance() > amount) {
			int updated_amount = sbiAccount.getBalance() - amount;
			// String baseUrl = "http://localhost:8080/api/sbi/account/details/" + sbiAc;
			String baseUrl = "http://localhost:8080/api/sbi/account/details/" + updated_amount + "/"
					+ sbiAccount.getAccount_no();
			RestTemplate template = new RestTemplate();

			Integer forObject = template.getForObject(baseUrl, Integer.class);
			return forObject.intValue();

		} else {
			try {
				throw new InsufficientBalanceException("Insufficient Balance found in : " + sbiAccount.getAccount_no());
			} catch (InsufficientBalanceException e) {
				// TODO Auto-generated catch block
				return 0;
			}
		}

	}

	private Account fetchAccountDetails(Account account) {
		// TODO Auto-generated method stub
		try {
			String sql = "SELECT * FROM ICICI_ACCOUNT WHERE CUSTOMER_ACCOUNT_NO=" + account.getAccount_no() + "";
			Account iciciAccountObject = jdbctemplate.queryForObject(sql, new AccountMapper());
			System.out.println("data from ICICI db :" + iciciAccountObject);
			return iciciAccountObject;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error occured while fetched data from db, hence transaction rolled back");
		}
		return account;
	}

	@Override
	@Transactional
	public boolean fundsTransfer(Account fromICICIAccount, Account toSBIAccount, int amount) {
		// TODO Auto-generated method stub
		System.out.println("#########  ICIC ACCOUNT DATA BEFORE DEBIT IS " +  fetchAccountDetails(fromICICIAccount));
		int debitStatus = debitfromICICI(fromICICIAccount, amount);
		System.out.println("#########  ICIC ACCOUNT DATA AFTER DEBIT WAS " + fetchAccountDetails(fromICICIAccount));
		System.out.println("########  SBI ACCOUNT DATA BEFORE CREDIT IS " + toSBIAccount);
		int creditStatus = creditToSBI(toSBIAccount, amount);
		String baseUrl = "http://localhost:8080/api/sbi/account/details/" + toSBIAccount.getAccount_no();
		System.out.println(baseUrl);
		RestTemplate template = new RestTemplate();
		Account sbiaccount = template.getForObject(baseUrl, Account.class);
		System.out.println("########  SBI ACCOUNT DATA AFTER CREDIT WAS " + sbiaccount);
		if(debitStatus==1 && creditStatus ==1) {
					return true;
		}else {
			return false;
		}
	}

	private int creditToSBI(Account toSBIAccount, int amount) {
		// TODO Auto-generated method stub
							int updated_amount=toSBIAccount.getBalance()+amount;
							String baseUrl = "http://localhost:8080/api/sbi/account/details/transfer/" + updated_amount + "/"
									+ toSBIAccount.getAccount_no();
							RestTemplate template = new RestTemplate();

							Integer forObject = template.getForObject(baseUrl, Integer.class);
							return forObject.intValue();
							
	}

	private int debitfromICICI(Account fromICICIAccount, int amount) {
		// TODO Auto-generated method stub
		Account fetchAccountDetails = fetchAccountDetails(fromICICIAccount);
		String UPDATE_SQL = "UPDATE ICICI_ACCOUNT SET CUSTOMER_BALANCE=? WHERE CUSTOMER_ACCOUNT_NO=? ";
		return jdbctemplate.update(UPDATE_SQL, new PreparedStatementSetter() {
			int getOriginalAmount = fetchAccountDetails.getBalance();

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, getOriginalAmount-amount);
				ps.setInt(2, fetchAccountDetails.getAccount_no());
			}
		});

	}

	@Override
	public Account getAccountData(Account account) {
		// TODO Auto-generated method stub
		     return    fetchAccountDetails(account);
	}

}
