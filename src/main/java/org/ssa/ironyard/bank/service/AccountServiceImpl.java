package org.ssa.ironyard.bank.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.ssa.ironyard.bank.dao.AccountDAOEager;
import org.ssa.ironyard.bank.model.Account;

@Component
public class AccountServiceImpl implements AccountService {

    Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);
    
    @Autowired
	AccountDAOEager accountDao;
	
	@Override
	@Transactional
	public List<Account> readUser(int userID) {
		List<Account> accounts = new ArrayList<>();
		accounts.addAll(accountDao.readUser(userID));
		return accounts;
	}

	@Override
	@Transactional
	public Account read(int accountID) {
		return accountDao.read(accountID);
	}

	@Override
	@Transactional
	public Account insert(Account account) {
		return accountDao.insert(account);
	}

	@Override
	@Transactional
	public Account deposit(int accountID, BigDecimal amount) {
		Account a1 = accountDao.read(accountID);
		LOGGER.info("Performing deposit on Account {}",a1.getId());
		a1.adjustBalance("deposit", amount);
		LOGGER.info("New balance is {}", a1.getBalance());
		return accountDao.update(a1);
	}

	@Override
	@Transactional
	public Account withdraw(int accountID, BigDecimal amount) {
		Account a1 = accountDao.read(accountID);
		LOGGER.info("Performing withdraw on Account {}",a1.getId());
		a1.adjustBalance("withdraw", amount);
	    LOGGER.info("New balance is {}",a1.getBalance());
		return accountDao.update(a1);
	}

    @Override
    @Transactional
    public boolean delete(Integer id)
    {
        return accountDao.delete(id);
    }

    @Override
    @Transactional
    public Map<String, Account> transfer(Integer customerID, Integer sourceAccountID, Integer targetAccountID,
            BigDecimal amount) {
        List<Account> customerAccounts = accountDao.readUser(customerID);
        Account sourceAccount = null, targetAccount = null;
        
        Map<String, Account> transferAccounts = new HashMap<>();
        
        for(Account a : customerAccounts) {
            if(a.getId() == sourceAccountID)
                sourceAccount = a;
            if(a.getId() == targetAccountID)
                targetAccount = a;
            if((sourceAccount != null) && (targetAccount != null))
                break;
        }
        
        sourceAccount.adjustBalance("withdraw", amount);
        targetAccount.adjustBalance("deposit", amount);
        
        transferAccounts.put("source", accountDao.update(sourceAccount));
        transferAccounts.put("target", accountDao.update(targetAccount));
        
        return transferAccounts;
    }

}
