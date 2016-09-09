package org.ssa.ironyard.bank.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.ssa.ironyard.bank.model.DomainObject;

public interface ORM<T extends DomainObject>
{
    String projection();
    String table();
    T map(ResultSet results) throws SQLException;
    String prepareInsert();
    String prepareUpdate();
    
    default String prepareReadAll()
    {
        return " SELECT " + projection() + " FROM " + table();
    }
    default String prepareSimpleQuery(String field)
    {
        return " SELECT " + projection() + " FROM " + table() + " WHERE " + field + " = ? ";    
    }
    default String prepareRead()
    {
        return " SELECT " + projection() + " FROM " + table() + "WHERE id = ? ";
    }
    default String prepareDelete()
    {
        return " DELETE FROM " + table() + " WHERE id = ? ";
    }
}
