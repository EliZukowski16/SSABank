package org.ssa.ironyard.bank.controller;

import java.math.BigDecimal;
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
import org.ssa.ironyard.bank.model.Account;
import org.ssa.ironyard.bank.model.Account.AccountType;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.AccountService;
import org.ssa.ironyard.bank.service.CustomerService;

@RestController
@RequestMapping("/ssa-bank")
public class SSABankRestController
{

//    @Autowired
//    CustomerService customerService;
//
//    @Autowired
//    AccountService accountService;
//
//    static Logger LOGGER = LogManager.getLogger(SSABankRestController.class);
//
//    @RequestMapping(value = "/customers")
//    @ResponseBody
//    public ResponseEntity<List<Customer>> allCustomers()
//    {
//        LOGGER.info("Returning List of Customers");
//
//        List<Customer> allCustomers = customerService.read();
//
//        for (Customer c : allCustomers)
//        {
//            LOGGER.info("Cust ID: {}, First Name: {}, Last Name: {}", c.getId().toString(), c.getFirstName(),
//                    c.getLastName());
//        }
//
//        return ResponseEntity.ok(allCustomers);
//    }
//
//    @RequestMapping(value = "/customers/{customerID}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Customer> getCustomer(@PathVariable String customerID)
//    {
//        LOGGER.info("Returning Single Customer with ID: {}", customerID);
//
//        Customer customer = customerService.read(Integer.parseInt(customerID));
//
//        LOGGER.info("Cust ID: {}, First Name: {}, Last Name: {}", customer.getId().toString(), customer.getFirstName(),
//                customer.getLastName());
//
//        return ResponseEntity.ok(customer);
//    }
//
//    @RequestMapping(value = "/customers/add", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<Customer> addCustomer(HttpServletRequest request)
//    {
//        String firstName = request.getParameter("firstName");
//        String lastName = request.getParameter("lastName");
//
//        LOGGER.info("Adding Single Customer - First Name: {}, Last Name: {}", firstName, lastName);
//
//        Customer addedCustomer = customerService.insert(new Customer(firstName, lastName));
//
//        LOGGER.info("Added Customer to Database - Cust ID: {}, First Name: {}, Last Name:{}", addedCustomer.getId(),
//                addedCustomer.getFirstName(), addedCustomer.getLastName());
//
//        return ResponseEntity.ok(addedCustomer);
//
//    }
//
//    @RequestMapping("/customers/{customerID}/accounts")
//    @ResponseBody
//    public ResponseEntity<List<Account>> allAccounts(@PathVariable String customerID)
//    {
//        LOGGER.debug("Returning List of Accounts for Customer ID: {}", customerID);
//
//        List<Account> customersAccounts = accountService.readUser(Integer.parseInt(customerID));
//
//        for (Account a : customersAccounts)
//        {
//            LOGGER.info("Acc ID: {}, Cust ID: {}, Account: {}, Balance: {}", a.getId().toString(),
//                    a.getCustomer().getId(), a.getType(), a.getBalance());
//        }
//
//        return ResponseEntity.ok(customersAccounts);
//    }
//
//    @RequestMapping(value = "/customers/{customerID}/accounts/{accountID}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<Account> getCustomerAccount(@PathVariable String customerID, @PathVariable String accountID)
//    {
//        LOGGER.info("Returning Single Account with ID: {} for Customer with ID: {}", customerID, accountID);
//
//        Account account = accountService.read(Integer.parseInt(accountID));
//
//        LOGGER.info("Acc ID: {}, Cust ID: {}, Account: {}, Balance: {}", account.getId().toString(),
//                account.getCustomer().getId(), account.getType(), account.getBalance());
//
//        return ResponseEntity.ok(account);
//    }
//
//    @RequestMapping(value = "/customers/{customerID}/accounts/add", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<Account> addAccount(HttpServletRequest request, @PathVariable String customerID)
//    {
//        Integer custID = Integer.parseInt(customerID);
//        AccountType type = AccountType.getInstance(request.getParameter("account"));
//        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(request.getParameter("balance")));
//
//        LOGGER.info("Adding Single Account - Cust ID: {}, Account: {}, Balance: {}", custID, type, balance);
//
//        Account addedAccount = accountService.insert(new Account(new Customer(custID, null, null), type, balance));
//
//        LOGGER.info("Added Accont to Database - Acct ID: {}, Cust ID: {}, Account: {}, Balance: {}",
//                addedAccount.getId(), addedAccount.getCustomer().getId(), addedAccount.getType(),
//                addedAccount.getBalance());
//
//        return ResponseEntity.ok(addedAccount);
//
//    }
//
//    @RequestMapping(value = "/customers/{customerID}/accounts/{accountID}/{transaction}", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity<Account> performTransaction(HttpServletRequest request, @PathVariable String transaction, @PathVariable String accountID, @PathVariable String customerID)
//    {
//        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
//        
//        LOGGER.info("Performing Transaction on Account ID: {} with Amount: {}", accountID, amount);
//        
//        Account updatedAccount;
//        
//        switch (transaction.toLowerCase())
//        {
//        case "deposit":
//            LOGGER.info("Performing Deposit");
//            updatedAccount = accountService.deposit(Integer.parseInt(accountID), amount);
//            LOGGER.info("Account ID: {} new Balance: {}", updatedAccount.getId(), updatedAccount.getBalance());
//            break;
//        case "withdraw":
//            LOGGER.info("Performing Withdrawal");
//            updatedAccount = accountService.withdraw(Integer.parseInt(accountID), amount);
//            LOGGER.info("Account ID: {} new Balance: {}", updatedAccount.getId(), updatedAccount.getBalance());
//            break;
//        default:
//            LOGGER.info("Did not understand transaction value of {}, no action performed", transaction);
//            updatedAccount = null;
//            break;
//        }
//        
//        return ResponseEntity.ok(updatedAccount);
//    }

}
