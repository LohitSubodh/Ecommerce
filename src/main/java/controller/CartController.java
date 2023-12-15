package controller;

import entity.Cart;
import entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CartService;
import service.CouponService;
import service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @GetMapping("/")
    public ResponseEntity<List<Cart>> getCarts() {
        List<Cart> carts = cartService.listCarts();
        return new ResponseEntity<List<Cart>>(carts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable("id") long id) throws ChangeSetPersister.NotFoundException {
        Cart cart = cartService.getCartById(id);
        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCart(@RequestBody  CartDto cartDto) {
        cartService.addItemToCart(cartDto.getCartId(),cartDto.getProductId(),cartDto.getQuantity());
        return new ResponseEntity<>(new ApiResponse(true, "Cart has been created."), HttpStatus.CREATED);
    }

    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable("id") long id) throws ChangeSetPersister.NotFoundException {
        cartService.deleteCart(id);
        return new ResponseEntity<>(new ApiResponse(true, "Cart has been deleted."), HttpStatus.OK);
    }
*/


    @PostMapping("/cart-checkout")
    public ResponseEntity<Order> createOrderFromCart(@RequestBody  Long cartId) {
        Order order = orderService.createOrderFromCart(cartId);
        couponService.generateCouponIfEligible(order);
        return ResponseEntity.ok(order);
    }

}
