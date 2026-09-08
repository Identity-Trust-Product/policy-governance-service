package com.identityos.policy_governance_service.dto;

import java.util.List;
import java.util.Map;

public record PolicyDecisionResponse(
        boolean allow,
        List<String> reasons,
        String policyPath,
        Map<String, Object> details) {
}
