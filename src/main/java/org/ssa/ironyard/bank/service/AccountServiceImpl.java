package org.ssa.ironyard.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssa.ironyard.bank.dao.AccountDAO;
import org.ssa.ironyard.bank.dao.AccountDAOEager;
import org.ssa.ironyard.bank.model.Account;

@Component
public class AccountServiceImpl implements AccountService {

    Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);
    
    @Autowired
	AccountDAOEager accountDao;
	
	@Override
	public List<Account> readUser(int userID) {
		List<Account> accounts = new ArrayList<>();
		accounts.addAll(accountDao.readUser(userID));
		return accounts;
	}

	@Override
	public Account read(int accountID) {
		return accountDao.read(accountID);
	}

	@Override
	public Account insert(Account account) {
		return accountDao.insert(account);
	}

	@Override
	public Account deposit(int accountID, BigDecimal amount) {
		Account a1 = accountDao.read(accountID);
		LOGGER.info("Performing deposit on Account {}",a1.getId());
		a1.adjustBalance("deposit", amount);
		LOGGER.info("New balance is {}", a1.getBalance());
		return accountDao.update(a1);
	}

	@Override
	public Account withdraw(int accountID, BigDecimal amount) {
		Account a1 = accountDao.read(accountID);
		LOGGER.info("Performing withdraw on Account {}",a1.getId());
		a1.adjustBalance("withdraw", amount);
	    LOGGER.info("New balance is {}",a1.getBalance());
		return accountDao.update(a1);
	}

}
