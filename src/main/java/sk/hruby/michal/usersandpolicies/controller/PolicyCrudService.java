package sk.hruby.michal.usersandpolicies.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import sk.hruby.michal.usersandpolicies.evaluation.Policy;
import sk.hruby.michal.usersandpolicies.evaluation.rules.PolicyRuleFactoryRegistry;
import sk.hruby.michal.usersandpolicies.exception.BadRequestException;
import sk.hruby.michal.usersandpolicies.request.PolicyRequest;
import sk.hruby.michal.usersandpolicies.response.PolicyResponse;
import sk.hruby.michal.usersandpolicies.service.PolicyService;
import sk.hruby.michal.usersandpolicies.service.UserService;

import java.util.List;

@AllArgsConstructor
@Service

public class PolicyCrudService {

    private final PolicyService policyService;
    private final UserService userService;
    private final PolicyRuleFactoryRegistry policyRuleFactoryRegistry;

    public List<PolicyResponse> getPolicies() {
        return this.policyService.findAll().stream().map(PolicyMapper::mapToDto).toList();
    }

    public PolicyResponse getPolicy(String policyId) {
        return PolicyMapper.mapToDto(this.policyService.findByUserFriendlyId(policyId));
    }

    public PolicyResponse createPolicy(PolicyRequest policyRequest) {
        Policy policy = this.policyService.create(PolicyMapper.mapToDomainObj(policyRequest, policyRuleFactoryRegistry));
        this.userService.forceEvaluation();
        return PolicyMapper.mapToDto(policy);
    }

    public PolicyResponse updatePolicy(String policyId, PolicyRequest policyRequest) {
        if (!policyId.equals(policyRequest.getId())) {
            throw new BadRequestException("Mismatch between path variable and body policyId");
        }

        Policy policy = this.policyService.update(PolicyMapper.mapToDomainObj(policyRequest, policyRuleFactoryRegistry));
        this.userService.forceEvaluation();
        return PolicyMapper.mapToDto(policy);
    }

    public void deletePolicy(@PathVariable String policyId) {
        this.policyService.delete(policyId);
    }
}
