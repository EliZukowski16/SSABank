package org.ssa.ironyard.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssa.ironyard.bank.dao.AccountDAO;
import org.ssa.ironyard.bank.dao.AccountDAOEager;
import org.ssa.ironyard.bank.model.Account;

@Component
public class AccountServiceImpl implements AccountService {

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
		a1.adjustBalance("deposit", amount);
		return accountDao.update(a1);
	}

	@Override
	public Account withdraw(int accountID, BigDecimal amount) {
		Account a1 = accountDao.read(accountID);
		a1.adjustBalance("withdraw", amount);
		return accountDao.update(a1);
	}

}
