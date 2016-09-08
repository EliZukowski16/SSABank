package org.ssa.ironyard.bank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.ssa.ironyard.bank.model.Customer;


@Component
public class CustomerService
{

    public List<Customer> read()
    {
        return new ArrayList<Customer>();
        
    }

    public Customer read(int customerID)
    {
        return null;
    }

    public Customer insert(Customer customer)
    {
        return null;
    }

}
