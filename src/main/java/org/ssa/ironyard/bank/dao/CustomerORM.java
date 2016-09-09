package org.ssa.ironyard.bank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ssa.ironyard.bank.model.Customer;
import org.ssa.ironyard.bank.dao.ORM;

public interface CustomerORM extends ORM<Customer>
{
    default String projection()
    {
        return " id, first, last ";
    }

    default String table()
    {
        return " customers ";
    }

    default Customer map(ResultSet results) throws SQLException
    {
        return new Customer(results.getInt("id"), results.getString("first"), results.getString("last"), true);
    }

    default String prepareInsert()
    {
        return " INSERT INTO " + table() + " (first, last) VALUES (?,?) ";
    }

    default String prepareUpdate()
    {
        return " UPDATE " + table() + " SET first = ?, last = ? WHERE id = ? ";
    }
    
    default String prepareReadCustomersByFirstName()
    {
        return prepareSimpleQuery("first");
    }
    
    default String prepareReadCustomersByLastName()
    {
        return prepareSimpleQuery("last");
    }
}
