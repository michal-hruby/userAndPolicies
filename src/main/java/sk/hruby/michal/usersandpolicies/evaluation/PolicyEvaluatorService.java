package sk.hruby.michal.usersandpolicies.evaluation;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PolicyEvaluatorService {

    public Set<String> evaluate(User user, List<Policy> policyDomainObjList) {
        return policyDomainObjList.stream()
                .filter(policyDomainObj -> policyDomainObj.appliesTo(user))
                .map(Policy::getId)
                .collect(Collectors.toSet());
    }
}
