package org.ssa.ironyard.bank.controller;

import java.util.Enumeration;
import java.util.List;

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

    public CustomerRestController(CustomerService cs)
    {
        this.customerService = cs;
    }


    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Customer>> allCustomers()
    {
        LOGGER.info("Returning List of Customers");

        List<Customer> allCustomers = customerService.read();

        for (Customer c : allCustomers)
        {
            LOGGER.trace("Cust ID: {}, First Name: {}, Last Name: {}", c.getId().toString(), c.getFirstName(),
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

        LOGGER.trace("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(), customer.getFirstName(),
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

        LOGGER.trace("Added Customer to Database - Cust ID: {}, First Name: {}, Last Name:{}", addedCustomer.getId(),
                addedCustomer.getFirstName(), addedCustomer.getLastName());

        return ResponseEntity.ok(addedCustomer);

    }

    @RequestMapping(value = "/customers/{customerID}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Customer> editCustomer(@PathVariable String customerID, HttpServletRequest request)
    {
        LOGGER.info("Editing Single Customer with ID: {}", customerID);

        Integer id = Integer.parseInt(customerID);
        
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        Enumeration<String> parameters = request.getParameterNames();
        
        while(parameters.hasMoreElements())
        {
            LOGGER.info(parameters.nextElement());
        }
        
        LOGGER.info("Got first name: {} and last name: {}", firstName, lastName);

        Customer customer = customerService.update(new Customer(id, firstName, lastName));

        LOGGER.info("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(), customer.getFirstName(),
                customer.getLastName());

        return ResponseEntity.ok(customer);
    }

}
