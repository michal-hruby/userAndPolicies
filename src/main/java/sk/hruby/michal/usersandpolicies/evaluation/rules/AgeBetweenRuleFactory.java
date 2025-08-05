package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;

@Component
public class AgeBetweenRuleFactory implements PolicyRuleFactory {

    @Override
    public String getType() {
        return "ageBetween";
    }

    @Override
    public PolicyRule create(JsonNode json) {
        int min = json.get("min").asInt();
        int max = json.get("max").asInt();
        return new AgeBetweenRule(this.getType(), min, max, json);
    }
}
