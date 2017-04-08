package com.haulmont.testtask.controller;

import com.haulmont.testtask.UI.MainUI;
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

    private final MainUI mainUI;

    public Controller(MainUI components){
        this.customerService = new CustomerService();
        this.orderService = new OrderService();
        this.mainUI = components;
    }

    public List<Customer> getAllCustomers(){
        return customerService.getAll();
    }

    public List<Order> getAllOrders(){
        return  orderService.getAll();
    }


    public void updateCustomer(Customer customer,
                               String textName,
                               String textSname,
                               String textFname,
                               String textPhone) {
        customer.setName(textName);
        customer.setSname(textSname);
        customer.setFname(textFname);
        customer.setPhone(Long.valueOf(textPhone));
        customerService.add(customer);
        mainUI.updateCustomerGrid(customer);
    }

}
