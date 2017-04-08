package com.haulmont.testtask.UI;

import com.haulmont.testtask.UI.validator.PhoneValidator;
import com.haulmont.testtask.UI.validator.StringOnlyValidator;
import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Customer;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

/**
 * Created by leonid on 07.04.17.
 */
public class CustomerWindow extends Window {
    private Controller controller;
    private Customer customer;

    public CustomerWindow(Controller controller, Customer customer){
        super();
        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSizeUndefined();
        buttonLayout.setSpacing(true);
        layout.setMargin(true);
        layout.setSpacing(true);
        Label labelName = createLabel("Имя:");
        Label labelSname = createLabel("Отчество:");
        Label labelFname = createLabel("Фамилия:");
        Label labelPhone = createLabel("Номер:");
        Label name = new Label();
        name.setValue(customer.getName());
        Label sname = new Label();
        sname.setValue(customer.getSname());
        Label fname = new Label();
        fname.setValue(customer.getFname());
        Label phone = new Label();
        phone.setValue(String.valueOf(customer.getPhone()));
        Button redaction = new Button("Редактировать");
        redaction.addStyleName("primary");
        Button cancel = new Button("Отменить");
        cancel.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        cancel.addStyleName("danger");
        redaction.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                TextField textName = new TextField();
                textName.setValue(customer.getName());
                textName.addValidator(new StringOnlyValidator("Имя должно содержать только буквы!"));
                TextField textSname = new TextField();
                textSname.setValue(customer.getSname());
                textSname.addValidator(new StringOnlyValidator("Отчество должно содержать только буквы!"));
                TextField textFname = new TextField();
                textFname.setValue(customer.getFname());
                textFname.addValidator(new StringOnlyValidator("Фамилия должно содержать только буквы!"));
                TextField textPhone = new TextField();
                textPhone.setValue(String.valueOf(customer.getPhone()));
                textPhone.addValidator(new PhoneValidator("Некорректный номер телефона!"));
                layout.replaceComponent(name, textName);
                layout.replaceComponent(sname, textSname);
                layout.replaceComponent(fname, textFname);
                layout.replaceComponent(phone, textPhone);
                buttonLayout.removeComponent(redaction);
                buttonLayout.addComponent(cancel);
                Button save = new Button("Сохранить");
                save.addStyleName("friendly");
                save.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent clickEvent) {
                        try {
                            textName.validate();
                            textSname.validate();
                            textFname.validate();
                            textPhone.validate();
                            controller.updateCustomer(customer,
                                    textName.getValue(),
                                    textSname.getValue(),
                                    textFname.getValue(),
                                    textPhone.getValue()
                            );
                            name.setValue(textName.getValue());
                            sname.setValue(textSname.getValue());
                            fname.setValue(textFname.getValue());
                            phone.setValue(textPhone.getValue());
                            layout.replaceComponent(textName, name);
                            layout.replaceComponent(textSname, sname);
                            layout.replaceComponent(textFname, fname);
                            layout.replaceComponent(textPhone, phone);
                            buttonLayout.removeComponent(cancel);
                            buttonLayout.removeComponent(save);
                            buttonLayout.addComponent(redaction);
                        } catch (Validator.InvalidValueException e){
                            Notification.show(e.getMessage());
                        }
                    }
                });
                buttonLayout.addComponent(save);
            }
        });
        buttonLayout.addComponent(redaction);
        layout.addComponents(labelName, name, labelSname, sname,  labelFname, fname, labelPhone, phone, buttonLayout);
        setWidth("30%");
        setHeight("50%");
        center();
        setModal(true);
        setContent(layout);
    }

    private Label createLabel(String text){

        return new Label("<b>"+text+"</b>", ContentMode.HTML);
    }
}
