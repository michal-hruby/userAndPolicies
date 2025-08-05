package sk.hruby.michal.usersandpolicies.evaluation;

import com.fasterxml.jackson.databind.JsonNode;

//todo prehodit do abstract?
public interface PolicyRule {
    boolean appliesTo(User user);
    String getType(); //todo
    JsonNode getJson(); //todo ak by sa mi podarilo convertovat PolicyRule do json
}
