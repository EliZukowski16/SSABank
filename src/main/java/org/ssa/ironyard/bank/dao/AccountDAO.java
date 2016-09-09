package org.ssa.ironyard.bank.dao;

import java.util.List;

import org.ssa.ironyard.bank.model.Account;
import org.ssa.ironyard.bank.dao.DAO;

public interface AccountDAO extends DAO<Account>
{
    public List<Account> readUser(int user);
    public List<Account> readUnderwater();
}
