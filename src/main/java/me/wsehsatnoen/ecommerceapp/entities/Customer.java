package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table ( name = "customers" )
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column ( name = "customer_id" )
    private Long id;

    @Column ( name = "customer_first_name", nullable = false, length = 50 )
    private String firstName;

    @Column ( name = "customer_last_name", nullable = false, length = 50 )
    private String lastName;

    @Column ( name = "address", nullable = false, length = 50 )
    private String address;

    @Column ( name = "postal_code", nullable = false, length = 50 )
    private String postal_code;

    @Column ( name = "phone", nullable = false, length = 50 )
    private String phone;

    @CreationTimestamp
    @Column ( name = "create_date", updatable = false )
    private Date create_date;

    @UpdateTimestamp
    @Column ( name = "last_update" )
    private Date last_update;

    @ManyToOne
    @JoinColumn ( name = "division_id" )
    private Division division;

    @OneToMany ( mappedBy = "customer" )
    private Set<Cart> carts;

    public void add(Cart cart) {
        if (cart != null) {
            if (carts == null) {
                carts = new HashSet<>();
            }
            carts.add(cart);
            cart.setCustomer(this);
        }
    }


    // constructor for bootstrap data
    public Customer(String firstName, String lastName, String address, String postal_code, String phone, Division division) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.postal_code = postal_code;
        this.phone = phone;
        this.division = division;
    }
}
