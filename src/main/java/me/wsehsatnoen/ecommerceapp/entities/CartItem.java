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

    @ManyToOne
    private Vacation vacation;

    //todo look at Udemy course 5 on how to do this one properly
    private Set<Excursion> excursions;

    @ManyToOne
    private Cart cart;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

}
