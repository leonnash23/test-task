package com.haulmont.testtask.UI;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Date;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    private Controller controller;

    private Grid customerGrid;

    private Grid orderGrid;

    private VerticalLayout customerLayout;


    @Override
    protected void init(VaadinRequest request) {
        controller = new Controller(this);
        initCustomerLayout();
        initOrderGrid();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.addComponents(customerLayout, orderGrid);
        setContent(layout);
    }

    private void initCustomerLayout() {
        initCustomerGrid();
        customerLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(new MarginInfo(false,false, true, false));
        Button addButton = new Button("Добавить");
        addButton.addStyleName("friendly");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addWindow(new CustomerWindow(controller, null, true));
            }
        });

//        Button removeButton = new Button("Удалить");
//        removeButton.addStyleName("danger");
//        removeButton.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent clickEvent) {
//                if(customerGrid.getSelectedRow() != null){
//                    controller.removeCustomer((Customer) customerGrid.getSelectedRow());
//                }
//            }
//        });

        buttonLayout.addComponents(addButton);
        customerLayout.addComponents(buttonLayout, customerGrid);
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
        orderGrid.addSelectionListener((SelectionEvent.SelectionListener) selectionEvent -> {
           Item item = orderGrid.getContainerDataSource().getItem(orderGrid.getContainerDataSource()
                   .getIdByIndex(Integer.parseInt(selectionEvent.getSelected().toArray()[0].toString())-1));
            addWindow(new OrderWindow(controller, controller.getOrderById((Long) item.getItemProperty("id").getValue()), false));
        });
    }

    private void initCustomerGrid(){
        List<Customer> customers = controller.getAllCustomers();
        customerGrid = new Grid(new BeanItemContainer<>(Customer.class,
                customers));
        customerGrid.setWidth("80%");
        customerGrid.setColumns("id", "name", "sname", "fname", "phone");
        customerGrid.addSelectionListener((SelectionEvent.SelectionListener) selectionEvent -> {
            addWindow(new CustomerWindow(controller, (Customer) selectionEvent.getSelected().toArray()[0], false));
        });
    }

    public void updateCustomerGrid(Customer customer, boolean remove){
        if(remove){
            customerGrid.getContainerDataSource().removeItem(customer);
        } else {
            Item item = customerGrid.getContainerDataSource().getItem(customer);
            if (item != null) {
                item.addItemProperty("name", new ObjectProperty<>(customer.getName()));
                item.addItemProperty("sname", new ObjectProperty<>(customer.getSname()));
                item.addItemProperty("fname", new ObjectProperty<>(customer.getFname()));
                item.addItemProperty("phone", new ObjectProperty<>(customer.getPhone()));
            } else {
                customerGrid.getContainerDataSource().addItem(customer);
            }
        }
        customerGrid.clearSortOrder();
    }



}