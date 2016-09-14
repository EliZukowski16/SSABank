package org.ssa.ironyard.bank.service;

import java.util.List;

import org.ssa.ironyard.bank.model.Customer;

public interface CustomerService {

	public List<Customer> read();
	public Customer read(int customerID);
	public Customer insert(Customer customer);
    Customer update(Customer customer);
    boolean delete(int customerID);
}
