package com.icici;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import com.icici.mappers.AccountMapper;
import com.icici.model.Account;

@Component
public class Connection {
	@Autowired
	JdbcTemplate jdbctemplate;
	
	public java.sql.Connection  getConnection() throws SQLException {
		  return    jdbctemplate.getDataSource().getConnection();
		  
	}
	
	public void createTable() {
					String sql="CREATE TABLE ICICI_ACCOUNT (CUSTOMER_NAME  VARCHAR(20),  CUSTOMER_EMAIL VARCHAR(50),  CUSTOMER_ACCOUNT_NO INTEGER, CUSTOMER_BALANCE INTEGER , CUSTOMER_ACCOUNT_TYPE VARCHAR(20))";
					jdbctemplate.execute(sql);
					System.out.println("table created");
	}
	public int insertData() {
				String sql="INSERT INTO ICICI_ACCOUNT VALUES ('Lilly','laxmiprasanna.pandiri@gmail.com',1247,40000,'Savings')";
			return	jdbctemplate.update(sql);
	}
	public void dropTable() {
		  String sql="DROP TABLE ICICI_ACCOUNT";
		  jdbctemplate.execute(sql);
		  System.out.println("table dropped");
	}
	
	public void retriveData(int accountno) {
		       
				String sql="SELECT * FROM ICICI_ACCOUNT WHERE CUSTOMER_ACCOUNT_NO="+accountno+"";
				Account queryForObject = jdbctemplate.queryForObject(sql, new AccountMapper());
				System.out.println("data from db :" +queryForObject);
			
	}
	
}
