package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.User;

@AllArgsConstructor
public class EmailDomainRule implements PolicyRule {

    private String type;
    private String domain;
    private JsonNode json;

    @Override
    public boolean appliesTo(User user) {
        return user.getEmailAddress().endsWith("@" + domain);
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public JsonNode getJson() {
        return this.json;
    }
}
