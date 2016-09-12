package org.ssa.ironyard.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ssa-bank")
public class Home
{
    @RequestMapping(value = "/")
    public String home()
    {
        return "/customers.html";
    }
    
    @RequestMapping(value = "/accounts")
    public String accounts()
    {
        return "/accounts.html";
    }

}
