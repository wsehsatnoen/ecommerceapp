package me.wsehsatnoen.ecommerceapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import java.util.Date;
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
    @JoinColumn ( name = "division_id", nullable = false )
    private Division division;

    @OneToMany ( mappedBy = "customer" )
    private Set<Cart> carts;

    public void add(Cart cart) {
        carts.add(cart);
    }
}
