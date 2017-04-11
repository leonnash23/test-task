package com.haulmont.testtask.model;

import lombok.*;

import javax.persistence.*;

/**
 * Created by leonid on 04.04.17.
 */
@Entity
@Table(name = "customers")
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@NamedQuery(name = "Customer.getAll", query = "from Customer")
@EqualsAndHashCode
public class Customer {
    @Getter @Setter @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    @Getter @Setter @NonNull
    private  String name;
    @Column
    @Getter @Setter @NonNull
    private  String fname;
    @Column
    @Getter @Setter @NonNull
    private  String sname;
    @Column
    @Getter @Setter @NonNull
    private  Long phone;

//    @Setter
//    private Set<Order> orders = new HashSet<Order>(
//            0);
//
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "costumer")
//    public Set<Order> getOrders() {
//        return this.orders;
//    }

}
