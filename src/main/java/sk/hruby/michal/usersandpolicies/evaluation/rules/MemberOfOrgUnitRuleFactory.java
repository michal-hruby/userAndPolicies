package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;

@Component
public class MemberOfOrgUnitRuleFactory implements PolicyRuleFactory {

    @Override
    public String getType() {
        return "isMemberOf";
    }

    @Override
    public PolicyRule create(JsonNode json) {
        String value = json.get("value").asText();
        return new MemberOfOrgUnitRule(this.getType(), value, json);
    }
}
