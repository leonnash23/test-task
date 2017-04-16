package com.haulmont.testtask.UI;

import com.haulmont.testtask.UI.validator.CostValidator;
import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.model.Order;
import com.haulmont.testtask.model.OrderStatus;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.List;

/**
 * Created by leonid on 07.04.17.
 */
class OrderWindow extends Window {

    private final Controller controller;

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



    public OrderWindow(Controller controller, Order order, boolean save) {
        super();
        this.controller = controller;
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
        inputCustomer.addValidator(new NullValidator("Поле \"Клиент\" должно быть заполненно", false));
        inputCustomer.setSizeFull();
        inputDescription = new TextField();
        inputDescription.setSizeFull();
        inputDescription.addValidator(new NullValidator("Поле \"Описание\" должно быть заполненно", false));
        inputStart = new DateField();
        inputStart.setResolution(Resolution.MINUTE);
        inputStart.addValidator(new NullValidator("Поле \"Создание заказа\" должно быть заполненно", false));
        inputEnd = new DateField();
        inputEnd.setResolution(Resolution.MINUTE);
        inputCost = new TextField();
        inputCost.addValidator(new CostValidator("Некорректная стоимость!"));
        inputStatus = new ComboBox();
        inputStatus.addItem(OrderStatus.PLANNED.toString());
        inputStatus.addItem(OrderStatus.COMPLETED.toString());
        inputStatus.addItem(OrderStatus.ACCEPTED_BY_CLIENT.toString());
        inputStatus.addValidator(new NullValidator("Поле \"Статус\" должно быть заполненно", false));


        textCustomer = new Label();
        textDecription = new Label();
        textStart = new Label();
        textEnd = new Label();
        textCost = new Label();
        textStatus = new Label();

        if(order != null){
            textCustomer.setValue(order.getCustomer().toString());
            textDecription.setValue(order.getDescription());
            textStart.setValue(order.getStart().toString());
            if(order.getEnd() != null) {
                textEnd.setValue(order.getEnd().toString());
                inputEnd.setValue(order.getEnd());
            }
            textCost.setValue(String.valueOf(order.getCost()));
            textStatus.setValue(order.getStatus());

            inputCustomer.select(order.getCustomer());
            inputDescription.setValue(order.getDescription());
            inputStart.setValue(order.getStart());
            inputCost.setValue(String.valueOf(order.getCost()));
            inputStatus.setValue(order.getStatus());
        }





        redactionButton = new Button("Редактировать");
        redactionButton.addStyleName("primary");
        redactionButton.addClickListener((Button.ClickListener) clickEvent -> {
            replaceTextWithInput(layout);
            replaceRedactionWithSaveCancel(buttonLayout);
        });
        saveButton = new Button("Сохранить");
        saveButton.addStyleName("friendly");
        saveButton.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                inputCustomer.validate();
                inputCost.validate();
                inputDescription.validate();
                inputStart.validate();
                inputStatus.validate();

                controller.updateOrder(order,
                        (Customer)inputCustomer.getValue(),
                            inputDescription.getValue(),
                            inputStart.getValue(),
                            inputEnd.getValue(),
                            inputCost.getValue(),
                            inputStatus.getValue().toString()
                );



                if(order == null){
                    close(); //TODO think of something better
                }




                textCustomer.setValue(String.valueOf(((Customer) inputCustomer.getValue()).getId()));
                textDecription.setValue(inputDescription.getValue());
                textStart.setValue(inputStart.getValue().toString());
                if(inputEnd.getValue()!=null) {
                    textEnd.setValue(inputEnd.getValue().toString());
                }
                textCost.setValue(inputCost.getValue());
                textStatus.setValue(inputStatus.getValue().toString());

                replaceInputWithText(layout);
                replaceSaveCancelWithRedaction(buttonLayout);
            } catch (Validator.InvalidValueException e){
                Notification.show(e.getMessage());
            }
        });

        cancelButton = new Button("Отменить");
        cancelButton.addStyleName("danger");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> {
            if(order == null) {
                close();
            } else {
                replaceInputWithText(layout);
                replaceSaveCancelWithRedaction(buttonLayout);
            }
        });


        removeButton = new Button("Удалить");
        removeButton.addStyleName("danger");
        removeButton.addClickListener((Button.ClickListener) clickEvent -> {
                controller.removeOrder(order);
                close();
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
        layout.replaceComponent(inputCost, textCost);
        layout.replaceComponent(inputStatus, textStatus);
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
