package com.shopping_cart.service.user;

import com.shopping_cart.dto.UserDto;
import com.shopping_cart.model.User;
import com.shopping_cart.request.CreateUserRequest;
import com.shopping_cart.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
    User getAuthenticatedUser();
}
