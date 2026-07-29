# Livetrack

Aplicação Java Spring Boot para rastreamento de objetos com API REST e WebSocket.

## Resumo
Projeto simples que permite criar um tracking, enviar eventos de localização e consultar o último evento ou histórico.

## Requisitos
- Java 21
- Maven
- PostgreSQL

## Build
Usando Maven:

```bash
mvn clean package
```

Ou rodar em modo de desenvolvimento:

```bash
mvn spring-boot:run
```

## Configuração
Arquivo: `src/main/resources/application.properties`

Exemplo (atual no projeto):
```
spring.application.name=livetrack
spring.datasource.url=jdbc:postgresql://localhost:5432/livetrack
spring.datasource.username=postgres
spring.datasource.password=manager
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
Altere as credenciais/URL conforme seu ambiente.

## Executando o JAR
Após `mvn clean package`:

```bash
java -jar target/livetrack-0.0.1-SNAPSHOT.jar
```

## Endpoints REST
Prefixo: `/tracking`

- POST /tracking
  - Cria um novo tracking.
  - Retorna `TrackingDTO` com `id`, `status` e `createdAt`.

- POST /tracking/{id}/event
  - Adiciona um evento ao tracking {id}.
  - Corpo: JSON com `latitude` e `longitude`.
  - Retorna `TrackingEventDTO` com `id`, `trackingId`, `latitude`, `longitude` e `eventTime`.

- GET /tracking/{id}
  - Retorna o último evento do tracking {id}.
  - Resposta: `TrackingEventDTO`.

- GET /tracking/{id}/history
  - Retorna todo o histórico de eventos do tracking {id}.
  - Resposta: lista de `TrackingEventDTO` ordenada por `eventTime`.

Exemplo curl (criar tracking):
```bash
curl -X POST http://localhost:8080/tracking
```

Exemplo curl (adicionar evento):
```bash
curl -X POST -H "Content-Type: application/json" -d '{"latitude":-23.5,"longitude":-46.6}' http://localhost:8080/tracking/1/event
```

## Respostas de erro
Quando a API retorna erro, o projeto responde com um JSON simples, sem `ProblemDetail` do Spring:

```json
{
  "status": 404,
  "error": "404 NOT_FOUND",
  "message": "Tracking not found",
  "timestamp": "2026-07-29T12:34:56"
}
```

Esse formato é aplicado pelo handler global em `src/main/java/com/felipesantos/livetrack/exception/GlobalExceptionHandler.java`.

## WebSocket
O projeto envia atualizações por WebSocket para o tópico:
```
/topic/tracking/{id}
```
Clientes podem se inscrever para receber eventos em tempo real.

## Banco de dados
Usa Spring Data JPA com PostgreSQL. `spring.jpa.hibernate.ddl-auto=update` fará a criação/atualização das tabelas automaticamente.

## Observações
- Projeto configurado para Java 21 (ver `pom.xml`).
- Utiliza Lombok; habilite suporte no IDE (annotation processing) para evitar erros visuais.

## Contato
Desenvolvedor: Felipe Santos

---
