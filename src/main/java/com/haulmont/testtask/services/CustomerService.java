package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class CustomerService {
    private final Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private static volatile CustomerService instance;

    static CustomerService getInstance() {
        CustomerService localInstance = instance;
        if (localInstance == null) {
            synchronized (Connector.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CustomerService();
                }
            }
        }
        return localInstance;
    }



    public CustomerService() {
        conn = Connector.getInstance();
    }

    public Customer add(Customer customer) throws SQLException {
        if(customer.getId() == null){
            statement = conn.prepareStatement("INSERT INTO CUSTOMERS(NAME , SNAME, FNAME, PHONE) VALUES(?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customer.getName());
            statement.setString(2, customer.getSname());
            statement.setString(3, customer.getFname());
            statement.setLong(4, customer.getPhone());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            customer.setId(resultSet.getLong(1));
        } else {
            statement = conn.prepareStatement("UPDATE CUSTOMERS SET NAME=?, SNAME=?, FNAME=?, PHONE=? WHERE ID=?");
            statement.setString(1,customer.getName());
            statement.setString(2,customer.getSname());
            statement.setString(3,customer.getFname());
            statement.setLong(4,customer.getPhone());
            statement.setLong(5,customer.getId());
            statement.executeUpdate();
        }
        return customer;
    }

    Customer get(long id) throws SQLException {
        statement = conn.prepareStatement("SELECT * FROM CUSTOMERS WHERE ID = ?");
        statement.setLong(1,id);
        resultSet = statement.executeQuery();
        if(resultSet.next()){
            Customer customer = new Customer();
            customer.setId(resultSet.getLong("id"));
            customer.setName(resultSet.getString("name"));
            customer.setSname(resultSet.getString("sname"));
            customer.setFname(resultSet.getString("fname"));
            customer.setPhone(resultSet.getLong("phone"));
            return customer;
        } else {
            return null;
        }
    }

    public void remove(Customer customer) throws DeleleException, SQLException {
        try {
            statement = conn.prepareStatement("DELETE FROM CUSTOMERS WHERE ID=?");
            statement.setLong(1, customer.getId());
            statement.execute();
        } catch (java.sql.SQLIntegrityConstraintViolationException e){
            throw  new DeleleException();
        }
    }

    public List<Customer> getAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        statement = conn.prepareStatement("SELECT * FROM CUSTOMERS");
        resultSet = statement.executeQuery();
        while (resultSet.next()){
            Customer customer = new Customer();
            customer.setId(resultSet.getLong("id"));
            customer.setName(resultSet.getString("name"));
            customer.setSname(resultSet.getString("sname"));
            customer.setFname(resultSet.getString("fname"));
            customer.setPhone(resultSet.getLong("phone"));
            customers.add(customer);
        }
        return customers;
    }

    public class DeleleException extends Exception{

    }
}
