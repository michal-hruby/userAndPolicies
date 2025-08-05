package sk.hruby.michal.usersandpolicies.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.hruby.michal.usersandpolicies.evaluation.Policy;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyEvaluatorService;
import sk.hruby.michal.usersandpolicies.evaluation.User;
import sk.hruby.michal.usersandpolicies.exception.AlreadyExistsException;
import sk.hruby.michal.usersandpolicies.persistence.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final PolicyService policyService;
    private final PolicyEvaluatorService policyEvaluator;
    private final UserRepository userRepository;

    public User findByEmailAddress(String email) {
        return this.userRepository.findByEmailAddressOrThrowException(email);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User create(final User user) {
        if (this.userRepository.findByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new AlreadyExistsException("User " + user.getEmailAddress() + " already exist!");
        }

        if (this.userRepository.findByUserName(user.getName()).isPresent()) {
            throw new AlreadyExistsException("User name " + user.getName() + " is already taken!");
        }

        user.setActivePolicies(this.policyEvaluator.evaluate(user, this.policyService.findAll()));

        return this.userRepository.create(user);
    }

    public User modify(final User user) {
        User persistentUserDomainObj = this.userRepository.findByEmailAddressOrThrowException(user.getEmailAddress());

        if (!user.getName().equals(persistentUserDomainObj.getName())) {
            if (this.userRepository.findByUserName(user.getName()).isPresent()) {
                throw new AlreadyExistsException("User name " + user.getName() + " is already taken!");
            }
        }

        persistentUserDomainObj.setName(user.getName());
        persistentUserDomainObj.setFirstName(user.getFirstName());
        persistentUserDomainObj.setLastName(user.getLastName());
        persistentUserDomainObj.setEmailAddress(user.getEmailAddress());
        persistentUserDomainObj.setOrganizationUnit(user.getOrganizationUnit());
        persistentUserDomainObj.setBirthDate(user.getBirthDate());
        persistentUserDomainObj.setRegisteredOn(user.getRegisteredOn());
        persistentUserDomainObj.setActivePolicies(user.getActivePolicies());

        persistentUserDomainObj.setActivePolicies(this.policyEvaluator.evaluate(persistentUserDomainObj, this.policyService.findAll()));

        return this.userRepository.save(persistentUserDomainObj);
    }

    public void forceEvaluation() {
        List<Policy> policies = this.policyService.findAll();
        for (User user :this.userRepository.findAll()) {
            user.setActivePolicies(this.policyEvaluator.evaluate(user, policies));
            this.userRepository.save(user);
        }
    }

    public void remove(String email) {
        this.userRepository.delete(email);
    }
}
