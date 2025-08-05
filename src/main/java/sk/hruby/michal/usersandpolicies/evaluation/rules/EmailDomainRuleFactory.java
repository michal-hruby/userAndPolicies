package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;

@Component
public class EmailDomainRuleFactory implements PolicyRuleFactory {

    @Override
    public String getType() {
        return "emailDomainIs";
    }

    @Override
    public PolicyRule create(JsonNode json) {
        String value = json.get("value").asText();
        return new EmailDomainRule(this.getType(), value, json);
    }
}
