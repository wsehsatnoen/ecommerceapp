package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table ( name = "carts" )
public class Cart {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column ( name = "cart_id" )
    private Long id;

    @Column ( name = "order_tracking_number" )
    private String orderTrackingNumber;

    @Column ( name = "package_price" )
    private BigDecimal package_price;

    @Column ( name = "party_size" )
    private int party_size;

    @Column ( name = "status" )
    @Enumerated ( EnumType.STRING )
    private StatusType status;

    @CreationTimestamp
    @Column ( name = "create_date", updatable = false )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    @ManyToOne
    @JoinColumn ( name = "customer_id" )
    private Customer customer;

    @OneToMany ( mappedBy = "cart" )
    private Set<CartItem> cartItems = new HashSet<>();

    public void add(CartItem cartItem) {
        if (cartItem != null) {
            cartItems.add(cartItem);
            cartItem.setCart(this);
        }
    }
}
