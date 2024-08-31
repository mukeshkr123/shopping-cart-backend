package com.shopping_cart.service.order;

import com.shopping_cart.dto.OrderDto;
import com.shopping_cart.dto.OrderItemDto;
import com.shopping_cart.enums.OrderStatus;
import com.shopping_cart.exceptions.ResourceNotFoundException;
import com.shopping_cart.model.*;
import com.shopping_cart.repository.OrderRepository;
import com.shopping_cart.repository.ProductRepository;
import com.shopping_cart.service.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        // Save order and update inventories in a single transaction
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            // Validate inventory before updating
            if (product.getInventory() < cartItem.getQuantity()) {
                throw new IllegalStateException("Not enough inventory for product: " + product.getName());
            }

            product.setInventory(product.getInventory() - cartItem.getQuantity());
            return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
        }).toList();

        // Save all updated products in batch to avoid multiple queries
        productRepository.saveAll(cart.getItems().stream().map(CartItem::getProduct).toList());

        return orderItems;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Ensure order items are loaded
        order.getOrderItems().size();

        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> getUserOrderItems(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        // Return OrderItemDtos for all orders
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(orderItem -> modelMapper.map(orderItem, OrderItemDto.class))
                .toList();
    }
}
