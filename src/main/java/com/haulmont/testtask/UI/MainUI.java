package com.haulmont.testtask.UI;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private Controller controller;

    private Grid customerGrid;

    private Grid orderGrid;


    @Override
    protected void init(VaadinRequest request) {
        controller = new Controller();
        initCustomerGrid();
        initOrderGrid();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.addComponents(customerGrid, orderGrid);
        setContent(layout);
    }

    private void initOrderGrid(){
        List<Order> orders = controller.getAllOrders();
        orderGrid = new Grid();
        orderGrid.setWidth("100%");
        orderGrid.addColumn("id", Long.class);
        orderGrid.addColumn("customerId", Long.class);
        orderGrid.addColumn("description", String.class);
        orderGrid.addColumn("start", Date.class);
        orderGrid.addColumn("end", Date.class);
        orderGrid.addColumn("cost", Double.class);
        orderGrid.addColumn("status", String.class);
        for(Order order:orders){
            orderGrid.addRow(
                    order.getId(),
                    order.getCustomer().getId(),
                    order.getDescription(),
                    order.getStart(),
                    order.getEnd(),
                    order.getCost(),
                    order.getStatus());
        }
    }

    private void initCustomerGrid(){
        List<Customer> customers = controller.getAllCustomers();
        customerGrid = new Grid(new BeanItemContainer<>(Customer.class,
                customers));
        customerGrid.setWidth("80%");
        customerGrid.setColumns("id", "name", "sname", "fname", "phone");
    }


}