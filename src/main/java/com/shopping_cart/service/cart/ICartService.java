package com.shopping_cart.service.cart;

import com.shopping_cart.model.Cart;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
    Long initializeNewCart();
    Cart getCartByUserId(Long userId);
}
