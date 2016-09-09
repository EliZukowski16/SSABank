package org.ssa.ironyard.bank.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerDAOImpl extends AbstractCustomerDAO implements CustomerDAO
{

    @Autowired
    public CustomerDAOImpl(DataSource datasource)
    {
        super(datasource, new CustomerORMImpl());
    }

}
