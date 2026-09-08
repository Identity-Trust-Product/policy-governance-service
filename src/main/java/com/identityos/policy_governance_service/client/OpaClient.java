package com.identityos.policy_governance_service.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.identityos.policy_governance_service.dto.OpaEvaluationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

@Component
public class OpaClient {
    private final RestClient restClient;

    public OpaClient(RestClient.Builder builder, @Value("${opa.base-url}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = builder.baseUrl(baseUrl).requestFactory(requestFactory).build();
    }

    public JsonNode evaluate(String policyPath, Map<String, Object> input) {
        return restClient.post()
                .uri("/v1/data/" + policyPath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OpaEvaluationRequest(input))
                .retrieve()
                .body(JsonNode.class);
    }
}
