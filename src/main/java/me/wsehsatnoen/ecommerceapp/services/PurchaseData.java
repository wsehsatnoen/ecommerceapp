package me.wsehsatnoen.ecommerceapp.services;

import lombok.Data;
import me.wsehsatnoen.ecommerceapp.entities.Cart;
import me.wsehsatnoen.ecommerceapp.entities.CartItem;
import me.wsehsatnoen.ecommerceapp.entities.Customer;

import java.util.Set;

@Data
public class PurchaseData {

    private Customer customer;
    private Cart cart;
    private Set<CartItem> cartItems;

}
