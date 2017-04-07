package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Customer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class CustomerService {
    private final EntityManager manager = Persistence
            .createEntityManagerFactory("COLIBRI")
            .createEntityManager();

    public Customer get(long id) {
        return manager.find(Customer.class, id);
    }

    public List<Customer> getAll() {
        TypedQuery<Customer> namedQuery = manager.createNamedQuery("Customer.getAll", Customer.class);
        return namedQuery.getResultList();
    }
}
