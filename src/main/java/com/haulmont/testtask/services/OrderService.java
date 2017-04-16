package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class OrderService {
    private final Connection conn;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private final CustomerService customerService;

    private static volatile OrderService instance;

    static OrderService getInstance() {
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

    Order get(long id) throws SQLException {
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

    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        statement = conn.prepareStatement("SELECT * FROM ORDERS");
        resultSet = statement.executeQuery();
        while (resultSet.next()){
            Order order = new Order();
            order.setId(resultSet.getLong("id"));
            order.setCustomer(customerService.get(resultSet.getLong("CUSTOMER")));
            order.setDescription(resultSet.getString("DESCRIPTION"));
            order.setStart(resultSet.getDate("start"));
            order.setEnd(resultSet.getDate("enddate"));
            order.setCost(resultSet.getDouble("cost"));
            order.setStatus(resultSet.getString("status"));
            orders.add(order);
        }
        return orders;
    }

    public Order add(Order order) throws SQLException {
        if(order.getId() == null){
            statement = conn.prepareStatement("INSERT INTO ORDERS (DESCRIPTION, CUSTOMER, START, ENDDATE, COST, STATUS)" +
                    " VALUES(?,?,?,?,?,?)",java.sql.Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, order.getDescription());
            statement.setLong(2, order.getCustomer().getId());
            statement.setDate(3, new Date(order.getStart().getTime()));
            if(order.getEnd() != null) {
                statement.setDate(4, new Date(order.getEnd().getTime()));
            } else {
                statement.setDate(4, null);
            }
            statement.setDouble(5, order.getCost());
            statement.setString(6, order.getStatus());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            order.setId(resultSet.getLong(1));
        } else {
            statement = conn.prepareStatement("UPDATE ORDERS SET DESCRIPTION=?,CUSTOMER=?," +
                    " START=?, ENDDATE=?, COST=?, STATUS=? WHERE ID=?");
            statement.setString(1, order.getDescription());
            statement.setLong(2, order.getCustomer().getId());
            statement.setDate(3, new Date(order.getStart().getTime()));
            if(order.getEnd() != null) {
                statement.setDate(4, new Date(order.getEnd().getTime()));
            } else {
                statement.setDate(4, null);
            }
            statement.setDouble(5, order.getCost());
            statement.setString(6, order.getStatus());
            statement.setLong(7, order.getId());
            statement.execute();
        }
        return order;
    }

    public void remove(Order order) throws SQLException {
        statement = conn.prepareStatement("DELETE FROM ORDERS WHERE ID=?");
        statement.setLong(1, order.getId());
        statement.execute();
    }
}
