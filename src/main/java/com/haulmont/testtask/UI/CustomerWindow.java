package com.haulmont.testtask.UI;

import com.haulmont.testtask.UI.validator.PhoneValidator;
import com.haulmont.testtask.UI.validator.StringOnlyValidator;
import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.haulmont.testtask.services.CustomerService;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by leonid on 07.04.17.
 */
class CustomerWindow extends Window {

    private final Controller controller;

    private Label labelName;
    private Label labelSname;
    private Label labelFname;
    private Label labelPhone;

    private Label textName;
    private Label textSname;
    private Label textFname;
    private Label textPhone;

    private TextField inputName;
    private TextField inputSname;
    private TextField inputFname;
    private TextField inputPhone;

    private Button redactionButton;
    private Button cancelButton;
    private Button saveButton;
    private Button removeButton;
    




    public CustomerWindow(Controller controller, Customer customer, boolean save){
        super();
        this.controller = controller;
        Layout layout = getFieldsLayout(customer, save);
        setWidth("30%");
        setHeight("50%");
        center();
        setModal(true);
        setContent(layout);
    }


    private VerticalLayout getFieldsLayout(Customer customer, boolean save){
        HorizontalLayout buttonLayout = new HorizontalLayout();
        VerticalLayout layout = new VerticalLayout();


        buttonLayout.setSizeUndefined();
        buttonLayout.setSpacing(true);

        layout.setMargin(true);
        layout.setSpacing(true);

        labelName = createLabel("Имя:");
        labelSname = createLabel("Отчество:");
        labelFname = createLabel("Фамилия:");
        labelPhone = createLabel("Номер:");


        inputName = new TextField();
        inputSname = new TextField();
        inputFname = new TextField();
        inputPhone = new TextField();

        inputName.addValidator(new StringOnlyValidator("Имя должно содержать только буквы!"));
        inputSname.addValidator(new StringOnlyValidator("Отчество должно содержать только буквы!"));
        inputFname.addValidator(new StringOnlyValidator("Фамилия должно содержать только буквы!"));
        inputPhone.addValidator(new PhoneValidator("Некорректный номер телефона!"));


        textName = new Label();
        textSname = new Label();
        textFname = new Label();
        textPhone = new Label();

        if(customer != null){
            inputName.setValue(customer.getName());
            inputSname.setValue(customer.getSname());
            inputFname.setValue(customer.getFname());
            inputPhone.setValue(String.valueOf(customer.getPhone()));
            textName.setValue(customer.getName());
            textSname.setValue(customer.getSname());
            textFname.setValue(customer.getFname());
            textPhone.setValue(String.valueOf(customer.getPhone()));

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
                inputName.validate();
                inputSname.validate();
                inputFname.validate();
                inputPhone.validate();

                controller.updateCustomer(customer,
                            inputName.getValue(),
                            inputSname.getValue(),
                            inputFname.getValue(),
                            inputPhone.getValue()
                );



                if(customer == null){
                    close(); //TODO think of something better
                }




                textName.setValue(inputName.getValue());
                textSname.setValue(inputSname.getValue());
                textFname.setValue(inputFname.getValue());
                textPhone.setValue(inputPhone.getValue());

                replaceInputWithText(layout);
                replaceSaveCancelWithRedaction(buttonLayout);
            } catch (Validator.InvalidValueException e){
                Notification.show(e.getMessage());
            }
        });

        cancelButton = new Button("Отменить");
        cancelButton.addStyleName("danger");
        cancelButton.addClickListener((Button.ClickListener) clickEvent -> {
            if(customer == null) {
                close();
            } else {
                replaceSaveCancelWithRedaction(buttonLayout);
                replaceInputWithText(layout);
            }
        });


        removeButton = new Button("Удалить");
        removeButton.addStyleName("danger");
        removeButton.addClickListener((Button.ClickListener) clickEvent -> {
            try {
                controller.removeCustomer(customer);
                close();
            } catch (CustomerService.DeleleException e) {
                Notification.show("Невозможно удалить клиента, так как для него существуют заказы.");
            }
        });

        if(save) {



            buttonLayout.addComponents(cancelButton,saveButton);

            layout.addComponents(labelName,
                    inputName,
                    labelSname,
                    inputSname,
                    labelFname,
                    inputFname,
                    labelPhone,
                    inputPhone,
                    buttonLayout);
        } else {

            buttonLayout.addComponents(removeButton, redactionButton);


            layout.addComponents(labelName,
                    textName,
                    labelSname,
                    textSname,
                    labelFname,
                    textFname,
                    labelPhone,
                    textPhone,
                    buttonLayout);
        }
        

        
        return layout;
    }


    private void replaceTextWithInput(Layout layout){
        layout.replaceComponent(textName, inputName);
        layout.replaceComponent(textSname, inputSname);
        layout.replaceComponent(textFname, inputFname);
        layout.replaceComponent(textPhone, inputPhone);
    }

    private void replaceInputWithText(Layout layout){
        layout.replaceComponent(inputName, textName);
        layout.replaceComponent(inputSname, textSname);
        layout.replaceComponent(inputFname, textFname);
        layout.replaceComponent(inputPhone, textPhone);
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
