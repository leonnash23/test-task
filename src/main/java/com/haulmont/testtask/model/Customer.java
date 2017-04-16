package com.haulmont.testtask.model;

import lombok.*;

/**
 * Created by leonid on 04.04.17.
 */
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Customer {
    @Getter @Setter @NonNull
    private Long id;
    @Getter @Setter @NonNull
    private  String name;
    @Getter @Setter @NonNull
    private  String fname;
    @Getter @Setter @NonNull
    private  String sname;
    @Getter @Setter @NonNull
    private  Long phone;


}
