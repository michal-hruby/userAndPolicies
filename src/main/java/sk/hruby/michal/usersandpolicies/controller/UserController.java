package sk.hruby.michal.usersandpolicies.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sk.hruby.michal.usersandpolicies.request.UserRequest;
import sk.hruby.michal.usersandpolicies.response.UserResponse;

import java.net.URI;
import java.util.List;

@Validated
@RestController
@AllArgsConstructor
public class UserController {

    private final UserCrudService userCrudService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(this.userCrudService.getUsers());
    }

    @GetMapping("/users/{emailAddress}")
    public ResponseEntity<UserResponse> getUser(@PathVariable @Valid EmailAddress emailAddress) {
        return ResponseEntity.ok(this.userCrudService.getUser(emailAddress.getValue()));
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = this.userCrudService.createUser(userRequest);
        URI location = URI.create("/users/" + userResponse.getEmailAddress());
        return ResponseEntity.created(location).body(userResponse);
    }

    @PutMapping("/users/{emailAddress}")
    public ResponseEntity<?> updateUser(@PathVariable @Valid EmailAddress emailAddress, @RequestBody @Valid UserRequest userRequest) {
        if (!emailAddress.getValue().equals(userRequest.getEmailAddress())) {
            return ResponseEntity.badRequest().body("Mismatch between path variable and request body email");
        }

        UserResponse userResponse = this.userCrudService.updateUser(emailAddress.getValue(), userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/users/{emailAddress}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Valid EmailAddress emailAddress) {
        this.userCrudService.deleteUser(emailAddress.getValue());
        return ResponseEntity.noContent().build();
    }
}
