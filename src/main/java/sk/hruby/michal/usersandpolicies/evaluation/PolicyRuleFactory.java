package sk.hruby.michal.usersandpolicies.evaluation;

import com.fasterxml.jackson.databind.JsonNode;

public interface PolicyRuleFactory {
    String getType();
    PolicyRule create(JsonNode json);
}
