package sk.hruby.michal.usersandpolicies.persistence;

import org.springframework.stereotype.Repository;
import sk.hruby.michal.usersandpolicies.evaluation.Policy;

import java.util.List;
import java.util.Optional;

@Repository
public interface PolicyRepository {

    Optional<Policy> findByUniqueId(String userFriendlyId);
    Policy findByUniqueIdOrThrowException(String userFriendlyId);
    List<Policy> findAll();
    Policy create(Policy policy);
    Policy save(Policy policy);
    void delete(String userFriendlyId);
}
