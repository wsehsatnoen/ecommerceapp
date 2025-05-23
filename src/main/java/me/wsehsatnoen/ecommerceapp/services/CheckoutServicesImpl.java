package me.wsehsatnoen.ecommerceapp.services;

import jakarta.transaction.Transactional;
import me.wsehsatnoen.ecommerceapp.dao.CartItemRepository;
import me.wsehsatnoen.ecommerceapp.dao.CartRepository;
import me.wsehsatnoen.ecommerceapp.dao.CustomerRepository;
import me.wsehsatnoen.ecommerceapp.entities.Cart;
import me.wsehsatnoen.ecommerceapp.entities.CartItem;
import me.wsehsatnoen.ecommerceapp.entities.Customer;
import me.wsehsatnoen.ecommerceapp.entities.StatusType;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServicesImpl implements CheckoutServices {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final CartItemRepository cartItemRepository;

    public CheckoutServicesImpl(CustomerRepository customerRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse checkout(PurchaseData purchaseData) {

        // retrieve data from customerRepo
        Cart cart = purchaseData.getCart();
        cart.setId(null); // Angular front end is sending an id of 0 and need the database to assign it.

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        cart.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with cartItems
        Set<CartItem> cartItems = purchaseData.getCartItems();
        cartItems.forEach(cart::add);

        // populate customer with cart
        Customer customer = purchaseData.getCustomer();
        customer.add(cart);

        // update cart status
        cart.setStatus(StatusType.ordered);

        // save to the database
        customerRepository.save(customer);
        cartRepository.save(cart);
        cartItemRepository.saveAll(cartItems);

        // return response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }


}
