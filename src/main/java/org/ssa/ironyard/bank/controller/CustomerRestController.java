package org.ssa.ironyard.bank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getCustomer(@PathVariable String customerID)
    {
        Map<String, Object> customerMap = new HashMap<>();

        LOGGER.info("Returning Single Customer with ID: {}", customerID);

        Customer customer = customerService.read(Integer.parseInt(customerID));

        if (customer != null)
        {
            LOGGER.trace("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(),
                    customer.getFirstName(), customer.getLastName());
            customerMap.put("success", customer);
        }
        else
        {
            customerMap.put("error", "");
            LOGGER.info("Customer with ID: {} does not exist, sending error", customerID);
        }
        return ResponseEntity.ok(customerMap);
    }

    @RequestMapping(value = "/customers", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCustomer(HttpServletRequest request)
    {
        Map<String, Object> customerMap = new HashMap<>();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        LOGGER.info("Adding Single Customer - First Name: {}, Last Name: {}", firstName, lastName);

        Customer customer = customerService.insert(new Customer(firstName, lastName));

        if (customer != null)
        {
            LOGGER.trace("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(),
                    customer.getFirstName(), customer.getLastName());
            customerMap.put("success", customer);
        }
        else
        {
            customerMap.put("error", new Customer(null, firstName, lastName));
            LOGGER.info("Customer could not be added");
        }
        return ResponseEntity.ok(customerMap);

    }

    @RequestMapping(value = "/customers/{customerID}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editCustomer(@PathVariable String customerID, HttpServletRequest request)
    {
        LOGGER.info("Editing Single Customer with ID: {}", customerID);

        Map<String, Object> customerMap = new HashMap<>();

        Integer id = Integer.parseInt(customerID);

        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        LOGGER.info("Got first name: {} and last name: {}", firstName, lastName);

        Customer customer = customerService.update(new Customer(id, firstName, lastName));

        if (customer != null)
        {
            LOGGER.trace("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(),
                    customer.getFirstName(), customer.getLastName());
            customerMap.put("success", customer);
        }
        else
        {
            customerMap.put("error", new Customer(id, firstName, lastName));
            LOGGER.info("Customer with ID: {} could not be updated", customerID);
        }
        return ResponseEntity.ok(customerMap);
    }

    @RequestMapping(value = "/customers/{customerID}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteCustomer(@PathVariable String customerID)
    {
        LOGGER.info("Deleting Single Customer with ID: {}", customerID);

        Integer id = Integer.parseInt(customerID);

        boolean deleteSuccess = customerService.delete(id);

        LOGGER.info("Delete was successful: {}", deleteSuccess);

        return deleteSuccess;
    }

}
