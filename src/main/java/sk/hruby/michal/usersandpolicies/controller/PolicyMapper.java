package sk.hruby.michal.usersandpolicies.controller;

import sk.hruby.michal.usersandpolicies.evaluation.Policy;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.rules.PolicyRuleFactoryRegistry;
import sk.hruby.michal.usersandpolicies.request.PolicyRequest;
import sk.hruby.michal.usersandpolicies.response.PolicyResponse;

public class PolicyMapper {
    public static Policy mapToDomainObj(PolicyRequest policyPayload, PolicyRuleFactoryRegistry factoryRegistry) {
        Policy policy = new Policy();
        policy.setId(policyPayload.getId());
        policy.setName(policyPayload.getName());
        PolicyRule rule = factoryRegistry.createRule(policyPayload.getCondition());
        policy.setPolicyRule(rule);

        return policy;
    }

    public static PolicyResponse mapToDto(Policy userDomainObj) {
        PolicyResponse policyResponse = new PolicyResponse();
        policyResponse.setId(userDomainObj.getId());
        policyResponse.setName(userDomainObj.getName());
        policyResponse.setCondition(userDomainObj.getPolicyRule().getJson());
        return policyResponse;
    }
}
