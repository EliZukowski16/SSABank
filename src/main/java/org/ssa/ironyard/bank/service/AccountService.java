package org.ssa.ironyard.Service;

import java.math.BigDecimal;
import java.util.List;

import org.ssa.ironyard.account.model.Account;

public interface AccountService {

    public List<Account> readUser(int userID);
    public Account read(int accountID);
    public Account insert(Account account);
    public Account deposit(int accountID, BigDecimal amount);
    public Account withdraw(int accountID, BigDecimal amount);
	
}
