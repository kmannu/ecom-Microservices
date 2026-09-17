package com.Ecommerse.Ecom.Controller;

import com.Ecommerse.Ecom.Dto.CartItemRequest;
import com.Ecommerse.Ecom.Model.CartItem;
import com.Ecommerse.Ecom.Service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Transactional
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId, @RequestBody CartItemRequest request){
        if(!cartService.addToCart(userId, request))
            return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removerFromCart(@PathVariable("productId") Long productId,
                                                @RequestHeader("X-User-ID") String userId){
        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
