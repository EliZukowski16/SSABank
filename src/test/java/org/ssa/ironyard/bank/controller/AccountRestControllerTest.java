package org.ssa.ironyard.bank.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
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
            reader = Files.newBufferedReader(Paths.get(".\\src\\test\\resources\\MOCK_CUSTOMER_DATA.csv"),
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
            reader = Files.newBufferedReader(Paths.get(".\\src\\test\\resources\\MOCK_ACCOUNT_DATA.csv"),
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

    @Before
    public void setUp() throws Exception
    {
        as = EasyMock.niceMock(AccountServiceImpl.class);
        this.controller = new AccountRestController(as);
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
                if (a.getCustomer().getId() == allCustomers.get(i).getId())
                    customerAccounts.add(a);
            }

            ResponseEntity<List<Account>> accounts = this.controller
                    .allAccounts(allCustomers.get(i).getId().toString());

            for (int j = 0; j < customerAccounts.size(); j++)
            {
                for (int k = 0; k < accounts.getBody().size(); k++)
                {
                    if (k == j)
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

    @Test
    public void getSingleAccountTest()
    {
        for (int i = 0; i < allCustomers.size(); i++)
        {
            List<Account> customerAccounts = new ArrayList<>();

            for (int j = 0; j < allAccounts.size(); j++)
            {
                if (allCustomers.get(i).getId() == allAccounts.get(j).getCustomer().getId())
                {
                    customerAccounts.add(allAccounts.get(j));
                }
            }

            for (int j = 0; j < customerAccounts.size(); j++)
            {
                EasyMock.expect(as.read(customerAccounts.get(j).getId())).andReturn(customerAccounts.get(j));
                EasyMock.replay(as);

                ResponseEntity<Map<String, Object>> accountMap = this.controller.getCustomerAccount(
                        allCustomers.get(i).getId().toString(), customerAccounts.get(j).getId().toString());

                Account account = (Account) accountMap.getBody().get("success");

                assertTrue(accountMap.getBody().containsKey("success"));
                assertFalse(accountMap.getBody().containsKey("error"));
                assertTrue(customerAccounts.get(j).deeplyEquals(account));

                EasyMock.verify(as);
                EasyMock.reset(as);
            }
        }
    }

    @Test
    public void addAccountTest()
    {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("CH");
        accountTypes.add("SA");

        for (int i = 0; i < allCustomers.size(); i++)
        {
            Integer randomAccount = (int) (Math.random() * 2);
            Double randomBalance = Math.random() * 1000;

            Account returnedAccount = new Account(i, allCustomers.get(i),
                    AccountType.getInstance(accountTypes.get(randomAccount)), BigDecimal.valueOf(randomBalance), true);

            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.addParameter("account", accountTypes.get(randomAccount));
            mockRequest.addParameter("balance", randomBalance.toString());

            Capture<Account> capturedAccount = Capture.<Account>newInstance();

            EasyMock.expect(as.insert(EasyMock.capture(capturedAccount))).andReturn(returnedAccount);
            EasyMock.replay(as);

            ResponseEntity<Map<String, Object>> accountMap = this.controller.addAccount(mockRequest,
                    allCustomers.get(i).getId().toString());

            Account account = (Account) accountMap.getBody().get("success");

            assertTrue(accountMap.getBody().containsKey("success"));
            assertFalse(accountMap.getBody().containsKey("false"));
            assertEquals(i, (int) account.getId());
            assertEquals(capturedAccount.getValue().getType(), account.getType());
            assertEquals(capturedAccount.getValue().getBalance(), account.getBalance());
            assertEquals(capturedAccount.getValue().getCustomer(), account.getCustomer());
            assertTrue(account.isLoaded());

            EasyMock.verify(as);
            EasyMock.reset(as);

        }

    }

    @Test
    public void performDepositToAccount()
    {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("CH");
        accountTypes.add("SA");

        for (int i = 0; i < allCustomers.size(); i++)
        {
            Integer randomAccount = (int) (Math.random() * 2);
            Double randomBalance = Math.random() * 1000;
            Double randomDeposit = Math.random() * 1000;

            Account originalAccount = new Account(i, allCustomers.get(i),
                    AccountType.getInstance(accountTypes.get(randomAccount)), BigDecimal.valueOf(randomBalance), true);

            Account returnedAccount = originalAccount.clone();
            returnedAccount.adjustBalance("deposit", BigDecimal.valueOf(randomDeposit));

            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.addParameter("amount", randomDeposit.toString());

            EasyMock.expect(as.deposit(i, BigDecimal.valueOf(randomDeposit))).andReturn(returnedAccount);
            EasyMock.replay(as);

            ResponseEntity<Map<String, Object>> accountMap = this.controller.performTransaction(mockRequest, "deposit",
                    originalAccount.getId().toString(), allCustomers.get(i).getId().toString());

            Account account = (Account) accountMap.getBody().get("success");

            assertTrue(accountMap.getBody().containsKey("success"));
            assertFalse(accountMap.getBody().containsKey("false"));
            assertEquals(i, (int) account.getId());
            assertEquals(returnedAccount.getType(), account.getType());
            assertEquals(returnedAccount.getBalance(), account.getBalance());
            assertEquals(returnedAccount.getCustomer(), account.getCustomer());
            assertTrue(account.isLoaded());

            EasyMock.verify(as);
            EasyMock.reset(as);
        }
    }

    @Test
    public void performWithdrawFromAccount()
    {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("CH");
        accountTypes.add("SA");

        for (int i = 0; i < allCustomers.size(); i++)
        {
            Integer randomAccount = (int) (Math.random() * 2);
            Double randomBalance = Math.random() * 1000;
            Double randomWithdrawal = Math.random() * 1000;

            Account originalAccount = new Account(i, allCustomers.get(i),
                    AccountType.getInstance(accountTypes.get(randomAccount)), BigDecimal.valueOf(randomBalance), true);

            Account returnedAccount = originalAccount.clone();
            returnedAccount.adjustBalance("withdraw", BigDecimal.valueOf(randomWithdrawal));

            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.addParameter("amount", randomWithdrawal.toString());

            EasyMock.expect(as.withdraw(i, BigDecimal.valueOf(randomWithdrawal))).andReturn(returnedAccount);
            EasyMock.replay(as);

            ResponseEntity<Map<String, Object>> accountMap = this.controller.performTransaction(mockRequest, "withdraw",
                    originalAccount.getId().toString(), allCustomers.get(i).getId().toString());

            Account account = (Account) accountMap.getBody().get("success");

            assertTrue(accountMap.getBody().containsKey("success"));
            assertFalse(accountMap.getBody().containsKey("false"));
            assertEquals(i, (int) account.getId());
            assertEquals(returnedAccount.getType(), account.getType());
            assertEquals(returnedAccount.getBalance(), account.getBalance());
            assertEquals(returnedAccount.getCustomer(), account.getCustomer());
            assertTrue(account.isLoaded());

            EasyMock.verify(as);
            EasyMock.reset(as);
        }
    }

    @Test
    public void performTransfer()
    {
        List<String> accountTypes = new ArrayList<>();
        accountTypes.add("CH");
        accountTypes.add("SA");

        for (int i = 0; i < allCustomers.size(); i++)
        {
            Map<String, Account> transferReturn = new HashMap<>();

            Integer randomSourceAccount = (int) (Math.random() * 2);
            Integer randomTargetAccount = (int) (Math.random() * 2);
            Double randomSourceBalance = Math.random() * 1000;
            Double randomTargetBalance = Math.random() * 1000;
            Double randomTransferAmount = Math.random() * 1000;

            Account sourceAccount = new Account(i, allCustomers.get(i),
                    AccountType.getInstance(accountTypes.get(randomSourceAccount)),
                    BigDecimal.valueOf(randomSourceBalance), true);

            Account targetAccount = new Account((i * 2), allCustomers.get(i),
                    AccountType.getInstance(accountTypes.get(randomTargetAccount)),
                    BigDecimal.valueOf(randomTargetBalance), true);

            if ((randomSourceBalance - randomTransferAmount) < 0)
            {
                transferReturn.put("source", null);
                transferReturn.put("target", null);
            }
            else
            {
                Account returnedSourceAccount = sourceAccount.clone();
                Account returnedTargetAccount = targetAccount.clone();

                returnedSourceAccount.adjustBalance("withdraw", BigDecimal.valueOf(randomTransferAmount));
                returnedTargetAccount.adjustBalance("deposit", BigDecimal.valueOf(randomTransferAmount));

                transferReturn.put("source", returnedSourceAccount);
                transferReturn.put("target", returnedTargetAccount);
            }

            MockHttpServletRequest mockRequest = new MockHttpServletRequest();
            mockRequest.addParameter("amount", randomTransferAmount.toString());

//            EasyMock.expect(as.withdraw(i, BigDecimal.valueOf(randomWithdrawal))).andReturn(returnedAccount);
//            EasyMock.replay(as);
//
//            ResponseEntity<Map<String, Object>> accountMap = this.controller.performTransaction(mockRequest, "withdraw",
//                    originalAccount.getId().toString(), allCustomers.get(i).getId().toString());
//
//            Account account = (Account) accountMap.getBody().get("success");
//
//            assertTrue(accountMap.getBody().containsKey("success"));
//            assertFalse(accountMap.getBody().containsKey("false"));
//            assertEquals(i, (int) account.getId());
//            assertEquals(returnedAccount.getType(), account.getType());
//            assertEquals(returnedAccount.getBalance(), account.getBalance());
//            assertEquals(returnedAccount.getCustomer(), account.getCustomer());
//            assertTrue(account.isLoaded());

            EasyMock.verify(as);
            EasyMock.reset(as);
        }

    }

    @Test
    public void getInvalidSingleAccountTest()
    {

    }

    @Test
    public void failAddAccountTest()
    {

    }

    @Test
    public void failPerformingDepositToAccount()
    {

    }

    @Test
    public void failPerformingWithdrawFromAccount()
    {

    }

    @Test
    public void failPerformingTransfer()
    {

    }

}
