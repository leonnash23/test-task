package com.haulmont.testtask.model;

/**
 * Created by leonid on 04.04.17.
 */
public enum OrderStatus {
    PLANNED("PLANNED"), COMPLETED("COMPLETED"), ACCEPTED_BY_CLIENT("ACCEPTED_BY_CLIENT");
    private final String text;


    OrderStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
