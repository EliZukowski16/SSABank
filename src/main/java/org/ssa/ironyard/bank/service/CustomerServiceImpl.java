package org.ssa.ironyard.bank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssa.ironyard.bank.dao.CustomerDAO;
import org.ssa.ironyard.bank.model.Customer;

@Component
public class CustomerServiceImpl implements CustomerService {

    @Autowired
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

}
