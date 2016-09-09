package org.ssa.ironyard.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.ssa.ironyard.account.dao.AccountDAO;
import org.ssa.ironyard.account.model.Account;

public class AccountServiceImpl implements AccountService {

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
