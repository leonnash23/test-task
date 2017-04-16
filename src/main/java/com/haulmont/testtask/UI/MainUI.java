package com.haulmont.testtask.UI;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.model.OrderStatus;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
@Title("Автомастерская")
public class MainUI extends UI {

    private Controller controller;

    private Grid customerGrid;

    private Grid orderGrid;

    private VerticalLayout customerLayout;

    private VerticalLayout orderLayout;


    @Override
    protected void init(VaadinRequest request) {
        controller = new Controller(this);
        initCustomerLayout();
        initOrderLayout();

        HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.addComponents(customerLayout, orderLayout);
        layout.setComponentAlignment(customerLayout, Alignment.BOTTOM_LEFT);
        layout.setComponentAlignment(orderLayout, Alignment.BOTTOM_RIGHT);
        setContent(layout);
    }

    private void initCustomerLayout() {
        initCustomerGrid();
        customerLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(new MarginInfo(false,false, true, false));
        customerLayout.setHeight("100%");
        Button addButton = new Button("Добавить");
        addButton.addStyleName("friendly");
        addButton.addClickListener((Button.ClickListener) clickEvent -> addWindow(new CustomerWindow(controller, null, true)));

        buttonLayout.addComponents(addButton);
        customerLayout.addComponents(buttonLayout, customerGrid);
        customerLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
        customerLayout.setExpandRatio(buttonLayout,0.1f);
        customerLayout.setExpandRatio(customerGrid,0.9f);
    }

    private void initOrderLayout(){
        initOrderGrid();
        orderLayout = new VerticalLayout();
        orderLayout.setSizeFull();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(new MarginInfo(false,false, true, false));
        Button addButton = new Button("Добавить");
        addButton.addStyleName("friendly");
        addButton.addClickListener((Button.ClickListener) clickEvent -> addWindow(new OrderWindow(controller, null, true)));

        ComboBox customerBox = new ComboBox("Клиент");
        customerBox.setFilteringMode(FilteringMode.CONTAINS);
        List<Customer> customerList = controller.getAllCustomers();
        for(Customer customer:customerList) {
            customerBox.getContainerDataSource().addItem(customer);
        }
        TextField description = new TextField("Описание");

        ComboBox inputStatus = new ComboBox("Статус");
        inputStatus.addItem(OrderStatus.PLANNED.toString());
        inputStatus.addItem(OrderStatus.COMPLETED.toString());
        inputStatus.addItem(OrderStatus.ACCEPTED_BY_CLIENT.toString());

        Button confirm = new Button("Применить");
        confirm.addStyleName("primary");
        confirm.addClickListener((Button.ClickListener) clickEvent -> {
            List<Order> orders = controller.getAllOrders();
            List<Order> newOrders = new ArrayList<>();
            if(customerBox.getValue()==null && description.getValue().trim().equals("") && inputStatus.getValue()==null){
                newOrders.addAll(orders);
            }

            for(Order order:orders){
                boolean customer = true;
                boolean descr = true;
                boolean status = true;
                if(customerBox.getValue() != null && !order.getCustomer().equals(customerBox.getValue())){
                   customer = false;
                }
                if(!description.getValue().trim().equals("") && !order.getDescription().toLowerCase()
                       .contains(description.getValue().toLowerCase().trim())){
                   descr = false;
                }
                if(inputStatus.getValue() != null && !order.getStatus().equals(inputStatus.getValue().toString())){
                   status = false;
                }
                if(customer && descr && status){
                    newOrders.add(order);
                }

            }
            orderGrid.setContainerDataSource(new BeanItemContainer<>(Order.class, newOrders));
        });
        buttonLayout.addComponents(addButton, customerBox, description, inputStatus, confirm);
        buttonLayout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
        buttonLayout.setComponentAlignment(confirm, Alignment.BOTTOM_RIGHT);
        orderLayout.addComponents(buttonLayout, orderGrid);
        orderLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        orderLayout.setExpandRatio(buttonLayout, 0.1f);
        orderLayout.setExpandRatio(orderGrid, 0.9f);
    }


    private void initOrderGrid(){
        List<Order> orders = controller.getAllOrders();
        orderGrid = new Grid();
        orderGrid.setWidth("100%");
        orderGrid.setSizeFull();
        orderGrid.setHeightMode(HeightMode.CSS);
        orderGrid.setHeight("100%");
        orderGrid.addColumn("id", Long.class);
        orderGrid.addColumn("customer", Customer.class);
        orderGrid.getColumn("customer").setMaximumWidth(150);
        orderGrid.addColumn("description", String.class);
        orderGrid.addColumn("start", Date.class);
        orderGrid.addColumn("end", Date.class);
        orderGrid.addColumn("cost", Double.class);
        orderGrid.addColumn("status", String.class);

        orderGrid.setContainerDataSource(new BeanItemContainer<>(Order.class, orders));

        orderGrid.addSelectionListener((SelectionEvent.SelectionListener) selectionEvent -> {
            if(selectionEvent.getSelected().size()>0) {
                addWindow(new OrderWindow(controller, (Order) orderGrid.getSelectedRow(), false));
                orderGrid.deselectAll();
            }
        });
    }



    private void initCustomerGrid(){
        List<Customer> customers = controller.getAllCustomers();
        customerGrid = new Grid(new BeanItemContainer<>(Customer.class,
                customers));
        customerGrid.setWidth("80%");
        customerGrid.setHeight("100%");
        customerGrid.setColumns("id", "name", "sname", "fname", "phone");
        customerGrid.addSelectionListener((SelectionEvent.SelectionListener) selectionEvent -> {
            if(selectionEvent.getSelected().size()>0) {
                addWindow(new CustomerWindow(controller, (Customer) customerGrid.getSelectedRow(), false));
                customerGrid.deselectAll();
            }
        });
    }

    public void updateCustomerGrid(Customer customer, boolean remove){
        if(remove){
            customerGrid.getContainerDataSource().removeItem(customer);
        } else {
            List<Customer> customers = controller.getAllCustomers();
            customerGrid.setContainerDataSource(new BeanItemContainer<>(Customer.class, customers));
        }
        customerGrid.clearSortOrder();
    }

    public void updateOrderGrid(Order order, boolean remove) {
        if(remove){
            orderGrid.getContainerDataSource().removeItem(order);
        } else {
           List<Order> orders = controller.getAllOrders();
           orderGrid.setContainerDataSource(new BeanItemContainer<>(Order.class,orders));
        }
        orderGrid.clearSortOrder();
    }


}