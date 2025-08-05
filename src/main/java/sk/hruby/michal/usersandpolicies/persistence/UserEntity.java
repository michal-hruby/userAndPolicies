package sk.hruby.michal.usersandpolicies.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;

import java.util.Set;

@Getter
@Setter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String emailAddress;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "user_organization_unit",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_unit_id")
    )
    private Set<OrganizationUnitEntity> organizationUnit = new HashSet<>();
    private LocalDate birthDate;
    private LocalDate registeredOn;
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "user_policies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "policy_id")
    )
    private Set<PolicyEntity> policies = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity userEntity = (UserEntity) o;

        return emailAddress.equals(userEntity.emailAddress);
    }

    @Override
    public int hashCode() {
        return emailAddress.hashCode();
    }
}
