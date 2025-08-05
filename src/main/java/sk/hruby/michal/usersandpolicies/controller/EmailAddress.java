package sk.hruby.michal.usersandpolicies.controller;

import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class EmailAddress {
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be a valid address"
    )
    String value;
}
