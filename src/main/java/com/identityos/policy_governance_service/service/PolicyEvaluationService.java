package com.identityos.policy_governance_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.identityos.policy_governance_service.client.OpaClient;
import com.identityos.policy_governance_service.dto.PolicyDecisionResponse;
import com.identityos.policy_governance_service.dto.PolicyEvaluationRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PolicyEvaluationService {
    private final OpaClient opaClient;

    public PolicyEvaluationService(OpaClient opaClient) {
        this.opaClient = opaClient;
    }

    public PolicyDecisionResponse evaluate(PolicyEvaluationRequest request) {
        String policyPath = toPolicyPath(request.policyPackage(), request.rule());
        JsonNode opaResponse = opaClient.evaluate(policyPath, request.input());
        JsonNode result = opaResponse == null ? null : opaResponse.path("result");
        boolean allow = result != null && result.path("allow").asBoolean(false);
        List<String> reasons = new ArrayList<>();
        if (result != null && result.path("reasons").isArray()) {
            result.path("reasons").forEach(reason -> reasons.add(reason.asText()));
        }
        Map<String, Object> details = new LinkedHashMap<>();
        if (result != null && result.path("sensitiveFields").isArray()) {
            List<String> sensitiveFields = new ArrayList<>();
            result.path("sensitiveFields").forEach(field -> sensitiveFields.add(field.asText()));
            details.put("sensitiveFields", sensitiveFields);
        }
        return new PolicyDecisionResponse(allow, reasons, policyPath, details);
    }

    private String toPolicyPath(String policyPackage, String rule) {
        return policyPackage.replace('.', '/') + "/" + rule;
    }
}
