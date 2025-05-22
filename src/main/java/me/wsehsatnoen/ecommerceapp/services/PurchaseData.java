package me.wsehsatnoen.ecommerceapp.services;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.wsehsatnoen.ecommerceapp.entities.Cart;
import me.wsehsatnoen.ecommerceapp.entities.CartItem;
import me.wsehsatnoen.ecommerceapp.entities.Customer;

import java.util.Set;

@Getter
@Setter
public class PurchaseData {

    private Customer customer;
    private Cart cart;
    private Set<CartItem> cartItems;

}
