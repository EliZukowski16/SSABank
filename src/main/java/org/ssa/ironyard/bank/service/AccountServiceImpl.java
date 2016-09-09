package org.ssa.ironyard.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssa.ironyard.bank.dao.AccountDAO;
import org.ssa.ironyard.bank.model.Account;

@Component
public class AccountServiceImpl implements AccountService {

    @Autowired
	AccountDAO accountDao;
	
	@Override
	public List<Account> readUser(int userID) {
		List<Account> accounts = new ArrayList<>();
		accounts.add(accountDao.read(userID));
		return accounts;
	}

	@Override
	public Account read(int accountID) {
	
		return null;
	}

	@Override
	public Account insert(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account deposit(int accountID, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account withdraw(int accountID, BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

}
