package sk.hruby.michal.usersandpolicies.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class PolicyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String uniqueId;
    private String name;
    private String json;
    @ManyToMany(mappedBy = "policies", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();
}
