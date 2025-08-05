package sk.hruby.michal.usersandpolicies.evaluation;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class User {
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<String> organizationUnit;
    private LocalDate birthDate;
    private LocalDate registeredOn;
    private Set<String> activePolicies = new HashSet<>();
}
