package org.ssa.ironyard.bank.controller;

import java.math.BigDecimal;
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
import org.ssa.ironyard.bank.model.Account;
import org.ssa.ironyard.bank.model.Account.AccountType;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.AccountService;

@RestController
@RequestMapping("/ssa-bank/customers/{customerID}")
public class AccountRestController
{
    @Autowired
    AccountService accountService;

    static Logger LOGGER = LogManager.getLogger(AccountRestController.class);

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Account>> allAccounts(@PathVariable String customerID)
    {
        LOGGER.info("Returning List of Accounts for Customer ID: {}", customerID);

        List<Account> customersAccounts = accountService.readUser(Integer.parseInt(customerID));

        for (Account a : customersAccounts)
        {
            LOGGER.trace("Acc ID: {}, Cust ID: {}, Account: {}, Balance: {}", a.getId().toString(),
                    a.getCustomer().getId(), a.getType(), a.getBalance());
        }

        return ResponseEntity.ok(customersAccounts);
    }

    @RequestMapping(value = "/accounts/{accountID}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCustomerAccount(@PathVariable String customerID, @PathVariable String accountID)
    {
        Map<String, Object> accountMap = new HashMap<>();
        
        LOGGER.info("Returning Single Account with ID: {} and Customer with ID: {}", accountID, customerID);

        Account account = accountService.read(Integer.parseInt(accountID));

        if (account != null)
        {
            LOGGER.trace("Acc ID: {}, Cust ID: {}, Account: {}, Balance: {}", account.getId().toString(),
                    account.getCustomer().getId(), account.getType(), account.getBalance());
            accountMap.put("success", account);
        }
        else
        {
            accountMap.put("error", "");
            LOGGER.info("account with ID: {} does not exist, sending error", accountID);
        }
        return ResponseEntity.ok(accountMap);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addAccount(HttpServletRequest request, @PathVariable String customerID)
    {
        Map<String, Object> accountMap = new HashMap<>();
        
        Integer custID = Integer.parseInt(customerID);
        AccountType type = AccountType.getInstance(request.getParameter("account"));
        BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(request.getParameter("balance")));

        LOGGER.info("Adding Single Account - Cust ID: {}, Account: {}, Balance: {}", custID, type, balance);

        Account addedAccount = accountService.insert(new Account(new Customer(custID, null, null), type, balance));
    
        if (addedAccount != null)
        {
            LOGGER.trace("Added Account to Database - Acct ID: {}, Cust ID: {}, Account: {}, Balance: {}",
                    addedAccount.getId(), addedAccount.getCustomer().getId(), addedAccount.getType(),
                    addedAccount.getBalance());
            accountMap.put("success", addedAccount);
        }
        else
        {
            accountMap.put("error", new Account(null, new Customer(custID, null, null), null, null));
            LOGGER.info("Account could not be added");
        }
        return ResponseEntity.ok(accountMap);
    }

    @RequestMapping(value = "/accounts/{accountID}/{transaction}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> performTransaction(HttpServletRequest request, @PathVariable String transaction,
            @PathVariable String accountID, @PathVariable String customerID)
    {
        Map<String, Object> accountMap = new HashMap<>(); 
        
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(request.getParameter("amount")));
        Integer accID = Integer.parseInt(accountID);

        LOGGER.info("Performing Transaction on Account ID: {} with Amount: {}", accountID, amount);

        Account updatedAccount;

        switch (transaction.toLowerCase())
        {
        case "deposit":
            LOGGER.info("Performing Deposit");
            updatedAccount = accountService.deposit(accID, amount);
            LOGGER.info("Account ID: {} new Balance: {}", updatedAccount.getId(), updatedAccount.getBalance());
            break;
        case "withdraw":
            LOGGER.info("Performing Withdrawal");
            updatedAccount = accountService.withdraw(accID, amount);
            LOGGER.info("Account ID: {} new Balance: {}", updatedAccount.getId(), updatedAccount.getBalance());
            break;
        default:
            LOGGER.info("Did not understand transaction value of {}, no action performed", transaction);
            updatedAccount = null;
            break;
        }
        
        if(updatedAccount != null)
        {
            accountMap.put("success", updatedAccount);
        }
        else
        {
            accountMap.put("error", new Account(accID, new Customer(Integer.parseInt(customerID), null, null), null, null));
        }

        return ResponseEntity.ok(accountMap);
    }
    
    @RequestMapping(value = "/accounts/{accountID}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteAccount(@PathVariable String accountID)
    {
        LOGGER.info("Deleting Single Accountr with ID: {}", accountID);

        Integer id = Integer.parseInt(accountID);

        boolean deleteSuccess = accountService.delete(id);

        LOGGER.info("Delete was successful: {}", deleteSuccess);

        return deleteSuccess;
    }

}
