package org.ssa.ironyard.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author thurston
 */
@SpringBootApplication
public class BankStarter 
{
//    static Logger LOGGER = LogManager.getLogger(BankStarter.class);

    public static void main(String[] args)
    {
//        LOGGER.info("Starting application ssa-bank");
        SpringApplication.run(BankStarter.class, args);
    }
}
