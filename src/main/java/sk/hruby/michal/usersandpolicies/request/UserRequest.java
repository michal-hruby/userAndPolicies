package sk.hruby.michal.usersandpolicies.request;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Value
public class UserRequest {
    @NotBlank(message = "Name is required")
    String name;
    @NotBlank(message = "First name is required")
    String firstName;
    @NotBlank(message = "Last name is required")
    String lastName;
    @NotBlank(message = "Email address is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email must be a valid address"
    )
    String emailAddress;
    @NotNull(message = "Organization Unit is required")
    Set<String> organizationUnit;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    LocalDate birthDate;
    LocalDate registeredOn;
}
