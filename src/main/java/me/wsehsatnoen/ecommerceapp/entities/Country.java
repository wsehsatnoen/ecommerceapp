package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@Table( name = "countries" )
public class Country {

    @Id
    @Column ( name = "country_id" )
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column ( name = "country" )
    private String country_name;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    //todo needs relationship
    private Set<Division> divisions;
}
