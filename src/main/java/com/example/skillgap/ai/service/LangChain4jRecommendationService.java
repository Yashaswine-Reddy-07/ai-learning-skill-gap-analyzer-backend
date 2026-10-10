package com.example.skillgap.ai.service;

import com.example.skillgap.ai.model.AiLearningPlanJson;
import com.example.skillgap.ai.model.AiLearningPlanSkill;
import com.example.skillgap.ai.prompt.LearningPlanPromptTemplate;
import com.example.skillgap.dto.response.SkillGapResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Service managing LangChain4j integration to generate personalized learning recommendations.
 *
 * <p><b>Provider priority chain:</b></p>
 * <ol>
 *   <li><b>PRIMARY — Groq (free tier):</b> Uses Groq's OpenAI-compatible API with models
 *       like {@code llama-3.3-70b-versatile}. This is the main source for AI-generated
 *       learning plans. Groq is free and requires only a free API key from
 *       <a href="https://console.groq.com/keys">https://console.groq.com/keys</a>.</li>
 *   <li><b>FALLBACK — OpenAI / Gemini (paid):</b> If Groq is not configured or fails,
 *       the service falls back to the OpenAI-compatible provider (e.g. GPT-4o-mini).
 *       This requires a paid API key and is intended only for testing or backup.</li>
 *   <li><b>SAFETY NET — Deterministic generator:</b> If both external providers fail or
 *       are not configured, a high-quality rule-based plan is synthesized locally using
 *       the student's assessed gaps, weak topics, and role context. This ensures the
 *       application never fails — a learning plan is always returned.</li>
 * </ol>
 *
 * <p><b>How the API connection works:</b></p>
 * <p>Both Groq and OpenAI expose OpenAI-compatible REST APIs. LangChain4j's
 * {@link OpenAiChatModel} sends an HTTP POST to the provider's {@code /chat/completions}
 * endpoint with a system message (persona + rules) and a user message (student's skill
 * gaps, scores, weak topics, target role). The provider returns a JSON learning plan
 * which is parsed into {@link AiLearningPlanJson}.</p>
 *
 * <p>The only difference between Groq and OpenAI in code is the {@code baseUrl}:
 * Groq uses {@code https://api.groq.com/openai/v1}, while OpenAI uses the default
 * {@code https://api.openai.com/v1}.</p>
 */
@Service
public class LangChain4jRecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(LangChain4jRecommendationService.class);

    // ---- PRIMARY: Groq (free) ----
    @Value("${app.ai.groq.api-key:}")
    private String groqApiKey;

    @Value("${app.ai.groq.model-name:llama-3.3-70b-versatile}")
    private String groqModelName;

    @Value("${app.ai.groq.base-url:https://api.groq.com/openai/v1}")
    private String groqBaseUrl;

    @Value("${app.ai.groq.timeout-seconds:30}")
    private int groqTimeoutSeconds;

    // ---- FALLBACK: OpenAI / Gemini (paid) ----
    @Value("${app.ai.fallback.api-key:}")
    private String fallbackApiKey;

    @Value("${app.ai.fallback.model-name:gpt-4o-mini}")
    private String fallbackModelName;

    @Value("${app.ai.fallback.timeout-seconds:30}")
    private int fallbackTimeoutSeconds;

    private final ObjectMapper objectMapper;

    /**
     * Constructs the recommendation service with a Jackson ObjectMapper for JSON parsing.
     *
     * @param objectMapper Spring-managed ObjectMapper instance
     */
    public LangChain4jRecommendationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Generates a structured personalized learning plan using the 3-tier provider chain.
     *
     * <p><b>Execution order:</b></p>
     * <ol>
     *   <li>Try Groq (primary, free) — if GROQ_API_KEY is set and valid</li>
     *   <li>Try OpenAI/Gemini (fallback, paid) — if AI_API_KEY is set and valid</li>
     *   <li>Generate deterministic plan (safety net) — always succeeds</li>
     * </ol>
     *
     * @param targetRole the student's target career role
     * @param gaps       prioritized list of assessed skill gaps
     * @return validated AiLearningPlanJson structure (never null)
     */
    public AiLearningPlanJson generatePlan(String targetRole, List<SkillGapResponse> gaps) {
        // ---- TIER 1: Groq (Primary, Free) ----
        if (isValidApiKey(groqApiKey)) {
            try {
                logger.info("TIER 1 — Attempting Groq AI call | model={} | baseUrl={}", groqModelName, groqBaseUrl);
                System.out.println("LangChain4jRecommendationService: Calling Groq (primary) with model: " + groqModelName);

                AiLearningPlanJson groqResult = callProvider(groqApiKey, groqModelName, groqBaseUrl, groqTimeoutSeconds, targetRole, gaps);
                if (groqResult != null && groqResult.getSkills() != null && !groqResult.getSkills().isEmpty()) {
                    logger.info("TIER 1 — Groq succeeded. Returning AI-generated learning plan.");
                    System.out.println("LangChain4jRecommendationService: Groq call SUCCEEDED.");
                    return groqResult;
                }
                logger.warn("TIER 1 — Groq returned empty/null result. Falling through to TIER 2.");
            } catch (Exception ex) {
                System.err.println("LangChain4jRecommendationService: Groq call FAILED: " + ex.getMessage());
                logger.warn("TIER 1 — Groq provider error: {}. Falling through to TIER 2.", ex.getMessage());
            }
        } else {
            logger.info("TIER 1 — Groq API key not configured. Skipping to TIER 2.");
            System.out.println("LangChain4jRecommendationService: GROQ_API_KEY not set. Skipping Groq.");
        }

        // ---- TIER 2: OpenAI / Gemini (Fallback, Paid) ----
        if (isValidApiKey(fallbackApiKey)) {
            try {
                logger.info("TIER 2 — Attempting OpenAI/Gemini fallback | model={}", fallbackModelName);
                System.out.println("LangChain4jRecommendationService: Calling OpenAI fallback with model: " + fallbackModelName);

                // null baseUrl = LangChain4j uses default OpenAI endpoint (https://api.openai.com/v1)
                AiLearningPlanJson fallbackResult = callProvider(fallbackApiKey, fallbackModelName, null, fallbackTimeoutSeconds, targetRole, gaps);
                if (fallbackResult != null && fallbackResult.getSkills() != null && !fallbackResult.getSkills().isEmpty()) {
                    logger.info("TIER 2 — OpenAI/Gemini fallback succeeded. Returning AI-generated learning plan.");
                    System.out.println("LangChain4jRecommendationService: OpenAI fallback call SUCCEEDED.");
                    return fallbackResult;
                }
                logger.warn("TIER 2 — OpenAI fallback returned empty/null result. Falling through to TIER 3.");
            } catch (Exception ex) {
                System.err.println("LangChain4jRecommendationService: OpenAI fallback FAILED: " + ex.getMessage());
                logger.warn("TIER 2 — OpenAI/Gemini fallback error: {}. Falling through to TIER 3.", ex.getMessage());
            }
        } else {
            logger.info("TIER 2 — OpenAI/Gemini fallback API key not configured. Skipping to TIER 3.");
            System.out.println("LangChain4jRecommendationService: AI_API_KEY not set. Skipping OpenAI fallback.");
        }

        // ---- TIER 3: Deterministic Safety Net ----
        logger.info("TIER 3 — Generating deterministic learning plan (no external AI).");
        System.out.println("LangChain4jRecommendationService: Using deterministic plan generator (TIER 3 safety net).");
        return generateDeterministicPlan(targetRole, gaps);
    }

    /**
     * Checks whether an API key is configured and not a placeholder value.
     *
     * @param key the API key string to validate
     * @return true if key is usable, false if empty/null/placeholder
     */
    private boolean isValidApiKey(String key) {
        try {
            if (key == null || key.isBlank()) return false;
            String lower = key.trim().toLowerCase();
            return !lower.contains("your_") && !lower.contains("mock") && !lower.contains("placeholder") && !lower.contains("_here");
        } catch (Exception ex) {
            System.err.println("LangChain4jRecommendationService: Error validating API key: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Executes a LangChain4j ChatLanguageModel invocation against any OpenAI-compatible provider.
     *
     * <p>Both Groq and OpenAI use the same {@link OpenAiChatModel} class. The only
     * difference is the {@code baseUrl}:</p>
     * <ul>
     *   <li>Groq: {@code https://api.groq.com/openai/v1}</li>
     *   <li>OpenAI: {@code null} (LangChain4j defaults to {@code https://api.openai.com/v1})</li>
     * </ul>
     *
     * <p><b>What happens under the hood:</b></p>
     * <ol>
     *   <li>LangChain4j builds an HTTP POST request to {@code {baseUrl}/chat/completions}</li>
     *   <li>The request body contains the system prompt (AI persona + rules) and user prompt
     *       (student's skill gaps, scores, weak topics, target role, and JSON schema)</li>
     *   <li>The provider returns a JSON response with the completion text</li>
     *   <li>We extract the text content from the response</li>
     *   <li>We strip any markdown fences ({@code ```json ... ```}) the LLM may have wrapped around it</li>
     *   <li>We parse the clean JSON into {@link AiLearningPlanJson}</li>
     * </ol>
     *
     * @param apiKey         the provider's API key
     * @param modelName      the model identifier (e.g. "llama-3.3-70b-versatile" or "gpt-4o-mini")
     * @param baseUrl        the provider's base URL (null for default OpenAI)
     * @param timeoutSeconds request timeout in seconds
     * @param targetRole     the student's target career role
     * @param gaps           the prioritized skill gaps
     * @return parsed AiLearningPlanJson, or null if parsing fails
     */
    private AiLearningPlanJson callProvider(String apiKey, String modelName, String baseUrl,
                                            int timeoutSeconds, String targetRole, List<SkillGapResponse> gaps) {
        try {
            // Build the LangChain4j model pointing at the correct provider
            OpenAiChatModel.OpenAiChatModelBuilder builder = OpenAiChatModel.builder()
                    .apiKey(apiKey.trim())
                    .modelName(modelName.trim())
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .temperature(0.3);

            // Set baseUrl only for non-OpenAI providers (e.g. Groq)
            if (baseUrl != null && !baseUrl.isBlank()) {
                builder.baseUrl(baseUrl.trim());
                logger.debug("Provider baseUrl set to: {}", baseUrl);
            }

            ChatLanguageModel chatModel = builder.build();

            // Build the structured prompt
            String systemPrompt = LearningPlanPromptTemplate.getSystemPrompt();
            String userPrompt = LearningPlanPromptTemplate.buildUserPrompt(targetRole, gaps);

            logger.debug("Sending prompt to provider. System prompt length={}, User prompt length={}",
                    systemPrompt.length(), userPrompt.length());

            // Execute the API call — this is where the HTTP POST happens
            String response = chatModel.generate(
                    SystemMessage.from(systemPrompt),
                    UserMessage.from(userPrompt)
            ).content().text();

            logger.debug("Raw provider response length: {}", response != null ? response.length() : 0);

            // Clean and parse the JSON response
            String cleanedJson = cleanJsonResponse(response);
            AiLearningPlanJson result = objectMapper.readValue(cleanedJson, AiLearningPlanJson.class);

            logger.info("Successfully parsed AI response with {} skill entries.",
                    result.getSkills() != null ? result.getSkills().size() : 0);

            return result;
        } catch (Exception ex) {
            System.err.println("LangChain4jRecommendationService: Provider communication error: " + ex.getMessage());
            logger.error("Provider call failed for model={}, baseUrl={}", modelName, baseUrl, ex);
            return null;
        }
    }

    /**
     * Sanitizes raw LLM output, stripping markdown code fences if present.
     * Many LLMs wrap JSON responses in ```json ... ``` blocks which must be removed
     * before Jackson can parse the content.
     *
     * @param rawResponse raw text from the model
     * @return pure JSON string suitable for ObjectMapper parsing
     */
    private String cleanJsonResponse(String rawResponse) {
        try {
            if (rawResponse == null) return "{}";
            String trimmed = rawResponse.trim();
            if (trimmed.startsWith("```json")) {
                trimmed = trimmed.substring(7);
            } else if (trimmed.startsWith("```")) {
                trimmed = trimmed.substring(3);
            }
            if (trimmed.endsWith("```")) {
                trimmed = trimmed.substring(0, trimmed.length() - 3);
            }
            return trimmed.trim();
        } catch (Exception ex) {
            System.err.println("LangChain4jRecommendationService: Error cleaning JSON response: " + ex.getMessage());
            logger.error("JSON cleanup failed", ex);
            return rawResponse != null ? rawResponse : "{}";
        }
    }

    /**
     * Synthesizes a high-quality deterministic learning plan tailored to the student's
     * assessed skill gaps, scores, and missed topics. This is the TIER 3 safety net
     * that guarantees the application always returns a usable learning plan, even when
     * both Groq and OpenAI/Gemini are unavailable.
     *
     * @param targetRole the target role name
     * @param gaps       the prioritized skill gaps
     * @return populated AiLearningPlanJson (never null)
     */
    public AiLearningPlanJson generateDeterministicPlan(String targetRole, List<SkillGapResponse> gaps) {
        try {
            List<AiLearningPlanSkill> skills = new ArrayList<>();
            int maxPriority = 1;

            for (SkillGapResponse gap : gaps) {
                String skillName = gap.getSkillName();
                List<String> topics = gap.getWeakTopics() != null && !gap.getWeakTopics().isEmpty()
                        ? gap.getWeakTopics()
                        : List.of("Core Fundamentals", "Applied Problem Solving");

                List<String> steps = new ArrayList<>();
                steps.add("Review official documentation and core concepts for " + skillName + " (" + String.join(", ", topics) + ").");
                steps.add("Complete guided exercises strengthening comprehension in: " + String.join(", ", topics) + ".");
                steps.add("Analyze edge cases and code optimization patterns in " + skillName + ".");

                List<String> practice = new ArrayList<>();
                practice.add("Solve 5-10 targeted practice problems addressing " + String.join(" and ", topics) + ".");
                practice.add("Implement sample code snippets demonstrating error handling and best practices.");

                String miniProject = "Develop a lightweight application module utilizing " + skillName + " focused on " +
                        (topics.isEmpty() ? "practical automation" : topics.get(0)) + " aligned with " + targetRole + " standards.";

                String revision = "Conduct a 45-minute self-test on " + skillName + " key terminology and practical syntax before re-attempting.";

                skills.add(AiLearningPlanSkill.builder()
                        .skill(skillName)
                        .priority(gap.getPriority() != null ? gap.getPriority() : maxPriority)
                        .currentLevel(gap.getCurrentLevel())
                        .requiredLevel(gap.getRequiredLevel())
                        .topics(topics)
                        .learningSteps(steps)
                        .practice(practice)
                        .miniProject(miniProject)
                        .revision(revision)
                        .build());

                maxPriority++;
            }

            String summary = String.format(
                    "Personalized roadmap generated for %s. Focus primarily on high-priority gaps with objective deficits before retaking.",
                    targetRole
            );

            String retake = "Complete the top priority practice tasks and mini-projects over 5-7 days before retaking the assessment.";

            logger.info("Deterministic plan generated with {} skill entries for role: {}", skills.size(), targetRole);

            return AiLearningPlanJson.builder()
                    .role(targetRole)
                    .summary(summary)
                    .skills(skills)
                    .retakeRecommendation(retake)
                    .build();
        } catch (Exception ex) {
            System.err.println("LangChain4jRecommendationService: Error generating deterministic plan: " + ex.getMessage());
            logger.error("Failed to build deterministic learning plan", ex);
            return AiLearningPlanJson.builder()
                    .role(targetRole)
                    .summary("Personalized learning curriculum.")
                    .skills(List.of())
                    .retakeRecommendation("Review core topics and retake the quiz.")
                    .build();
        }
    }
}
