package me.wsehsatnoen.ecommerceapp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;


@Entity
@Getter
@Setter
@Table ( name = "cart_items" )
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column ( name = "cart_item_id" )
    private Long id;

    /// Unidirectional relationship, no need for Join column/Mapped by declarations
    @ManyToOne
    private Vacation vacation;

    @ManyToMany
    @JoinTable ( name = "excursion_cartitem",
        joinColumns = @JoinColumn ( name = "cart_item_id" ),
        inverseJoinColumns = @JoinColumn ( name = "excursion_id" ))
    private Set<Excursion> excursions;

    @ManyToOne
    @JoinColumn ( name = "cart_id" )
    private Cart cart;

    @CreationTimestamp
    @Column ( name = "create_date", updatable = false )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

}
