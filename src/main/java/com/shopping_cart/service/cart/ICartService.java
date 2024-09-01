package com.shopping_cart.service.cart;

import com.shopping_cart.model.Cart;
import com.shopping_cart.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Cart initializeNewCart(User user);
    Cart getCartByUserId(Long userId);
}
