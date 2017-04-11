package com.haulmont.testtask.UI.validator;

import com.vaadin.data.validator.AbstractStringValidator;

/**
 * Created by lech0816 on 11.04.2017.
 */
public class CostValidator extends AbstractStringValidator {
    public CostValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected boolean isValidValue(String s) {
        try {
            Double d = Double.valueOf(s);
            if(d>=0){
                return true;
            }
        } catch (NumberFormatException e){
            return false;
        }
        return false;
    }
}
