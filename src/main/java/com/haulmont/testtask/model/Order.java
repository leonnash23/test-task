package com.haulmont.testtask.model;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by leonid on 04.04.17.
 */
@Entity
@Table(name = "orders")
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@NamedQuery(name = "Order.getAll", query = "from Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Column
    @Getter @Setter @NonNull
    private String description;

    @Setter @NonNull
    private Customer customer;

    @Column
    @Type(type="timestamp")
    @Getter @Setter @NonNull
    private Date start;
    @Column(name = "enddate")
    @Type(type="timestamp")
    @Getter @Setter
    private Date end;
    @Column
    @Getter @Setter @NonNull
    private Double cost;
    @Column
    @Getter @Setter @NonNull
    private String status;

    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "CUSTOMER", nullable = false, foreignKey = @ForeignKey(name = "FOREING_KEY_FOR_CUSTOMER"))
    public Customer getCustomer() {
        return this.customer;
    }
}
