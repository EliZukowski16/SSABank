package org.ssa.ironyard.bank.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.CustomerService;


@RestController
@RequestMapping("/ssa-bank")
public class CustomerRestController
{

    @Autowired
    CustomerService customerService;

    static Logger LOGGER = LogManager.getLogger(CustomerRestController.class);

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Customer>> allCustomers()
    {
        LOGGER.info("Returning List of Customers");

        List<Customer> allCustomers = customerService.read().stream()
                .sorted((c1,c2) -> c1.getFirstName().compareTo(c2.getFirstName()))
                .sorted((c1, c2) -> c1.getLastName().compareTo(c2.getLastName()))
                .collect(Collectors.toList());;
        
        for (Customer c : allCustomers)
        {
            LOGGER.info("Cust ID: {}, First Name: {}, Last Name: {}", c.getId().toString(), c.getFirstName(),
                    c.getLastName());
        }

        return ResponseEntity.ok(allCustomers);
    }

    @RequestMapping(value = "/customers/{customerID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Customer> getCustomer(@PathVariable String customerID)
    {
        LOGGER.info("Returning Single Customer with ID: {}", customerID);

        Customer customer = customerService.read(Integer.parseInt(customerID));

        LOGGER.info("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(), customer.getFirstName(),
                customer.getLastName());

        return ResponseEntity.ok(customer);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Customer> addCustomer(HttpServletRequest request)
    {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        LOGGER.info("Adding Single Customer - First Name: {}, Last Name: {}", firstName, lastName);

        Customer addedCustomer = customerService.insert(new Customer(firstName, lastName));

        LOGGER.info("Added Customer to Database - Cust ID: {}, First Name: {}, Last Name:{}", addedCustomer.getId(),
                addedCustomer.getFirstName(), addedCustomer.getLastName());

        return ResponseEntity.ok(addedCustomer);

    }

}
