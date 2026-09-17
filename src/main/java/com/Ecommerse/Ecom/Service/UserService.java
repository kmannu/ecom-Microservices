package com.Ecommerse.Ecom.Service;

import com.Ecommerse.Ecom.Dto.AddressDTO;
import com.Ecommerse.Ecom.Dto.UserRequest;
import com.Ecommerse.Ecom.Dto.UserResponse;
import com.Ecommerse.Ecom.Model.Address;
import com.Ecommerse.Ecom.Model.User;
import com.Ecommerse.Ecom.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
       return userRepository.findAll().stream()
               .map(this::mapToUserResponse)
               .collect(Collectors.toList());
    }
    public void addUsers(UserRequest userRequest){

        User user = new User();
        UpdateUserFromUserRequest(user,userRequest);
       userRepository.save(user);
    }

    public Optional<UserResponse> fetchUser(Long id) {
       return userRepository.findById(id)
               .map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest updatedUserRequest) {
        return userRepository.findById(id)
                .map(existingUser-> {
                    UpdateUserFromUserRequest(existingUser, updatedUserRequest);
                    userRepository.save(existingUser);
                    return true;
                }).orElse(false);
    }

    private UserResponse mapToUserResponse(User user){
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        if(user.getAddress() != null){
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setCountry(user.getAddress().getCountry());
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setZipcode(user.getAddress().getZipcode());
            addressDTO.setCountry(user.getAddress().getCountry());

            response.setAddress(addressDTO);
        }
        return response;
    }
    private void UpdateUserFromUserRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        
        if(userRequest.getAddress() != null){
            Address address = new Address();
            address.setCity(userRequest.getAddress().getCity());
            address.setCountry(userRequest.getAddress().getCountry());
            address.setStreet(userRequest.getAddress().getStreet());
            address.setZipcode(userRequest.getAddress().getZipcode());
            address.setState(userRequest.getAddress().getState());

            user.setAddress(address);
        }
    }
}
