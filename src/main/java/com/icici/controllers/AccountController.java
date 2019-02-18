package com.icici.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.icici.components.SendEmail;
import com.icici.model.Account;
import com.icici.service.AccountService;

@RestController
@RequestMapping(value = "api/transfer")
public class AccountController {
	@Autowired
	AccountService service;
	@Autowired
	JavaMailSender sender;
	@Autowired
	SendEmail mail;

	@RequestMapping(value = "/account/details/{accountno}", method = RequestMethod.GET)
	@ResponseBody
	public Account getICICIAccountDetails(@PathVariable("accountno") int accountNumber) {
		Account account = new Account();
		account.setAccount_no(accountNumber);
		return service.getICICIAccountDetails(account);
	}

	@RequestMapping(value = "/icici")
	public String fundTransfer2icici(@RequestParam int fromSBI, @RequestParam int toICICI, @RequestParam int amount) {
		// List<ServiceInstance> instances = sbiservice.getInstances("sbi-service");
		// URI uri = instances.get(0).getUri();
		// http://localhost:8080/api/sbi/account/details/1266
		Account iciciAccount = new Account();
		iciciAccount.setAccount_no(toICICI);
		int amountToBeTransferred = amount;
		String baseUrl = "http://localhost:8080/api/sbi/account/details/" + fromSBI;
		System.out.println(baseUrl);
		RestTemplate template = new RestTemplate();
		Account sbiaccount = template.getForObject(baseUrl, Account.class);
		System.out.println("ICIC calling sbi service and fetched detaisl : " + sbiaccount);

		boolean moneyTransferFromSbiToIcici = service.moneyTransferToICICI(sbiaccount, iciciAccount,
				amountToBeTransferred);
		if (moneyTransferFromSbiToIcici == true) {
			Account iciciAccountDetails = service.getICICIAccountDetails(iciciAccount);
			mail.setTo(iciciAccountDetails.getEmail());
			mail.setSubject("ICICI Transaction Alert!");
			mail.setText(
					"Transaction from SBI to ICICI was successful. Please login to account to know the updated balance");
			sender.send(mail);
			return "transaction successful please check your email ";

		} else {

			mail.setTo(sbiaccount.getEmail());
			mail.setSubject("SBI Transaction Alert!");
			mail.setText("Transaction from SBI to ICICI was Unsuccessful. ");
			sender.send(mail);
			return "transaction was unsuccessful please check your email for more details";

		}

	}

	@RequestMapping(value = "/sbi")
	public String fundTransfer2sbi(@RequestParam("icici") int fromICICI, @RequestParam("sbi") int toSBI, @RequestParam("amount") int amount) {
		// List<ServiceInstance> instances = sbiservice.getInstances("sbi-service");
		// URI uri = instances.get(0).getUri();
		// http://localhost:8080/api/sbi/account/details/1266
		Account iciciAccount = new Account();
		iciciAccount.setAccount_no(fromICICI);
		int amountToBeTransferred = amount;
		String baseUrl = "http://localhost:8080/api/sbi/account/details/" + toSBI;
		System.out.println(baseUrl);
		RestTemplate template = new RestTemplate();
		Account sbiaccount = template.getForObject(baseUrl, Account.class);
		System.out.println("ICIC calling sbi service and fetched detaisl : " + sbiaccount);

		boolean moneyTransferFromSbiToIcici = service.moneyTransfer(iciciAccount, sbiaccount, amountToBeTransferred);
		if (moneyTransferFromSbiToIcici == true) {
			mail.setTo(sbiaccount.getEmail());
			mail.setSubject("SBI Transaction Alert!");
			mail.setText(
					"Transaction from ICICI to SBI was successful. Please login to account to know the updated balance");
			sender.send(mail);
			return "transaction successful please check your email ";
		} else {
			Account iciciAccountDetails = service.getICICIAccountDetails(iciciAccount);
			mail.setTo(iciciAccountDetails.getEmail());
			mail.setSubject("ICICI Transaction Alert!");
			mail.setText("Transaction from ICICI to SBI was Unsuccessful. ");
			sender.send(mail);
			return "transaction was unsuccessful please check your email for more details";
		}

	}

}
