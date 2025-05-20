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
@Table ( name = "excursions" )
public class Excursion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "excursion_id" )
    private Long id;

    @Column ( name = "excursion_title" )
    private String excursion_title;

    @Column ( name = "excursion_price" )
    private BigDecimal excursion_price;

    @Column ( name = "image_url" )
    private String image_URL;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date created_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    //todo add relationships
    private Vacation vacation;
    private Set<CartItem> cart_items;

}
