package org.ssa.ironyard.bank.controller;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ssa.ironyard.bank.service.CustomerService;

public class CustomerRestControllerTest
{
    
    CustomerService cs;
    CustomerRestController controller;

    @BeforeClass
    public static void setUpBeforeClass()
    {

    }

    @AfterClass
    public static void tearDownAfterClass()
    {
    }

    @Before
    public void setUp()
    {
        this.cs = EasyMock.createNiceMock(CustomerService.class);
        this.controller = new CustomerRestController();
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void test()
    {

    }

}
