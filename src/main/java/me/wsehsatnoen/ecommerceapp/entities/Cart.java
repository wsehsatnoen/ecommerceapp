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
@Table ( name = "carts" )
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "cart_id" )
    private Long id;

    @Column ( name = "order_tracking_number" )
    private String orderTrackingNumber;

    @Column ( name = "package_price" )
    private BigDecimal package_price;

    @Column ( name = "party_size" )
    private int party_size;

    public enum StatusType {
        pending, ordered, canceled
    }

    @Column ( name = "status" )
    private StatusType status;

    @CreationTimestamp
    @Column ( name = "create_date" )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    //todo add relationships
    private Customer customer;
    private Set<CartItem> cartItems;

}
