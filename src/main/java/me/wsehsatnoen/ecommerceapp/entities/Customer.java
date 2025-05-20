package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@Table ( name = "customers" )
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "customer_id" )
    private Long id;

    @Column ( name = "customer_first_name" )
    private String firstName;

    @Column ( name = "customer_last_name" )
    private String lastName;

    @Column ( name = "address" )
    private String address;

    @Column ( name = "postal_code" )
    private String postal_code;

    @Column ( name = "phone" )
    private String phone;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    //todo set relationships
    private Division division;
    private Set<Cart> carts;
}
