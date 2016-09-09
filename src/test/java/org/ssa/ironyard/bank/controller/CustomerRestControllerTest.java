package org.ssa.ironyard.bank.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.service.CustomerService;

public class CustomerRestControllerTest
{
    
    CustomerService cs;
    CustomerRestController controller;
    List<Customer> allCustomers;
    
    @BeforeClass
    public void setupBefore() throws Exception
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
                allCustomers.add(new Customer(id, names[0], names[1]));
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
        this.cs = EasyMock.createNiceMock(CustomerService.class);
        this.controller = new CustomerRestController();
    }


    @Test
    public void viewAllCustomers()
    {
        EasyMock.expect(this.cs.read()).andReturn(allCustomers);
        EasyMock.replay(this.cs);
        
        ResponseEntity<List<Customer>> customers = this.controller.allCustomers();

    }

}
