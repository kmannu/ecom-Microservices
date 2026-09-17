package com.Ecommerse.Ecom.Repository;

import com.Ecommerse.Ecom.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
