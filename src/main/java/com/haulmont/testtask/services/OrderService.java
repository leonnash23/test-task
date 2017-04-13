package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Order;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class OrderService {
    private final Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private CustomerService customerService;

    private static volatile OrderService instance;

    public static OrderService getInstance() {
        OrderService localInstance = instance;
        if (localInstance == null) {
            synchronized (Connector.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new OrderService();
                }
            }
        }
        return localInstance;
    }


    public OrderService() {
        conn = Connector.getInstance();
        customerService = CustomerService.getInstance();
    }

    public Order get(long id) throws SQLException {
        Order order;
        statement = conn.prepareStatement("SELECT * from PUBLIC.ORDERS WHERE ID = ?");
        statement.setLong(1,id);
        resultSet = statement.executeQuery();
        if(resultSet.next()){
            order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setDescription(resultSet.getString("DESCRIPTION"));
            order.setStart(resultSet.getDate("start"));
            order.setEnd(resultSet.getDate("ENDDATE"));
            order.setCustomer(customerService.get(resultSet.getLong("CUSTOMER")));
            order.setCost(resultSet.getDouble("cost"));
            order.setStatus(resultSet.getString("status"));
            return order;
        }
        return null;
    }

    public List<Order> getAll(){
//        TypedQuery<Order> namedQuery = manager.createNamedQuery("Order.getAll", Order.class);
//        return namedQuery.getResultList();
        return null;
    }
    public Order add(Order order){
        if(order.getId() != null){

        } else {

        }
        return order;
    }

    public void remove(Order order) {
//        manager.getTransaction().begin();
//        manager.remove(order);
//        manager.getTransaction().commit();
    }
}
