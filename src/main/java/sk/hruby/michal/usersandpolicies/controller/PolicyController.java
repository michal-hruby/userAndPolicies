package sk.hruby.michal.usersandpolicies.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.hruby.michal.usersandpolicies.request.PolicyRequest;
import sk.hruby.michal.usersandpolicies.response.PolicyResponse;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
public class PolicyController {

    private final PolicyCrudService policyCrudService;

    @GetMapping("/policies")
    public ResponseEntity<List<PolicyResponse>> getPolicies() {
        return ResponseEntity.ok(this.policyCrudService.getPolicies());
    }

    @GetMapping("/policies/{policyId}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable String policyId) {
        return ResponseEntity.ok(this.policyCrudService.getPolicy(policyId));
    }

    @PostMapping("/policies")
    public ResponseEntity<PolicyResponse> createPolicy(@Valid @RequestBody PolicyRequest policyRequest) {
        PolicyResponse policyResponse = this.policyCrudService.createPolicy(policyRequest);
        URI location = URI.create("/policies/" + policyResponse.getId());
        return ResponseEntity.created(location).body(policyResponse);
    }

    @PutMapping("/policies/{policyId}")
    public ResponseEntity<PolicyResponse> updatePolicy(@PathVariable String policyId, @Valid @RequestBody PolicyRequest policyRequest) {
        return ResponseEntity.ok(this.policyCrudService.updatePolicy(policyId, policyRequest));
    }

    @DeleteMapping("/policies/{policyId}")
    public ResponseEntity<Void> deletePolicy(@PathVariable String policyId) {
        this.policyCrudService.deletePolicy(policyId);
        return ResponseEntity.noContent().build();
    }
}
