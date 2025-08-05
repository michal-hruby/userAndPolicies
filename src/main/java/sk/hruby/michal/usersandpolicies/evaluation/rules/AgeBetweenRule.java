package sk.hruby.michal.usersandpolicies.evaluation.rules;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import sk.hruby.michal.usersandpolicies.evaluation.PolicyRule;
import sk.hruby.michal.usersandpolicies.evaluation.User;

import java.time.LocalDate;

@AllArgsConstructor
public class AgeBetweenRule implements PolicyRule {
    private String type;
    private int minAge;
    private int maxAge;
    private JsonNode json;

    @Override
    public boolean appliesTo(User user) {
        int currentAge = LocalDate.now().getYear() - user.getBirthDate().getYear();
        return minAge <= currentAge && currentAge <= maxAge;
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
