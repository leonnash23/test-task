package com.haulmont.testtask.model;

import lombok.*;

import java.util.Date;

/**
 * Created by leonid on 04.04.17.
 */
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Order {
    @Getter @Setter
    private Long id;
    @Getter @Setter @NonNull
    private String description;

    @Setter @NonNull
    private Customer customer;

    @Getter @Setter @NonNull
    private Date start;
    @Getter @Setter
    private Date end;
    @Getter @Setter @NonNull
    private Double cost;
    @Getter @Setter @NonNull
    private String status;

    public Customer getCustomer() {
        return this.customer;
    }
}
