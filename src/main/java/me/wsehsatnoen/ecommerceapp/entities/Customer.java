package me.wsehsatnoen.ecommerceapp.entities;

import java.util.Date;
import java.util.Set;

public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String postal_code;
    private String phone;
    private Date create_date;
    private Date last_update;
    private Division division;
    private Set<Cart> carts;
}
