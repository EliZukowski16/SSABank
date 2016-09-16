package org.ssa.ironyard.bank.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.easymock.Capture;
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
                    Paths.get(".\\src\\test\\resources\\MOCK_CUSTOMER_DATA.csv"),
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
        this.controller = new CustomerRestController(cs);
    }

    @Test
    public void viewAllCustomersTest()
    {
        EasyMock.expect(cs.read()).andReturn(allCustomers);
        EasyMock.replay(cs);

        ResponseEntity<List<Customer>> customers = this.controller.allCustomers();

        for (int i = 0; i < allCustomers.size(); i++)
        {
            for (int j = 0; j < customers.getBody().size(); j++)
            {
                if (i == j)
                {
                    assertTrue(allCustomers.get(i).deeplyEquals(customers.getBody().get(j)));
                }
                else
                {
                    assertFalse(allCustomers.get(i).deeplyEquals(customers.getBody().get(j)));
                }

            }

        }

        EasyMock.verify(cs);

        assertEquals(allCustomers, customers.getBody());
    }

    @Test
    public void getSingleCustomerTest()
    {
        for (int i = 0; i < allCustomers.size(); i++)
        {

            EasyMock.expect(cs.read(i + 1)).andReturn(allCustomers.get(i));
            EasyMock.replay(cs);

            Integer j = i + 1;

            ResponseEntity<Map<String, Object>> customerMap = this.controller.getCustomer(j.toString());

            Customer customer = (Customer) customerMap.getBody().get("success");
            assertTrue(customerMap.getBody().containsKey("success"));
            assertFalse(customerMap.getBody().containsKey("error"));
            assertTrue(allCustomers.get(i).deeplyEquals(customer));

            EasyMock.reset(cs);
        }
    }

    @Test
    public void addingCustomerTest()
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("firstName", "John");
        mockRequest.addParameter("lastName", "Smith");

        Customer testCustomerLoaded = new Customer(1, mockRequest.getParameter("firstName"),
                mockRequest.getParameter("lastName"), true);

        Capture<Customer> capturedCustomer = Capture.<Customer>newInstance();

        EasyMock.expect(cs.insert(EasyMock.capture(capturedCustomer))).andReturn(testCustomerLoaded);
        EasyMock.replay(cs);

        ResponseEntity<Map<String, Object>> customerMap = this.controller.addCustomer(mockRequest);

        Customer customer = (Customer) customerMap.getBody().get("success");

        Customer parameters = capturedCustomer.getValue();
        
        assertTrue(customerMap.getBody().containsKey("success"));
        assertFalse(customerMap.getBody().containsKey("error"));
        assertTrue(testCustomerLoaded.deeplyEquals(customer));
        assertFalse(testCustomerLoaded.deeplyEquals(parameters));
        assertEquals(1, (int) customer.getId());
        assertNotEquals(parameters.getId(), customer.getId());
        assertEquals(parameters.getFirstName(), customer.getFirstName());
        assertEquals(parameters.getLastName(), customer.getLastName());
        assertTrue(customer.isLoaded());
        assertFalse(parameters.isLoaded());

    }

    @Test
    public void updatingValidCustomerTest()
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("firstName", "Mike");
        mockRequest.addParameter("lastName", "Jones");

        Customer testCustomerUpdated = new Customer(1, mockRequest.getParameter("firstName"),
                mockRequest.getParameter("lastName"), true);

        Capture<Customer> capturedCustomer = Capture.<Customer>newInstance();

        EasyMock.expect(cs.update(EasyMock.capture(capturedCustomer))).andReturn(testCustomerUpdated);
        EasyMock.replay(cs);

        ResponseEntity<Map<String, Object>> customerMap = this.controller.editCustomer("1", mockRequest);

        Customer customer = (Customer) customerMap.getBody().get("success");
        Customer parameters = capturedCustomer.getValue();
        
        assertTrue(customerMap.getBody().containsKey("success"));
        assertFalse(customerMap.getBody().containsKey("error"));
        assertTrue(testCustomerUpdated.deeplyEquals(customer));
        assertFalse(customer.deeplyEquals(parameters));
        assertEquals(parameters.getId(), customer.getId());
        assertEquals(parameters.getFirstName(), customer.getFirstName());
        assertEquals(parameters.getLastName(), customer.getLastName());
        assertTrue(customer.isLoaded());
        assertFalse(parameters.isLoaded());

        EasyMock.verify(cs);
    }

    @Test
    public void deletingValidCustomer()
    {
        EasyMock.expect(cs.delete(1)).andReturn(true);
        EasyMock.replay(cs);

        assertTrue(this.controller.deleteCustomer("1"));

        EasyMock.verify(cs);
    }

    @Test
    public void addingInvalidCustomerTest()
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("firstName", "null");
        mockRequest.addParameter("lastName", "null");

        Customer invalidCustomer = null;

        Capture<Customer> capturedCustomer = Capture.<Customer>newInstance();

        EasyMock.expect(cs.insert(EasyMock.capture(capturedCustomer))).andReturn(invalidCustomer);
        EasyMock.replay(cs);

        ResponseEntity<Map<String, Object>> customerMap = this.controller.addCustomer(mockRequest);

        Customer customer = (Customer) customerMap.getBody().get("error");
        
        assertFalse(customerMap.getBody().containsKey("success"));
        assertTrue(customerMap.getBody().containsKey("error"));
        assertTrue(capturedCustomer.getValue().deeplyEquals(customer));
        assertEquals(capturedCustomer.getValue().getId(), customer.getId());
        assertEquals(capturedCustomer.getValue().getFirstName(), customer.getFirstName());
        assertEquals(capturedCustomer.getValue().getLastName(), customer.getLastName());
        assertFalse(capturedCustomer.getValue().isLoaded());
        assertFalse(customer.isLoaded());
        
        EasyMock.verify(cs);
    }

    @Test
    public void updatingInvalidCustomerTest()
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addParameter("firstName", "null");
        mockRequest.addParameter("lastName", "null");

        Customer invalidCustomer = null;

        Capture<Customer> capturedCustomer = Capture.<Customer>newInstance();

        EasyMock.expect(cs.update(EasyMock.capture(capturedCustomer))).andReturn(invalidCustomer);
        EasyMock.replay(cs);

        ResponseEntity<Map<String, Object>> customerMap = this.controller.editCustomer("0", mockRequest);
        
        Customer customer = (Customer) customerMap.getBody().get("error");
        
        assertFalse(customerMap.getBody().containsKey("success"));
        assertTrue(customerMap.getBody().containsKey("error"));
        assertTrue(capturedCustomer.getValue().deeplyEquals(customer));
        assertEquals(capturedCustomer.getValue().getId(), customer.getId());
        assertEquals(capturedCustomer.getValue().getFirstName(), customer.getFirstName());
        assertEquals(capturedCustomer.getValue().getLastName(), customer.getLastName());
        assertFalse(capturedCustomer.getValue().isLoaded());
        assertFalse(customer.isLoaded());
        
        EasyMock.verify(cs);
    }
    
    @Test
    public void getSingleCustomerThatDoesNotExist()
    {
        EasyMock.expect(cs.read(0)).andReturn(null);
        EasyMock.replay(cs);


        ResponseEntity<Map<String, Object>> customerMap = this.controller.getCustomer("0");

        assertFalse(customerMap.getBody().containsKey("success"));
        assertTrue(customerMap.getBody().containsKey("error"));
        assertEquals("", customerMap.getBody().get("error"));
        
        EasyMock.verify(cs);
    }
    
    @Test
    public void deleteInvalidCustomerTest()
    {
        EasyMock.expect(cs.delete(0)).andReturn(false);
        EasyMock.replay(cs);

        assertFalse(this.controller.deleteCustomer("0"));

        EasyMock.verify(cs);
    }
    
    

}
