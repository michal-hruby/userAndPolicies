package sk.hruby.michal.usersandpolicies.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sk.hruby.michal.usersandpolicies.evaluation.User;
import sk.hruby.michal.usersandpolicies.exception.BadRequestException;
import sk.hruby.michal.usersandpolicies.request.UserRequest;
import sk.hruby.michal.usersandpolicies.response.UserResponse;
import sk.hruby.michal.usersandpolicies.service.UserService;

import java.util.List;

@AllArgsConstructor
@Service
public class UserCrudService {

    private final UserService userService;

    public List<UserResponse> getUsers() {
        return this.userService.findAll()
                .stream()
                .map(UserMapper::mapToDto)
                .toList();
    }


    public UserResponse getUser(String email) {
        return UserMapper.mapToDto(this.userService.findByEmailAddress(email));
    }


    public UserResponse createUser(UserRequest userRequest) {
        User user = this.userService.create(UserMapper.mapToDomainObj(userRequest));
        return UserMapper.mapToDto(user);
    }

    public UserResponse updateUser(String email, UserRequest userRequest) throws BadRequestException {
        if (!email.equals(userRequest.getEmailAddress())) {
            throw new BadRequestException("Mismatch between path variable and request body email");
        }

        User user = this.userService.modify(UserMapper.mapToDomainObj(userRequest));
        return UserMapper.mapToDto(user);
    }

    public void deleteUser(@PathVariable String email) {
        this.userService.remove(email);
    }
}
