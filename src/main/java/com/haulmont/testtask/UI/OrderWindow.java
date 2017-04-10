package com.haulmont.testtask.UI;

import com.haulmont.testtask.UI.validator.PhoneValidator;
import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.model.OrderStatus;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.List;

/**
 * Created by leonid on 07.04.17.
 */
public class OrderWindow extends Window {

    private Controller controller;

    private Order order;

    private Label labelCustomer;
    private Label labelDescription;
    private Label labelStart;
    private Label labelEnd;
    private Label labelCost;
    private Label labelStatus;

    private Label textCustomer;
    private Label textDecription;
    private Label textStart;
    private Label textEnd;
    private Label textCost;
    private Label textStatus;

    private ComboBox inputCustomer;
    private TextField inputDescription;
    private DateField inputStart;
    private DateField inputEnd;
    private TextField inputCost;

    private ComboBox inputStatus;
    private Button redactionButton;
    private Button cancelButton;
    private Button saveButton;
    private Button removeButton;



    public OrderWindow(Controller controller, Order order, boolean save){
        super();
        this.controller = controller;
        this.order = order;
        Layout layout = getFieldsLayout(order, save);
        setWidth("30%");
        setHeight("50%");
        center();
        setModal(true);
        setContent(layout);
    }


    private VerticalLayout getFieldsLayout(Order order, boolean save){
        HorizontalLayout buttonLayout = new HorizontalLayout();
        VerticalLayout layout = new VerticalLayout();


        buttonLayout.setSizeUndefined();
        buttonLayout.setSpacing(true);

        layout.setMargin(true);
        layout.setSpacing(true);

        labelCustomer = createLabel("Клиент:");
        labelDescription = createLabel("Описание:");
        labelStart = createLabel("Создание заказа:");
        labelEnd = createLabel("Завершение заказа:");
        labelCost = createLabel("Стоимость:");
        labelStatus = createLabel("Статус");


        inputCustomer = new ComboBox();
        inputCustomer.setFilteringMode(FilteringMode.CONTAINS);
        List<Customer> customerList = controller.getAllCustomers();
        for(Customer customer:customerList) {
            inputCustomer.getContainerDataSource().addItem(customer);
        }
        inputDescription = new TextField();
        inputStart = new DateField();
        inputStart.setResolution(Resolution.MINUTE);
        inputEnd = new DateField();
        inputEnd.setResolution(Resolution.MINUTE);
        inputCost = new TextField();
        inputCost.addValidator(new PhoneValidator("Некорректная стоимость!"));
        inputStatus = new ComboBox();
        inputStatus.addItem(OrderStatus.PLANNED);
        inputStatus.addItem(OrderStatus.COMPLETED);
        inputStatus.addItem(OrderStatus.ACCEPTED_BY_CLIENT);


        textCustomer = new Label();
        textDecription = new Label();
        textStart = new Label();
        textEnd = new Label();
        textCost = new Label();
        textStatus = new Label();



//        if(customer != null){
//            inputCustomer.setValue(customer.getName());
//            inputDescription.setValue(customer.getSname());
//            inputStart.setValue(customer.getFname());
//            inputEnd.setValue(String.valueOf(customer.getPhone()));
//            textCustomer.setValue(customer.getName());
//            textDecription.setValue(customer.getSname());
//            textStart.setValue(customer.getFname());
//            textEnd.setValue(String.valueOf(customer.getPhone()));
//
//        }


        redactionButton = new Button("Редактировать");
        redactionButton.addStyleName("primary");
        redactionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                replaceTextWithInput(layout);
                replaceRedactionWithSaveCancel(buttonLayout);
            }
        });
        saveButton = new Button("Сохранить");
        saveButton.addStyleName("friendly");
        saveButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                try {
                    inputCost.validate();

//                    controller.updateCustomer(customer,
//                                inputCustomer.getValue(),
//                                inputDescription.getValue(),
//                                inputStart.getValue(),
//                                inputEnd.getValue()
//                    );



//                    if(customer == null){
//                        close(); //TODO think of something better
//                    }




//                    textCustomer.setValue(inputCustomer.getValue());
                    textDecription.setValue(inputDescription.getValue());
//                    textEnd.setValue(inputEnd.getValue());

                    replaceInputWithText(layout);
                    replaceSaveCancelWithRedaction(buttonLayout);
                } catch (Validator.InvalidValueException e){
                    Notification.show(e.getMessage());
                }
            }
        });

        cancelButton = new Button("Отменить");
        cancelButton.addStyleName("danger");
        cancelButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });


        removeButton = new Button("Удалить");
        removeButton.addStyleName("danger");
        removeButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
//                try {
//                    controller.removeCustomer(customer);
//                    close();
//                } catch (CustomerService.DeleleException e) {
//                    Notification.show("Невозможно удалить клиента, так как для него существуют заказы.");
//                }
            }
        });

        if(save) {



            buttonLayout.addComponents(cancelButton,saveButton);

            layout.addComponents(labelCustomer,
                    inputCustomer,
                    labelDescription,
                    inputDescription,
                    labelStart,
                    inputStart,
                    labelEnd,
                    inputEnd,
                    labelCost,
                    inputCost,
                    labelStatus,
                    inputStatus,
                    buttonLayout);
        } else {

            buttonLayout.addComponents(removeButton, redactionButton);


            layout.addComponents(labelCustomer,
                    textCustomer,
                    labelDescription,
                    textDecription,
                    labelStart,
                    textStart,
                    labelEnd,
                    textEnd,
                    labelCost,
                    textCost,
                    labelStatus,
                    textStatus,
                    buttonLayout);
        }
        

        
        return layout;
    }


    private void replaceTextWithInput(Layout layout){
        layout.replaceComponent(textCustomer, inputCustomer);
        layout.replaceComponent(textDecription, inputDescription);
        layout.replaceComponent(textStart, inputStart);
        layout.replaceComponent(textEnd, inputEnd);
        layout.replaceComponent(textCost, inputCost);
        layout.replaceComponent(textStatus, inputStatus);
    }

    private void replaceInputWithText(Layout layout){
        layout.replaceComponent(inputCustomer, textCustomer);
        layout.replaceComponent(inputDescription, textDecription);
        layout.replaceComponent(inputStart, textStart);
        layout.replaceComponent(inputEnd, textEnd);
    }

    private void replaceRedactionWithSaveCancel(Layout layout){
        layout.replaceComponent(removeButton,cancelButton);
        layout.replaceComponent(redactionButton, saveButton);
    }

    private void replaceSaveCancelWithRedaction(Layout layout){
        layout.replaceComponent(cancelButton,removeButton);
        layout.replaceComponent(saveButton, redactionButton);
    }

    private Label createLabel(String text){

        return new Label("<b>"+text+"</b>", ContentMode.HTML);
    }
}
