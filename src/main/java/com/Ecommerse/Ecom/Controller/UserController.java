package com.Ecommerse.Ecom.Controller;

import com.Ecommerse.Ecom.Dto.UserRequest;
import com.Ecommerse.Ecom.Dto.UserResponse;
import com.Ecommerse.Ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<String> createUsers(@RequestBody UserRequest userRequest){
        userService.addUsers(userRequest);
        return ResponseEntity.ok("User added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUsers(@PathVariable Long id, @RequestBody UserRequest updateUserRequest){
        boolean updated = userService.updateUser(id,updateUserRequest);
        if(updated){
            return ResponseEntity.ok("User updated successfully");
        }
        return ResponseEntity.notFound().build();
    }
}
