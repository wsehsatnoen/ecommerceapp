package me.wsehsatnoen.ecommerceapp.services;

import jakarta.transaction.Transactional;
import me.wsehsatnoen.ecommerceapp.dao.CustomerRepository;
import me.wsehsatnoen.ecommerceapp.entities.Cart;
import me.wsehsatnoen.ecommerceapp.entities.CartItem;
import me.wsehsatnoen.ecommerceapp.entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
public class CheckoutServicesImpl implements CheckoutServices {

    private CustomerRepository customerRepository;

    @Autowired
    public CheckoutServicesImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public PurchaseResponse checkout(PurchaseData purchaseData) {

        // retrieve data from customerRepo
        Cart cart = purchaseData.getCart();

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

        // return response
        return new PurchaseResponse(orderTrackingNumber);
    }

    private String generateOrderTrackingNumber() {
        return UUID.randomUUID().toString();
    }


}
