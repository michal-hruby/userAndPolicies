package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRuleFactory;
import sk.hruby.michal.usersandpolicies.exception.InvalidJsonException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PolicyRuleFactoryRegistry {

    private final Map<String, PolicyRuleFactory> factories = new HashMap<>();

    public PolicyRuleFactoryRegistry(List<PolicyRuleFactory> factoryList) {
        for (PolicyRuleFactory factory : factoryList) {
            factories.put(factory.getType(), factory);
        }
    }

    public PolicyRule createRule(JsonNode jsonNode) {
        JsonNode typeNode = jsonNode.get("type");
        String type;
        if (jsonNode.get("type") != null && typeNode.isTextual()) {
            type = typeNode.asText();
        } else {
            throw new InvalidJsonException("Required text field type was not found!");
        }

        PolicyRuleFactory factory = factories.get(type);
        if (factory == null) {
            throw new IllegalArgumentException("Unknown rule type: " + type);
        }

        try {
            return factory.create(jsonNode);
        } catch (Exception e) {
            throw new InvalidJsonException(e.getMessage());
        }
    }
}
