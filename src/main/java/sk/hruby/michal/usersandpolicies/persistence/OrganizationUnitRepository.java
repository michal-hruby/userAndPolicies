package sk.hruby.michal.usersandpolicies.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationUnitRepository extends JpaRepository<OrganizationUnitEntity, Long> {

    Optional<OrganizationUnitEntity> findByName(String name);
}
