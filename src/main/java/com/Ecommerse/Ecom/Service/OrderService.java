package com.Ecommerse.Ecom.Service;

import com.Ecommerse.Ecom.Dto.OrderCreatedEvent;
import com.Ecommerse.Ecom.Dto.OrderItemDTO;
import com.Ecommerse.Ecom.Dto.OrderResponse;
import com.Ecommerse.Ecom.Model.CartItem;
import com.Ecommerse.Ecom.Model.Order;
import com.Ecommerse.Ecom.Model.OrderItem;
import com.Ecommerse.Ecom.Model.OrderStatus;
import com.Ecommerse.Ecom.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
//    private final StreamBridge streamBridge;

    public Optional<OrderResponse> createOrder(String userId) {
        // Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            return Optional.empty();
        }
//        // Validate for user
//
//        Optional<User> userOptional = userRepository.findById(Long.valueOf(userId));
//        if (userOptional.isEmpty()) {
//            return Optional.empty();
//        }
//        User user = userOptional.get();

        // Calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);

        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(userId);

        // Publish order created event
        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getStatus(),
                mapToOrderItemDTOs(savedOrder.getItems()),
                savedOrder.getTotalAmount(),
                savedOrder.getCreatedAt()
        );
//        streamBridge.send("createOrder-out-0", event);

        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private List<OrderItemDTO> mapToOrderItemDTOs(List<OrderItem> items) {
        return items.stream()
                .map(item -> new OrderItemDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                )).collect(Collectors.toList());
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream()
                        .map(orderItem -> new OrderItemDTO(
                                orderItem.getId(),
                                orderItem.getProduct().getId(),
                                orderItem.getQuantity(),
                                orderItem.getPrice(),
                                orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                        ))
                        .toList(),
                order.getCreatedAt()
        );
    }
}