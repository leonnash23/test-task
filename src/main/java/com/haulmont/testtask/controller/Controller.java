package com.haulmont.testtask.controller;

import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.services.CustomerService;
import com.haulmont.testtask.services.OrderService;

import java.util.List;

/**
 * Created by leonid on 07.04.17.
 */
public class Controller {

    private final CustomerService customerService;

    private final OrderService orderService;

    public Controller(){
        this.customerService = new CustomerService();
        this.orderService = new OrderService();
    }

    public List<Customer> getAllCustomers(){
        return customerService.getAll();
    }

    public List<Order> getAllOrders(){
        return  orderService.getAll();
    }


}
