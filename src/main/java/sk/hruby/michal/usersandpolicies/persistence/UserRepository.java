package sk.hruby.michal.usersandpolicies.persistence;

import sk.hruby.michal.usersandpolicies.evaluation.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAddress(String emailAddress);
    Optional<User> findByUserName(String userName);
    User findByEmailAddressOrThrowException(String userName);
    List<User> findAll();
    User create(User user);
    User save(User user);
    void delete(String email);
}
