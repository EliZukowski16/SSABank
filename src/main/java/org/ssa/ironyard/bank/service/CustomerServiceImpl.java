package org.ssa.ironyard.bank.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		return customers.stream()
                .sorted((c1, c2) -> c1.getFirstName().compareTo(c2.getFirstName()))
                .sorted((c1, c2) -> c1.getLastName().compareTo(c2.getLastName())).collect(Collectors.toList());
	}

	@Override
	public Customer read(int customerID) {
		return customerDao.read(customerID);
	}

	@Override
	public Customer insert(Customer customer) {
		return customerDao.insert(customer);
	}

}
