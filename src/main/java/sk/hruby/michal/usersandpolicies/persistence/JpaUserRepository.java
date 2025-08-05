package sk.hruby.michal.usersandpolicies.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAddress(String emailAddress);
    Optional<UserEntity> findByUserName(String userName);
}
