package org.ssa.ironyard.bank;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author thurston
 */
@SpringBootApplication
public class BankStarter 
{
    static Logger LOGGER = LogManager.getLogger(BankStarter.class);

    public static void main(String[] args)
    {
        LOGGER.info("Starting application ssa-bank");
        SpringApplication.run(BankStarter.class, args);
    }
}
