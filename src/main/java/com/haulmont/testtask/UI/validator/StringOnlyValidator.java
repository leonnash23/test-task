package com.haulmont.testtask.UI.validator;

import com.vaadin.data.validator.AbstractStringValidator;

/**
 * Created by leonid on 08.04.17.
 */
public class StringOnlyValidator extends AbstractStringValidator {

    public StringOnlyValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected boolean isValidValue(String s) {
        s = s.trim();
        return s.matches("[a-zA-Z-а-яА-ЯёЁ]+");
    }
}
