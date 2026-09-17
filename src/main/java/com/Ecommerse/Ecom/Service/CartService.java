package com.Ecommerse.Ecom.Service;

import com.Ecommerse.Ecom.Dto.CartItemRequest;
import com.Ecommerse.Ecom.Model.CartItem;
import com.Ecommerse.Ecom.Model.Product;
import com.Ecommerse.Ecom.Model.User;
import com.Ecommerse.Ecom.Repository.CartItemRepository;
import com.Ecommerse.Ecom.Repository.ProductRepository;
import com.Ecommerse.Ecom.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository  userRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        Optional<Product> prodcutOptional = productRepository.findById(request.getProductId());
        if(prodcutOptional.isEmpty())
            return false;

        Product  product = prodcutOptional.get();

        if(product.getStockQuantity() < request.getQuantity())
            return false;

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if(userOpt.isEmpty())
            return false;

        User user = userOpt.get();

        CartItem exitingCartItem = cartItemRepository.findByUserAndProduct(user, product);
        if(exitingCartItem != null)
        {
            exitingCartItem.setQuantity(exitingCartItem.getQuantity() + request.getQuantity());
            exitingCartItem.setPrice(
                    new BigDecimal(product.getPrice())
                            .multiply(
                                    BigDecimal.valueOf(exitingCartItem.getQuantity())
                            )
            );
            cartItemRepository.save(exitingCartItem);
        }
        else{
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(
                    new BigDecimal(product.getPrice())
                            .multiply(
                                    BigDecimal.valueOf(request.getQuantity())
                            )
            );
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public boolean deleteItemFromCart(String userId, Long productId) {
        Optional<Product> prodcutOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if(prodcutOpt.isPresent() && userOpt.isPresent()){
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), prodcutOpt.get());
            return true;
        }
        return false;
        }

//    public List<CartItem> getCart(String userId) {
//        return cartItemRepository.findById(Long.valueOf(userId))
//                .map(cartItemRepository::findByUser)
//                .orElseGet(List::of);
//    }
    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    @Transactional
    public void clearCart(String userId) {

        userRepository.findById(Long.valueOf(userId))
                .ifPresent(cartItemRepository::deleteByUser);
    }
}
