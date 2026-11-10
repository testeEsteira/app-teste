# template-java-springboot

Template de repositório: Java 21 + Spring Boot 4.1 + Maven, imagem Docker multi-arch (amd64/arm64).

## Como usar
1. "Use this template" no GitHub.
2. Ajuste `groupId`/`artifactId` no `pom.xml` e o pacote em `src/` (mantenha `<finalName>app</finalName>`, o Dockerfile usa `app.jar`).
3. Em `.github/workflows/ci.yml`, troque `<org>/<repo-de-workflows>` pelo repo do workflow reutilizável e ajuste `ecr-registry`/`aws-region`.
4. Crie o repositório no ECR com o **mesmo nome do repo GitHub** (o ECR não cria no push) e garanta o secret `AWS_ROLE_ARN` (role OIDC com permissão de push).

## Local
```bash
mvn verify
docker buildx build --platform linux/amd64,linux/arm64 -t app:dev .
docker run --rm -p 8080:8080 -p 8081:8081 app:dev
curl localhost:8081/actuator/health
```

## Probes Kubernetes
- liveness: `/actuator/health/liveness` (porta 8081)
- readiness: `/actuator/health/readiness` (porta 8081)

## Testes
- `*Test.java` → unitários (surefire), rodam em `mvn test`. Ex.: `HelloControllerTest` (MockMvc standalone, sem contexto).
- `ApplicationTests` → `@SpringBootTest` (smoke: o contexto sobe).
- Cobertura (JaCoCo): `mvn verify` gera `target/site/jacoco/index.html` e **falha se a cobertura de linhas ficar abaixo de 70%** (propriedade `jacoco.min.coverage` no pom; `Application.class` é excluída).

## App de teste (API de tarefas em memória)
Serve para validar a esteira de ponta a ponta, sem banco.

| Método | Rota | Resposta |
|---|---|---|
| GET | `/hello` | 200 `{"message":"hello"}` |
| GET | `/tasks` | 200 lista |
| POST | `/tasks` `{"title":"..."}` | 201 (400 se título vazio ou > 100) |
| GET | `/tasks/{id}` | 200 / 404 |
| PUT | `/tasks/{id}/done` | 200 / 404 |
| DELETE | `/tasks/{id}` | 204 / 404 |

Testes: `TaskServiceTest` (unitário), `TaskControllerTest` (MockMvc com validação e 404), `HelloControllerTest`, `ApplicationTests` (contexto sobe).
