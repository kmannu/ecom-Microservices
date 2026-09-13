package com.Ecommerse.Ecom;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final List<User> usersList = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> fetchAllUsers() {
        return usersList;
    }
    public void addUsers(User user){
        user.setId(nextId++);
        usersList.add(user);
    }
    public Optional<User> fetchUser(Long id) {
        /*for(User user : usersList){
            if(user.getId().equals(id)){
                return user;
            }
        }
        return null;*/
        return usersList.stream().filter(users->
                users.getId().equals(id)).findFirst();
    }

    public boolean updateUser(Long id, User updatedUser){
        /*for(User u : usersList){
            if(u.getId().equals(id)){
                u.setFirstName(updatedUser.getFirstName());
                u.setLastName(updatedUser.getLastName());
                return  true;
            }
        }
        return false;*/

        return usersList.stream()
                .filter(user->user.getId().equals(id))
                .findFirst()
                .map(existingUser-> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    return true;
                }).orElse(false);
    }
}
