package com.haulmont.testtask.UI;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.model.OrderStatus;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
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

        buttonLayout.addComponents(addButton);
        customerLayout.addComponents(buttonLayout, customerGrid);
    }

    private void initOrderLayout(){
        initOrderGrid();
        orderLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setMargin(new MarginInfo(false,false, true, false));
        Button addButton = new Button("Добавить");
        addButton.addStyleName("friendly");
        addButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                addWindow(new OrderWindow(controller, null, true));
            }
        });

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
        confirm.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
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
                orderGrid.getContainerDataSource().removeAllItems();
                for(Order order:newOrders){
                    updateOrderGrid(order, false);
                }
            }
        });
        buttonLayout.addComponents(addButton, customerBox, description, inputStatus, confirm);
        buttonLayout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);
        buttonLayout.setComponentAlignment(confirm, Alignment.BOTTOM_RIGHT);
        orderLayout.addComponents(buttonLayout, orderGrid);
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
            if(selectionEvent.getSelected().size()>0) {
                Item item = orderGrid.getContainerDataSource().getItem(orderGrid.getContainerDataSource()
                        .getIdByIndex(Integer.parseInt(selectionEvent.getSelected().toArray()[0].toString()) - 1));
                try {
                    addWindow(new OrderWindow(controller, controller.getOrderById((Long) item.getItemProperty("id").getValue()), false));
                } catch (SQLException e) {
                    Notification.show(e.getMessage());
                }
            }
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


    public void updateOrderGrid(Order order, boolean remove) {
        if(remove){
            orderGrid.getContainerDataSource().removeItem(orderGrid.getSelectedRow());
        } else {
            Item item = getOrderItem(order);
            if (item != null) {
                item.getItemProperty("customerId").setValue(order.getCustomer().getId());
                item.getItemProperty("description").setValue(order.getDescription());
                item.getItemProperty("start").setValue(order.getStart());
                item.getItemProperty("end").setValue(order.getEnd());
                item.getItemProperty("cost").setValue(order.getCost());
                item.getItemProperty("status").setValue(order.getStatus());
            } else {
                orderGrid.addRow(order.getId(),
                        order.getCustomer().getId(),
                        order.getDescription(),
                        order.getStart(),
                        order.getEnd(),
                        order.getCost(),
                        order.getStatus());
            }
        }
        orderGrid.clearSortOrder();
    }


    private Item getOrderItem(Order order){
        Container.Indexed containerDataSource = orderGrid.getContainerDataSource();
        for(int i=0;i<containerDataSource.size();i++){
            if(Long.valueOf((containerDataSource.getItem(containerDataSource.getIdByIndex(i)))
                    .getItemProperty("id").getValue().toString()).longValue()==order.getId().longValue()){

                return containerDataSource.getItem(containerDataSource.getIdByIndex(i));
            }
        }
        return null;
    }
}