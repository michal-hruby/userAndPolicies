package sk.hruby.michal.usersandpolicies.controller;

import sk.hruby.michal.usersandpolicies.evaluation.User;
import sk.hruby.michal.usersandpolicies.request.UserRequest;
import sk.hruby.michal.usersandpolicies.response.UserResponse;

public class UserMapper {
    public static User mapToDomainObj(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmailAddress(userRequest.getEmailAddress().toLowerCase());
        user.setOrganizationUnit(userRequest.getOrganizationUnit());
        user.setBirthDate(userRequest.getBirthDate());
        user.setRegisteredOn(userRequest.getRegisteredOn());
        return user;
    }

    public static UserResponse mapToDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmailAddress(user.getEmailAddress());
        userResponse.setOrganizationUnit(user.getOrganizationUnit());
        userResponse.setBirthDate(user.getBirthDate());
        userResponse.setRegisteredOn(user.getRegisteredOn());
        userResponse.setPolicy(user.getActivePolicies());
        return userResponse;
    }
}