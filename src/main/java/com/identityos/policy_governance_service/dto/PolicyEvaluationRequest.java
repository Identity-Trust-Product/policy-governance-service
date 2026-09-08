package com.identityos.policy_governance_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record PolicyEvaluationRequest(
        @NotBlank String policyPackage,
        @NotBlank String rule,
        @NotNull Map<String, Object> input) {
}
