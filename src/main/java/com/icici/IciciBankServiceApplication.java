package com.icici;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.icici.controllers.AccountController;
import com.icici.model.Account;

@SpringBootApplication
@ComponentScan(value= {"com.icici"})
@EnableTransactionManagement
public class IciciBankServiceApplication {
	
	
	public static void main(String[] args) throws SQLException {
		ConfigurableApplicationContext run = SpringApplication.run(IciciBankServiceApplication.class, args);
		// AccountController iciciController = run.getBean(AccountController.class);
		//bean.createTable();
		/*int insertData = bean.insertData();
		if(insertData==1) {
			System.out.println("data inserted ...");
		}
		*/
		
//		bean.retriveData(1247);
	//	 Account icici=new Account();
		// icici.setAccount_no(1247);
	
	}//

}

