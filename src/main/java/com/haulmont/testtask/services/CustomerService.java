package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class CustomerService {
    private final Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private static volatile CustomerService instance;

    public static CustomerService getInstance() {
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

    public Customer add(Customer customer){
//        manager.getTransaction().begin();
//        customer = manager.merge(customer);
//        manager.getTransaction().commit();
        return customer;
    }

    public Customer get(long id) throws SQLException {
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

    public void remove(Customer customer) throws DeleleException {
//        try {
//            manager.getTransaction().begin();
//            manager.remove(manager.contains(customer) ? customer : manager.merge(customer));
//            manager.getTransaction().commit();
//        } catch (javax.persistence.RollbackException e){
//            manager.getTransaction().rollback();
//            throw  new DeleleException();
//        }
    }

    public List<Customer> getAll() {
//        TypedQuery<Customer> namedQuery = manager.createNamedQuery("Customer.getAll", Customer.class);
//        return namedQuery.getResultList();
        return null;
    }

    public class DeleleException extends Exception{

    }
}
