# DIO Spring Boot - Final Project 05: Spring AI (budgeting)

## Introduction

This final module applies Spring AI in a budgeting API while preserving the same layered architecture used across the track.

The goal is to integrate AI capabilities without bypassing domain and use case boundaries.

## Code Context

The project processes voice commands to create and query financial transactions.

Primary flow:

1. Client uploads an audio file.
2. Audio is transcribed into text.
3. The model selects an application tool/use case.
4. The use case persists or queries transaction data.
5. The final response is converted to audio.

## Project Structure

- `src/main/java/dio/budgeting/domain`
  - Domain model and repository contract.
- `src/main/java/dio/budgeting/application`
  - Use cases used by both REST and AI tool calling.
- `src/main/java/dio/budgeting/infrastructure`
  - HTTP adapters, JPA adapters, and integration glue.

## Module-Specific Topics

### Speech-to-text

- Uses `TranscriptionModel` for audio transcription.
- Model settings are configured in `application.properties`.

### Tool calling

- `ChatClient` registers use-case tools.
- `@Tool` methods expose business capabilities to the model.

### Text-to-speech

- `TextToSpeechModel` produces MP3 output from final text.
- AI endpoint returns generated audio.

## Spring AI Documentation

- Spring AI Reference: https://docs.spring.io/spring-ai/reference/index.html
- ChatModel API: https://docs.spring.io/spring-ai/reference/api/chatmodel.html
- ChatClient API: https://docs.spring.io/spring-ai/reference/api/chatclient.html
- Tools API: https://docs.spring.io/spring-ai/reference/api/tools.html
- Audio Transcriptions API: https://docs.spring.io/spring-ai/reference/api/audio/transcriptions.html
- Audio Speech API: https://docs.spring.io/spring-ai/reference/api/audio/speech.html

## Shared Architecture References

Common architecture concepts are documented in the root README:

- [DDD layers](../README.md#ddd-layered-architecture)
- [Class vs record](../README.md#java-class-vs-java-record-in-domain-modeling)
- [Strong typed identifiers](../README.md#strong-typed-identifiers)
- [Repository pattern](../README.md#repository-pattern)
- [Use cases and Clean Architecture](../README.md#use-cases-and-clean-architecture)
- [Docker Compose support](../README.md#docker-compose-support-in-development)

## How to Run

Set your OpenAI API key:

```bash
export OPENAI_API_KEY="your_api_key_here"
```

Run the application and tests:

```bash
./gradlew bootRun
./gradlew test
```

## Notes

- Educational final project focused on AI plus architectural discipline.
- External provider integration tests may require active credentials.

## Improvements (Project Challenge)

While working on this project challenge, I expanded its core functionalities by creating a new Use Case that adheres to the existing Domain-Driven Design (DDD) architecture.

**1. Delete Transaction**
- Added a `DELETE /transactions/{id}` route.
- Created the `DeleteTransactionUseCase`, returning rich outputs.
- Implemented a Spring `Converter` to elegantly inject `TransactionId` directly into the Controller, keeping the code clean.
- Added exception handling (returns a clean response when a transaction is not found).

**2. Get Latest Transactions**
- Added a `GET /transactions` endpoint to retrieve a quick overview of the user's latest 10 transactions.
- Added a `createdOn` (timestamp) field to the domain to support chronological operations.
- Implemented a smart database query to fetch strictly the 10 most recent transactions.
- Protected the API and AI token limits by preventing full database scans.

**3. Get Total Amount**
- Added a `GET /transactions/total` endpoint to retrieve the sum of all expenses.
- Implemented a custom JPQL `@Query` with the `SUM()` function in the repository, ensuring the math is calculated efficiently at the database level.
- Handled empty database scenarios by treating potential `null` results.

**4. Get Monthly Transactions**
- Added a `GET /transactions/monthly` endpoint to retrieve transactions for a specific month.
- Implemented optional `@RequestParam` parameters, allowing the system to smartly fallback to the current month/year if the inputs are `null`.
- Used Java's `YearMonth` to dynamically calculate the exact start and end `Instant` of a given month, ensuring accurate database queries regardless of month lengths or leap years.

## How to test the new implementations
You can use the requests below in your HTTP client to test the new methods:
- `DELETE http://localhost:8080/transactions/TRANSACTION_ID` - Delete method
- `GET http://localhost:8080/transactions` - Get latest 10 transactions method
- `GET http://localhost:8080/transactions/total` - Get total amount method
- `GET http://localhost:8080/transactions/monthly` - Get current month transactions (auto-fallback)
- `GET http://localhost:8080/transactions/monthly?month=8` - Get August transactions for the current year
- `GET http://localhost:8080/transactions/monthly?month=7&year=2026` - Get transactions for a specific month and year

## Next steps (Roadmap)
- [ ] Implement AI-driven transaction categorization using Spring AI.