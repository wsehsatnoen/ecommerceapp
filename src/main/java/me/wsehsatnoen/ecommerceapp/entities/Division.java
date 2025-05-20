package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
@Entity
@Table( name = "divisions" )
public class Division {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "division_id" )
    private Long id;

    @Column ( name = "division" )
    private String division_name;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    //todo assign relationships
    private Country country;
    private Long country_id;
    private Set<Customer> customers;
}
