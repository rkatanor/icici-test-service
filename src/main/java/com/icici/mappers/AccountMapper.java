package com.icici.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.icici.model.Account;

public class AccountMapper implements RowMapper<Account> {

	@Override
	public Account mapRow(ResultSet rs, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		Account account = new Account();
		account.setName(rs.getString(1));
		account.setEmail(rs.getString(2));
		account.setAccount_no(rs.getInt(3));
		account.setBalance(rs.getInt(4));
		account.setAccount_type(rs.getString(5));

		return account;
	}

}
