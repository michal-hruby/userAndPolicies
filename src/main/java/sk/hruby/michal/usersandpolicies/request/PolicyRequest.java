package sk.hruby.michal.usersandpolicies.request;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class PolicyRequest {
    @Pattern(regexp = "^[a-zA-Z0-9._~-]+$", message = "Id contains invalid characters")
    @NotBlank(message = "Id is required")
    String id;
    @NotBlank(message = "Name is required")
    String name;
    @NotNull(message = "Condition is required")
    JsonNode condition;
}
