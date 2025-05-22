package me.wsehsatnoen.ecommerceapp.services;

import jakarta.transaction.Transactional;
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

    public CheckoutServicesImpl(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
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

        // save to database
        customerRepository.save(customer);
        cart.setStatus(StatusType.ordered);
        cartRepository.save(cart);

        // return response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }


}
