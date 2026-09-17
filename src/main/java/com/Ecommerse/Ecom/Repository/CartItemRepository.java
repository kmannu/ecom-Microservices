package com.Ecommerse.Ecom.Repository;

import com.Ecommerse.Ecom.Model.CartItem;
import com.Ecommerse.Ecom.Model.Product;
import com.Ecommerse.Ecom.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(CartItem cartItem);

    List<CartItem> findByUserId(String userId);

    void deleteByUser(User user);
}
