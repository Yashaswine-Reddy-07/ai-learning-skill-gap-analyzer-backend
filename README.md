# Skill Gap Analyzer — Backend Service
## Spring Boot 3 + LangChain4j + MySQL REST API

The backend service is responsible for objective quiz generation, answer verification, deterministic scoring, skill gap variance computation, LangChain4j AI prompt construction, and persistent data storage.

---

## 1. Prerequisites

- **Java JDK**: Version 21 or Version 27 (configured in `JAVA_HOME`)
- **Apache Maven**: Version 3.9+
- **MySQL**: Version 8.0 running on localhost:3306

---

## 2. Environment Configuration

Create a `.env` file in the `backend/` directory by copying `.env.example`:

```bash
cp .env.example .env
```

Configuration properties:
```env
DB_URL=jdbc:mysql://localhost:3306/skill_gap_analyzer?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_database_password
SERVER_PORT=8080
FRONTEND_URL=http://localhost:5173
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
JWT_EXPIRATION_MS=86400000
# PRIMARY AI: Groq (free tier — https://console.groq.com/keys)
GROQ_API_KEY=your_groq_api_key_here
GROQ_MODEL_NAME=llama-3.3-70b-versatile
GROQ_BASE_URL=https://api.groq.com/openai/v1

# FALLBACK AI: OpenAI / Gemini (paid, testing fallback)
AI_API_KEY=your_openai_or_gemini_api_key
AI_MODEL_NAME=gpt-4o-mini
```

---

## 3. Running the Service

### Run Tests
```powershell
# Using Maven wrapper (works out of the box, zero setup needed):
.\mvnw test

# Or using global Maven:
mvn test
```

### Start the Application
```powershell
# Using Maven wrapper (works out of the box, zero setup needed):
.\mvnw spring-boot:run

# Or using global Maven:
mvn spring-boot:run
```

The service will start on port `8080`.
The database schema (`db/schema.sql`) and seed data (`db/data.sql`) will initialize automatically on first startup.

---

## 4. Key Packages

- `config`: Spring Security filter chain, CORS configuration, and JWT filter.
- `controller`: Thin REST controllers exposing endpoints and validating payloads.
- `service`: Domain business services (`QuizService`, `SkillGapService`, `LearningPlanService`).
- `repository`: Spring Data JPA interfaces.
- `entity`: Relational database models.
- `dto`: Request and response transfer objects.
- `exception`: Global `@RestControllerAdvice` exception handler sanitizing stack traces.
- `security`: HMAC-SHA256 JWT generator and validator.
- `util`: Objective scoring formula and deterministic gap prioritization logic.
- `ai`: LangChain4j prompt templates, structured output models, and chat model service.

---

## 5. Agent Reference

For automated agent instructions, consult [`backend/agent/Skill.md`](file:///backend/agent/Skill.md).
