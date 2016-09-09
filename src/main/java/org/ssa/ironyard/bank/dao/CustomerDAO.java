package org.ssa.ironyard.bank.dao;

import java.util.List;

import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.dao.DAO;

public interface CustomerDAO extends DAO<Customer>
{
    public List<Customer> read();
    public List<Customer> readFirstName(String firstName);
    public List<Customer> readLastName(String lastName);
}
