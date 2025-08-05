package sk.hruby.michal.usersandpolicies.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sk.hruby.michal.usersandpolicies.evaluation.Policy;
import sk.hruby.michal.usersandpolicies.exception.AlreadyExistsException;
import sk.hruby.michal.usersandpolicies.persistence.PolicyRepository;


import java.util.List;

@Service
@AllArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public List<Policy> findAll() {
        return this.policyRepository.findAll();
    }

    public Policy findByUserFriendlyId(String policyId) {
        return this.policyRepository.findByUniqueIdOrThrowException(policyId);
    }

    public Policy create(Policy policy) {
        return this.policyRepository.create(policy);
    }

    public void delete(String policyId) {
        this.policyRepository.delete(policyId);
    }

    public Policy update(Policy policy) {
        Policy persistentPolicyDomainObj = this.policyRepository.findByUniqueIdOrThrowException(policy.getId());

        if (!persistentPolicyDomainObj.getId().equals(policy.getId())) {
            if (this.policyRepository.findByUniqueId(policy.getId()).isPresent()) {
                throw new AlreadyExistsException("Policy " + policy.getId() + " already exist!");
            }
        }

        persistentPolicyDomainObj.setName(policy.getName());
        persistentPolicyDomainObj.setPolicyRule(policy.getPolicyRule());

        return this.policyRepository.save(persistentPolicyDomainObj);
    }
}
