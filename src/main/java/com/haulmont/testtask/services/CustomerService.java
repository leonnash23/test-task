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

    public Customer add(Customer customer){
        manager.getTransaction().begin();
        customer = manager.merge(customer);
        manager.getTransaction().commit();
        return customer;
    }

    public Customer get(long id) {
        return manager.find(Customer.class, id);
    }

    public void remove(Customer customer) throws DeleleException {
        try {
            manager.getTransaction().begin();
            manager.remove(manager.contains(customer) ? customer : manager.merge(customer));
            manager.getTransaction().commit();
        } catch (javax.persistence.RollbackException e){
            manager.getTransaction().rollback();
            throw  new DeleleException();
        }
    }

    public List<Customer> getAll() {
        TypedQuery<Customer> namedQuery = manager.createNamedQuery("Customer.getAll", Customer.class);
        return namedQuery.getResultList();
    }

    public class DeleleException extends Exception{

    }
}
