package org.ssa.ironyard.bank.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.CustomerService;
import org.ssa.ironyard.bank.service.CustomerServiceImpl;

public class CustomerRestControllerTest
{
    
    static CustomerService cs;
    CustomerRestController controller;
    static List<Customer> allCustomers;
    
    @BeforeClass
    public static void setupBefore() throws Exception
    {
        BufferedReader reader = null;
        allCustomers = new ArrayList<>();

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
        
    }

    @Before
    public void setUp()
    {
        cs = EasyMock.niceMock(CustomerServiceImpl.class);
        this.controller = new CustomerRestController(this.cs);
    }


    @Test
    public void viewAllCustomersTest()
    {
        EasyMock.expect(cs.read()).andReturn(allCustomers);
        EasyMock.replay(cs);
        
        ResponseEntity<List<Customer>> customers = this.controller.allCustomers();
        
        for(int i = 0; i < allCustomers.size(); i++)
        {
            for(int j = 0; j < customers.getBody().size(); j++)
            {
                if(i == j)
                {
                    assertTrue(allCustomers.get(i).deeplyEquals(customers.getBody().get(j)));
                }
                else
                {
                    assertFalse(allCustomers.get(i).deeplyEquals(customers.getBody().get(j)));
                }
                
            }
            
        }
        
        assertEquals(allCustomers, customers.getBody());
    }
    
    @Test
    public void getSingleCustomerTest()
    {
        EasyMock.expect(cs.read(1)).andReturn(allCustomers.get(0));
        EasyMock.replay(cs);
        
        ResponseEntity<Map<String,Object>> customerMap = this.controller.getCustomer("1");
        
        Customer customer = (Customer) customerMap.getBody().get("success");
        
        assertTrue(allCustomers.get(0).deeplyEquals(customer));
    }
    
    @Test 
    public void addingCustomerTest()
    {
        MockHttpServletRequest mock = new MockHttpServletRequest();
        mock.addParameter("firstName", "John");
        mock.addParameter("lastName", "Smith");
        
        Customer testCustomerLoaded = new Customer(1, mock.getParameter("firstName"), mock.getParameter("lastName"), true);
        Customer testCustomerNotLoaded = new Customer("John", "Smith");
        
        EasyMock.expect(cs.insert(testCustomerNotLoaded)).andReturn(testCustomerLoaded);
        EasyMock.replay(cs);
        
        ResponseEntity<Customer> customer = this.controller.addCustomer(mock);
        
        assertTrue(testCustomerLoaded.deeplyEquals(customer.getBody()));
        assertEquals(1, (int) customer.getBody().getId());
        assertEquals("John", customer.getBody().getFirstName());
        assertEquals("Smith", customer.getBody().getLastName());
        assertEquals(true, customer.getBody().isLoaded());
        
    }
    
    @Test
    public void updatingCustomerTest()
    {
        MockHttpServletRequest mock = new MockHttpServletRequest();
        mock.addParameter("firstName", "Mike");
        mock.addParameter("lastName", "Jones");
        
        Customer testCustomerNotUpdated = new Customer(1, mock.getParameter("firstName"), mock.getParameter("lastName"));
        Customer testCustomerUpdated = new Customer(1, mock.getParameter("firstName"), mock.getParameter("lastName"), true);
        
        EasyMock.expect(cs.update(testCustomerNotUpdated)).andReturn(testCustomerUpdated);
        EasyMock.replay(cs);
        
        ResponseEntity<Map<String,Object>> customerMap = this.controller.editCustomer("1", mock);
        
        Customer customer = (Customer) customerMap.getBody().get("success");
        
        assertTrue(testCustomerUpdated.deeplyEquals(customer));
        assertEquals(1, (int) customer.getId());
        assertEquals("Mike", customer.getFirstName());
        assertEquals("Jones", customer.getLastName());
        assertEquals(true, customer.isLoaded());
    }

}
