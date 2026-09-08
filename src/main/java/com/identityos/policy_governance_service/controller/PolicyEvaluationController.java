package com.identityos.policy_governance_service.controller;

import com.identityos.policy_governance_service.dto.PolicyDecisionResponse;
import com.identityos.policy_governance_service.dto.PolicyEvaluationRequest;
import com.identityos.policy_governance_service.service.PolicyEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/policy")
public class PolicyEvaluationController {
    private final PolicyEvaluationService policyEvaluationService;

    public PolicyEvaluationController(PolicyEvaluationService policyEvaluationService) {
        this.policyEvaluationService = policyEvaluationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<PolicyDecisionResponse> evaluate(@Valid @RequestBody PolicyEvaluationRequest request) {
        return ResponseEntity.ok(policyEvaluationService.evaluate(request));
    }
}
