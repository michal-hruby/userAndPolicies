package sk.hruby.michal.usersandpolicies.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UserResponse {
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<String> organizationUnit;
    private LocalDate birthDate;
    private LocalDate registeredOn;
    private Set<String> policy;
}
