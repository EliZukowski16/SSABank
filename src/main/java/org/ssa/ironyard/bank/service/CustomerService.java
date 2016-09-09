package org.ssa.ironyard.Service;

import java.util.List;

import org.ssa.ironyard.customer.model.Customer;

public interface CustomerService {

	public List<Customer> read();
	public Customer read(int customerID);
	public Customer insert(Customer customer);
	
}
