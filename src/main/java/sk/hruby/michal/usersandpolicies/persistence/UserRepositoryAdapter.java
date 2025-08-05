package sk.hruby.michal.usersandpolicies.persistence;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import sk.hruby.michal.usersandpolicies.evaluation.User;
import sk.hruby.michal.usersandpolicies.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AllArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository userRepository;

    private final OrganizationUnitRepository organizationUnitRepository;

    private final JpaPolicyRepository policyRepository;

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        return this.userRepository.findByEmailAddress(emailAddress).map(this::maptoDomainUser);
    }

    @Override
    public User findByEmailAddressOrThrowException(String emailAddress) {
        return this.findByEmailAddress(emailAddress)
                .orElseThrow(() -> new NotFoundException("User " + emailAddress + " was not found."));
    }

    @Override
    public Optional<User> findByUserName(String userName) {
        return this.userRepository.findByUserName(userName).map(this::maptoDomainUser);
    }

    @Override
    public List<User> findAll() {
        return this.userRepository.findAll().stream().map(this::maptoDomainUser).toList();
    }

    @Override
    public User create(User user) {
        UserEntity userEntity = new UserEntity();
        return this.maptoDomainUser(this.userRepository.save(this.mapToUserEntity(userEntity, user)));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = this.userRepository.findByEmailAddress(user.getEmailAddress())
                .orElseThrow(() -> new NotFoundException("User " + user.getEmailAddress() + " was not found."));
        return this.maptoDomainUser(this.userRepository.save(this.mapToUserEntity(userEntity, user)));
    }

    @Override
    public void delete(String email) {
        UserEntity userEntity = this.userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));

        this.userRepository.delete(userEntity);
    }

    private UserEntity mapToUserEntity(final UserEntity userEntity, final User user) {
        userEntity.setUserName(user.getName());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setEmailAddress(user.getEmailAddress());
        userEntity.setBirthDate(user.getBirthDate());
        userEntity.setRegisteredOn(user.getRegisteredOn());
        userEntity.getOrganizationUnit().clear();
        for (String organizationUnit : user.getOrganizationUnit()) {
            OrganizationUnitEntity organizationUnitEntity = this.organizationUnitRepository.findByName(organizationUnit)
                    .orElseGet(() -> {
                        OrganizationUnitEntity newOrganizationUnit = new OrganizationUnitEntity();
                        newOrganizationUnit.setName(organizationUnit);
                        return organizationUnitRepository.save(newOrganizationUnit);
                    });
            userEntity.getOrganizationUnit().add(organizationUnitEntity);
        }
        userEntity.getPolicies().clear();
        for (String activePolicy : user.getActivePolicies()) {
            PolicyEntity policyEntity = this.policyRepository.findByUniqueId(activePolicy).orElseThrow(() -> new NotFoundException("Policy " + activePolicy + " was not found!"));
            userEntity.getPolicies().add(policyEntity);
        }
        return userEntity;
    }

    private User maptoDomainUser(final UserEntity userEntity) {
        User user = new User();
        user.setName(userEntity.getUserName());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmailAddress(userEntity.getEmailAddress());
        user.setBirthDate(userEntity.getBirthDate());
        user.setRegisteredOn(userEntity.getRegisteredOn());
        user.setOrganizationUnit(userEntity.getOrganizationUnit().stream()
                .map(OrganizationUnitEntity::getName)
                .collect(Collectors.toSet()));
        user.setActivePolicies(userEntity.getPolicies().stream().map(PolicyEntity::getUniqueId).collect(Collectors.toSet()));
        return user;
    }
}
