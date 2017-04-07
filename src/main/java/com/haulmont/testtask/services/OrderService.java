package com.haulmont.testtask.services;


import com.haulmont.testtask.model.Order;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by leonid on 04.04.17.
 */
public class OrderService {
    private final EntityManager manager = Persistence
            .createEntityManagerFactory("COLIBRI")
            .createEntityManager();
    public Order get(long id){
        return manager.find(Order.class, id);
    }
    public List<Order> getAll(){
        TypedQuery<Order> namedQuery = manager.createNamedQuery("Order.getAll", Order.class);
        return namedQuery.getResultList();
    }
    public Order add(Order order){
        manager.getTransaction().begin();
        order = manager.merge(order);
        manager.getTransaction().commit();
        return order;
    }
}
