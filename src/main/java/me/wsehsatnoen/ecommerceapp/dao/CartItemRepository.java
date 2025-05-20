package me.wsehsatnoen.ecommerceapp.dao;

import me.wsehsatnoen.ecommerceapp.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
