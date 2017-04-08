package com.haulmont.testtask.UI.validator;

import com.vaadin.data.validator.AbstractStringValidator;

/**
 * Created by leonid on 08.04.17.
 */
public class PhoneValidator extends AbstractStringValidator {
    public PhoneValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected boolean isValidValue(String s) {
        //TODO каким может быть номер? Да любым...
        return s.matches("[0-9]+");
    }
}
