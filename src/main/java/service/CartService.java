package service;

import controller.CartDto;
import entity.Cart;
import entity.CartItem;
import entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import repository.CartRepository;
import repository.ProductRepository;

import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        return cartRepository.save(cart);
    }

    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElse(null);
    }

    public Cart addItemToCart(Long cartId, Long productId, Integer quantity) {
        Cart cart = getCartById(cartId);
        if (cart == null) {
            return null;
        }
        Product product = productService.getProductById(productId);
        if (product == null) {
            return null;
        }
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        cart.getItems().add(item);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = getCartById(cartId);
        if (cart == null) {
            return null;
        }
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElse(null);
        if (item == null) {
            return null;
        }
        cart.getItems().remove(item);
        return cartRepository.save(cart);
    }

    public List<Cart> listCarts() {
        return cartRepository.findAll();
    }

    /*
    public void addCart(CartDto cartDto) {
        // get the user id and product id from the request body
        Long userId = cartDto.getUserId();
        Long productId = cartDto.getProductId();
        // find the product by id
        Product product = productRepository.findById(productId).orElse(null);
        // check if the product exists
        if (product == null) {
            // throw an exception if the product is not found
            System.out.println("not found");
        }
        // create a new cart for the user
        Cart cart = new Cart();
        cart.setUserId(userId);
        // create a new cart item for the product
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(1); // set the initial quantity to 1
        // add the cart item to the cart
        cart.getItems().add(cartItem);
        // save the cart to the database
        cartRepository.save(cart);
    }

     */
}
