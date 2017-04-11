package com.haulmont.testtask.controller;

import com.haulmont.testtask.UI.MainUI;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.services.CustomerService;
import com.haulmont.testtask.services.OrderService;

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
        return customerService.getAll();
    }

    public List<Order> getAllOrders(){
        return  orderService.getAll();
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
        customer = customerService.add(customer);
        mainUI.updateCustomerGrid(customer,false);
        return customer;
    }

    public void removeCustomer(Customer customer) throws CustomerService.DeleleException {
        customerService.remove(customer);
        mainUI.updateCustomerGrid(customer, true);
    }

    public Order getOrderById(Long id){
        return orderService.get(id);
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
            order = orderService.add(order);
            mainUI.updateOrderGrid(order,false);
            return order;
    }

    public void removeOrder(Order order) {
        orderService.remove(order);
        mainUI.updateOrderGrid(order, true);
    }
}
