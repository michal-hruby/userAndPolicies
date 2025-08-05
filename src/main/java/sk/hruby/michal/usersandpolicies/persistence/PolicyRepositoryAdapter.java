package sk.hruby.michal.usersandpolicies.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import sk.hruby.michal.usersandpolicies.evaluation.Policy;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.rules.PolicyRuleFactoryRegistry;
import sk.hruby.michal.usersandpolicies.exception.AlreadyExistsException;
import sk.hruby.michal.usersandpolicies.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class PolicyRepositoryAdapter implements PolicyRepository {

    private final PolicyRuleFactoryRegistry policyRuleFactoryRegistry;

    private final JpaPolicyRepository policyRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //todo ucesat
    @Override
    public Optional<Policy> findByUniqueId(String userFriendlyId) {
        return this.policyRepository.findByUniqueId(userFriendlyId).map((PolicyEntity policyEntity) -> mapToDomainPolicy(policyEntity, policyRuleFactoryRegistry));
    }

    //todo ucesat
    @Override
    public Policy findByUniqueIdOrThrowException(String userFriendlyId) {
        return this.findByUniqueId(userFriendlyId).
                orElseThrow(() -> new NotFoundException("Policy with id " + userFriendlyId + " was not found!"));
    }

    //todo ucesat
    private PolicyEntity findByUniqueIdOrThrowException2(String userFriendlyId) {
        return this.policyRepository.findByUniqueId(userFriendlyId).
                orElseThrow(() -> new NotFoundException("Policy with id " + userFriendlyId + " was not found!"));
    }

    @Override
    public List<Policy> findAll() {
        return this.policyRepository.findAll().stream().map((PolicyEntity policyEntity) -> mapToDomainPolicy(policyEntity, policyRuleFactoryRegistry)).toList();
    }

    @Override
    public Policy create(Policy policy) {
        if (this.policyRepository.findByUniqueId(policy.getId()).isPresent()) {
            throw new AlreadyExistsException("Policy " + policy.getId() + " already exist!");
        }

        PolicyEntity policyEntity = this.createPolicyEntity(policy);
        return this.mapToDomainPolicy(this.policyRepository.save(policyEntity), policyRuleFactoryRegistry);
    }

    @Override
    public Policy save(Policy policy) {
        PolicyEntity policyEntity = this.findByUniqueIdOrThrowException2(policy.getId());

        this.createPolicyEntity(policyEntity, policy);

        return this.mapToDomainPolicy(this.policyRepository.save(policyEntity), policyRuleFactoryRegistry);
    }

    @Override
    public void delete(String userFriendlyId) {
        PolicyEntity policyEntity = this.findByUniqueIdOrThrowException2(userFriendlyId);
        for (UserEntity userEntity : policyEntity.getUsers()) {
            userEntity.getPolicies().remove(policyEntity);
        }
        policyEntity.getUsers().clear();
        this.policyRepository.delete(policyEntity);
    }

    @SneakyThrows//code smell
    private Policy mapToDomainPolicy(final PolicyEntity policyEntity, PolicyRuleFactoryRegistry factoryRegistry) {
        Policy policy = new Policy();
        policy.setId(policyEntity.getUniqueId());
        policy.setName(policyEntity.getName());

        JsonNode jsonNode = objectMapper.readTree(policyEntity.getJson());

        PolicyRule rule = factoryRegistry.createRule(jsonNode);
        policy.setPolicyRule(rule);
        return policy;
    }

    private PolicyEntity createPolicyEntity(final Policy policy) {
        PolicyEntity policyEntity = new PolicyEntity();
        policyEntity.setUniqueId(policy.getId());
        return this.createPolicyEntity(policyEntity, policy);
    }

    @SneakyThrows//TODO code smell
    private PolicyEntity createPolicyEntity(final PolicyEntity policyEntity, final Policy policy) {
        policyEntity.setName(policy.getName());
        String jsonString = objectMapper.writeValueAsString(policy.getPolicyRule().getJson());
        policyEntity.setJson(jsonString);
        return policyEntity;
    }
}
