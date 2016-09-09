package org.ssa.ironyard.Service;

import java.util.ArrayList;
import java.util.List;

import org.ssa.ironyard.customer.dao.CustomerDAO;
import org.ssa.ironyard.customer.model.Customer;

public class CustomerServiceImpl implements CustomerService {

	CustomerDAO customerDao;
	
	@Override
	public List<Customer> read() {
		List<Customer> customers = new ArrayList<>();
		customers.addAll(customerDao.read());
		return customers;
	}

	@Override
	public Customer read(int customerID) {
		customerDao.read(customerID);
		return null;
	}

	@Override
	public Customer insert(Customer customer) {
		return customerDao.insert(customer);
	}


	@Override
	public Customer update(Customer customer) {
		return customerDao.update(customer);
	}

}
