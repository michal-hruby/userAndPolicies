package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;

@Component
public class YoungerThanRuleFactory implements PolicyRuleFactory {

    @Override
    public String getType() {
        return "youngerThan";
    }

    @Override
    public PolicyRule create(JsonNode json) {
        int age = json.get("value").asInt();
        return new YoungerThanRule(this.getType(), age, json);
    }
}
