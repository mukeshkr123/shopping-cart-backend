package com.shopping_cart.service.order;

import com.shopping_cart.dto.OrderDto;
import com.shopping_cart.dto.OrderItemDto;
import com.shopping_cart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderItemDto> getUserOrderItems(Long userId);
}
