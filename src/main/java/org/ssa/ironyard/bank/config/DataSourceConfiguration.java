package org.ssa.ironyard.bank.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author thurston
 */
@Configuration
public class DataSourceConfiguration 
{
    static final String DATABASE_URL = "jdbc:mysql://localhost/ssa_bank?user=root&password=root&" +
            "useServerPrpStmts=true";
    static final Logger LOGGER = LogManager.getLogger(DataSourceConfiguration.class);
    @Bean
    public DataSource datasource()
    {
        LOGGER.debug("YEAH ANnotation based processing is working");
        MysqlDataSource mysqlds = new MysqlDataSource();
        mysqlds.setURL(DATABASE_URL);
        return mysqlds;
    }


}