package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@Table ( name = "vacations" )
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "vacation_id" )
    private Long id;

    @Column ( name = "vacation_title" )
    private String vacation_title;

    @Column ( name = "description" )
    private String description;

    @Column ( name = "travel_fare_price" )
    private BigDecimal travel_price;

    @Column ( name = "image_url" )
    private String image_URL;

    @CreationTimestamp
    @Column ( name = "create_date", updatable = false )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    @OneToMany ( mappedBy = "vacation" )
    private Set<Excursion> excursions;

}
