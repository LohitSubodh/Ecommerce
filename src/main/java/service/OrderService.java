package service;

import entity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.CartRepository;
import repository.OrderRepository;

import java.util.Date;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private CouponService couponService;

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public Order createOrderFromCart(Long cartId) {
        Cart cart = cartService.getCartById(cartId);
        if (cart == null || cart.getItems().isEmpty()) {
            return null;
        }
        Order order = new Order();
        order.setUserId(cart.getUserId());
        order.setDate(new Date());
        order.setTotal(0.0);
        order.setCoupon(null);
        order.setDiscount(0.0);
        for (CartItem cartItem : ((Cart) cart).getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            order.getItems().add(orderItem);
            order.setTotal(order.getTotal() + orderItem.getPrice());
        }
        orderRepository.save(order);
        cartRepository.delete(cart);
        couponService.generateCouponIfEligible(order);
        return order;
    }
    public Order applyCouponToOrder(Long orderId, String couponCode) {
        Order order = getOrderById(orderId);
        if (order == null) {
            return null;
        }
        Coupon coupon = couponService.getCouponByCode(couponCode);
        if (coupon == null || !coupon.getValid()) {
            return null;
        }
        order.setCoupon(coupon.getCode());
        order.setDiscount(order.getTotal() * coupon.getPercentage() / 100);
        return orderRepository.save(order);
    }
}
