package sk.hruby.michal.usersandpolicies.evaluation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Policy {
    private String id;
    private String name;
    private PolicyRule policyRule;

    public boolean appliesTo(User user) {
        return policyRule.appliesTo(user);
    }
}
