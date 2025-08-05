package sk.hruby.michal.usersandpolicies.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPolicyRepository extends JpaRepository<PolicyEntity, Long> {
    Optional<PolicyEntity> findByUniqueId(String userFriendlyId);
}
