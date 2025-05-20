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
@Table ( name = "cart_items" )
public class CartItem {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column ( name = "cart_item_id" )
    private Long id;


    private Vacation vacation;
    private Set<Excursion> excursions;


    private Cart cart;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

}
