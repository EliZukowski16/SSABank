package org.ssa.ironyard.bank.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.ssa.ironyard.bank.model.Account;

public interface AccountService {

    public List<Account> readUser(int userID);
    public Account read(int accountID);
    public Account insert(Account account);
    public Account deposit(int accountID, BigDecimal amount);
    public Account withdraw(int accountID, BigDecimal amount);
    public boolean delete(Integer id);
    public Map<String, Account> transfer(Integer customerID, Integer sourceAccountID, Integer targetAccountID, BigDecimal amount);
	
}
