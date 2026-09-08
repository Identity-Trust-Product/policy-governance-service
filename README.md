# Policy Governance Service

This service is a trial policy-governance layer for Identity OS. It exposes a simple REST API and delegates policy decisions to Open Policy Agent.

## Run Order

1. Start OPA:

```powershell
cd ../identity-os-opa
docker compose up -d
```

2. Start this service:

```powershell
cd ../policy-governance-service
mvn spring-boot:run
```

Service URL:

```text
http://localhost:8084
```

## Evaluate A Policy

```powershell
Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8084/api/v1/policy/evaluate" `
  -ContentType "application/json" `
  -Body '{
    "policyPackage": "identityos.access",
    "rule": "decision",
    "input": {
      "action": "application.login",
      "token": {
        "claims": {
          "organization_id": "org_03db05edc099",
          "application_id": "app_716d1fe7a98f",
          "realm_access": { "roles": ["APPLICATION_USER"] }
        }
      },
      "application": {
        "organization_id": "org_03db05edc099",
        "application_id": "app_716d1fe7a98f",
        "status": "ACTIVE"
      }
    }
  }'
```

Expected response:

```json
{
  "allow": true,
  "reasons": [],
  "policyPath": "identityos/access/decision"
}
```

## Identity OS Trial Use Case

Use this after a third-party app user logs in through Identity OS and receives a Keycloak access token.

The third-party app can decode the token claims and ask the policy service:

```text
Can this APPLICATION_USER access this active application for this organization?
```

OPA allows only when:

- action is `application.login`
- application status is `ACTIVE`
- token `organization_id` matches application `organization_id`
- token `application_id` matches application `application_id`
- token has realm role `APPLICATION_USER`
