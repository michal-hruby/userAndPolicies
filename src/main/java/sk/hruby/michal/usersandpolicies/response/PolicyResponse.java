package sk.hruby.michal.usersandpolicies.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyResponse {
    private String id;
    private String name;
    private JsonNode condition;
}