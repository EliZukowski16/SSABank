package org.ssa.ironyard.bank.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.ssa.ironyard.bank.model.Account;
import org.ssa.ironyard.bank.model.Account.AccountType;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.AccountService;
import org.ssa.ironyard.bank.service.AccountServiceImpl;

public class AccountRestControllerTest
{

    static AccountService as;
    AccountRestController controller;
    static List<Customer> allCustomers;
    static List<Account> allAccounts;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        BufferedReader reader = null;
        allCustomers = new ArrayList<>();
        allAccounts = new ArrayList<>();

        try
        {
            reader = Files.newBufferedReader(
                    Paths.get("C:\\Users\\admin\\workspace\\DatabaseApp\\resources\\MOCK_CUSTOMER_DATA.csv"),
                    Charset.defaultCharset());

            String line;
            int id = 1;

            while (null != (line = reader.readLine()))
            {
                String[] names = line.split(",");
                allCustomers.add(new Customer(id, names[0], names[1], true));
                id++;
            }

        }
        catch (IOException iex)
        {
            System.err.println(iex);
            throw iex;
        }
        finally
        {
            if (null != reader)
                reader.close();
        }

        try
        {
            reader = Files.newBufferedReader(
                    Paths.get("C:\\Users\\admin\\workspace\\DatabaseApp\\resources\\MOCK_ACCOUNT_DATA.csv"),
                    Charset.defaultCharset());

            String line;
            int id = 1;

            while (null != (line = reader.readLine()))
            {
                String[] account = line.split(",");
                Integer randomCustomerID = (int) (Math.random() * allCustomers.size());
                allAccounts.add(new Account(id, allCustomers.get(randomCustomerID), AccountType.getInstance(account[0]),
                        BigDecimal.valueOf(Double.valueOf(account[1])), true));
                id++;
            }

        }
        catch (IOException iex)
        {
            System.err.println(iex);
            throw iex;
        }
        finally
        {
            if (null != reader)
                reader.close();
        }

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    @Before
    public void setUp() throws Exception
    {
        as = EasyMock.niceMock(AccountServiceImpl.class);
        this.controller = new AccountRestController(as);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void viewAllCustomerAccounts()
    {
        for (int i = 0; i < allCustomers.size(); i++)
        {   
            List<Account> customerAccounts = new ArrayList<>();
            
            EasyMock.expect(as.readUser(allCustomers.get(i).getId())).andReturn(customerAccounts);
            EasyMock.replay(as);
            
            for (Account a : allAccounts)
            {
                if(a.getCustomer().getId() == allCustomers.get(i).getId())
                    customerAccounts.add(a);
            }
            
            ResponseEntity<List<Account>> accounts = this.controller.allAccounts(allCustomers.get(i).getId().toString());
            
            for(int j = 0; j < customerAccounts.size(); j++)
            {
                for(int k = 0; k < accounts.getBody().size(); k++)
                {
                    if(k == j)
                    {
                        assertTrue(customerAccounts.get(j).deeplyEquals(accounts.getBody().get(k)));
                    }
                    else
                    {
                        assertFalse(customerAccounts.get(j).deeplyEquals(accounts.getBody().get(k)));
                    }
                    
                }
                
            }
            
            EasyMock.verify(as);
            
            EasyMock.reset(as);
        }
    }

}
