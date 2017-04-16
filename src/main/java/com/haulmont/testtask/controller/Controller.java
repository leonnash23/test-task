package com.haulmont.testtask.controller;

import com.haulmont.testtask.UI.MainUI;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.services.CustomerService;
import com.haulmont.testtask.services.OrderService;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by leonid on 07.04.17.
 */
public class Controller {

    private final CustomerService customerService;

    private final OrderService orderService;

    private final MainUI mainUI;

    public Controller(MainUI components){
        this.customerService = new CustomerService();
        this.orderService = new OrderService();
        this.mainUI = components;
    }

    public List<Customer> getAllCustomers(){
        try {
            return customerService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> getAllOrders(){
        try {
            return  orderService.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Customer updateCustomer(Customer customer,
                               String textName,
                               String textSname,
                               String textFname,
                               String textPhone) {
        if(customer == null){
            customer = new Customer();
        }
        customer.setName(textName);
        customer.setSname(textSname);
        customer.setFname(textFname);
        customer.setPhone(Long.valueOf(textPhone));
        try {
            customer = customerService.add(customer);
            mainUI.updateCustomerGrid(customer,false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public void removeCustomer(Customer customer) throws CustomerService.DeleleException {
        try {
            customerService.remove(customer);
            mainUI.updateCustomerGrid(customer, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order updateOrder(Order order,
                             Customer customer,
                             String description,
                             Date start,
                             Date end,
                             String cost,
                             String status) {
        if(order == null){
            order = new Order();
        }
        order.setCustomer(customer);
        order.setDescription(description);
        order.setStart(start);
        order.setEnd(end);
        order.setCost(Double.valueOf(cost));
        order.setStatus(status);
        try {
            order = orderService.add(order);
            mainUI.updateOrderGrid(order,false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public void removeOrder(Order order) {
        try {
            orderService.remove(order);
            mainUI.updateOrderGrid(order, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
