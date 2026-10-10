# BACKEND AGENT SKILL SPECIFICATION: SPRING BOOT & LANGCHAIN4J
=============================================================

This document provides technical instructions, structural maps, and operational guidelines for Autonomous AI Agents and developers maintaining or extending the backend of the **AI-Powered Personalized Learning and Skill Gap Analysis System**.

---

## 1. BACKEND TECHNICAL PROFILE

- **Framework**: Spring Boot 3.3.4 (Java 21 / 27 compatible)
- **Persistence**: Spring Data JPA + Hibernate + MySQL 8.0 Driver
- **Security**: Spring Security 6 with stateless HMAC-SHA256 JWT Bearer token authentication
- **AI Engine**: LangChain4j (`langchain4j-open-ai` 0.35.0)
- **Build Tool**: Apache Maven 3.9+
- **Architecture Pattern**: Strict Layered REST (`Controller` -> `Service` -> `Repository` -> `Entity`)

---

## 2. DIRECTORY STRUCTURE & COMPONENT ROLES

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/skillgap/
│   │   │   ├── SkillGapAnalyzerApplication.java   # Spring Boot entry point with Dotenv loader
│   │   │   ├── config/                            # SecurityConfig (JWT & CORS)
│   │   │   ├── controller/                        # Thin REST controllers (DTO validation)
│   │   │   ├── service/                           # Domain business logic (Scoring, Gaps, Plans)
│   │   │   ├── repository/                        # Spring Data JPA repositories
│   │   │   ├── entity/                            # Relational JPA entities
│   │   │   ├── dto/
│   │   │   │   ├── request/                       # Validated input records
│   │   │   │   └── response/                      # Standardized response payloads
│   │   │   ├── exception/                         # GlobalExceptionHandler (@RestControllerAdvice)
│   │   │   ├── security/                          # JwtTokenProvider, JwtAuthFilter
│   │   │   ├── util/                              # ScoringUtils, SkillGapCalculator
│   │   │   └── ai/
│   │   │       ├── prompt/                        # LearningPlanPromptTemplate
│   │   │       ├── model/                         # AiLearningPlanJson schema
│   │   │       └── service/                       # LangChain4jRecommendationService
│   │   └── resources/
│   │       ├── application.properties             # Environment variable property mappings
│   │       └── db/
│   │           ├── schema.sql                     # Idempotent DDL script
│   │           └── data.sql                       # Seed roles, skills, benchmark levels, & questions
│   └── test/                                      # Unit and integration test suites
├── .env.example                                   # Configuration template
├── pom.xml                                        # Maven dependencies & build plugins
└── README.md                                      # Backend startup and development guide
```

---

## 3. CORE BUSINESS LOGIC & CONSTRAINTS

### A. Quiz Security & Answer Verification
- **Rule**: `Question.correctOption` must NEVER be included in `QuizQuestionResponse`.
- **Location**: `QuizService.generateQuiz()` sanitizes questions before returning to clients.
- **Evaluation**: Client sends `{ questionId, selectedOption }`. The backend queries the authoritative database record, compares answers case-insensitively, and groups results by skill.

### B. Scoring & Level Thresholds
- **Formula**: `(correctCount / totalQuestions) * 100.0`
- **Threshold Mapping**:
  - `0% - 20%`   -> Level 1 (Beginner)
  - `21% - 40%`  -> Level 2 (Basic)
  - `41% - 60%`  -> Level 3 (Intermediate)
  - `61% - 80%`  -> Level 4 (Advanced)
  - `81% - 100%` -> Level 5 (Expert)

### C. Skill Gap Calculation & Prioritization
- **Formula**: `Gap = Required Benchmark Level - Actual Level`
- **Ordering**:
  1. Primary: Highest `Gap` descending (`Integer.compare(b.getGap(), a.getGap())`).
  2. Secondary: Lower quiz score ascending (`Double.compare(scoreA, scoreB)`).
  3. Tertiary: Count of weak topics descending.

### D. LangChain4j Prompting, Groq Integration & 3-Tier Fallback
- **Prompt Builder**: `LearningPlanPromptTemplate.buildUserPrompt()`
- **Tier 1 (Primary - Free)**: Groq API (`llama-3.3-70b-versatile`) via LangChain4j `OpenAiChatModel` pointing to `https://api.groq.com/openai/v1` using `GROQ_API_KEY`.
- **Tier 2 (Fallback - Paid/Testing)**: OpenAI / Gemini (`gpt-4o-mini`) via `AI_API_KEY`.
- **Tier 3 (Safety Net - Deterministic)**: If keys are absent or external services error, `LangChain4jRecommendationService.generateDeterministicPlan()` generates an actionable, high-quality roadmap directly from the student's evaluated gaps and wrong topics. The API call never fails.

---

## 4. ESSENTIAL BACKEND COMMANDS

```powershell
# Set JDK 21 or 27
set JAVA_HOME=C:\Program Files\Java\jdk-27

# Compile and run all unit tests
mvn test

# Run application locally
mvn spring-boot:run
```
