package org.ssa.ironyard.bank.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountDAOImpl extends AbstractAccountDAO implements AccountDAO
{
    @Autowired
    public AccountDAOImpl(DataSource datasource)
    {
        super(datasource, new AccountORMImpl());
    }

    // static final Logger LOGGER = LogManager.getLogger(AccountDAOImpl.class);

}