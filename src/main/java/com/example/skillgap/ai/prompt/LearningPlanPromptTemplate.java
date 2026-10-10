package com.example.skillgap.ai.prompt;

import com.example.skillgap.dto.response.SkillGapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Prompt engineering template for LangChain4j personalized learning plan generation.
 * Encapsulates role requirements, current proficiencies, gaps, weak topics, and output schema.
 */
public final class LearningPlanPromptTemplate {

    private static final Logger logger = LoggerFactory.getLogger(LearningPlanPromptTemplate.class);

    private LearningPlanPromptTemplate() {
        // Prevent instantiation
    }

    /**
     * Builds the system prompt guiding the LLM persona, responsibilities, and formatting rules.
     *
     * @return system prompt string
     */
    public static String getSystemPrompt() {
        return """
                You are an expert AI Technical Career Mentor and Curriculum Designer in an AI-Powered Skill Gap Analyzer.
                Your task is to analyze objective skill gaps identified from technical quizzes and create a structured,
                step-by-step personalized learning plan.

                CRITICAL CONSTRAINTS:
                1. You must NOT alter scores, proficiency levels, or skill gaps.
                2. You must address the exact weak topics identified from the student's missed questions.
                3. You MUST respond with ONLY valid JSON matching the exact schema specified below. Do not wrap in markdown or backticks.
                4. Tone: Encouraging, rigorous, and highly actionable.
                """;
    }

    /**
     * Constructs the structured user prompt with student's career role, skill gaps, and weak topics.
     *
     * @param targetRole the target career role (e.g. Data Analyst)
     * @param gaps       the prioritized skill gaps
     * @return formatted prompt string ready for LLM consumption
     */
    public static String buildUserPrompt(String targetRole, List<SkillGapResponse> gaps) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Target Career Role: ").append(targetRole).append("\n\n");
            sb.append("ASSESSED SKILL GAPS (Sorted by Priority):\n");

            for (SkillGapResponse gap : gaps) {
                sb.append(String.format("- Priority %d | Skill: %s\n", gap.getPriority(), gap.getSkillName()));
                sb.append(String.format("  Required Level: %d | Assessed Level: %d | Score: %.1f%% | Gap: %d\n",
                        gap.getRequiredLevel(), gap.getCurrentLevel(), gap.getScore(), gap.getGap()));
                if (gap.getWeakTopics() != null && !gap.getWeakTopics().isEmpty()) {
                    sb.append("  Identified Weak Topics: ").append(String.join(", ", gap.getWeakTopics())).append("\n");
                }
            }

            sb.append("\nGenerate a personalized curriculum in the following JSON format:\n");
            sb.append("""
                    {
                      "role": "%s",
                      "summary": "Brief 2-3 sentence overview of the student's readiness and primary focus areas.",
                      "skills": [
                        {
                          "skill": "Skill Name",
                          "priority": 1,
                          "currentLevel": 2,
                          "requiredLevel": 4,
                          "topics": ["Topic 1", "Topic 2"],
                          "learningSteps": ["Study concept X", "Complete module Y"],
                          "practice": ["Hands-on lab Z", "LeetCode/HackerRank exercise"],
                          "miniProject": "Build a small portfolio project applying these concepts",
                          "revision": "Key review checklist before retaking the assessment"
                        }
                      ],
                      "retakeRecommendation": "Specific recommendation on when to retake the quiz (e.g. after completing top 2 priorities in 7 days)."
                    }
                    """.formatted(targetRole));

            return sb.toString();
        } catch (Exception ex) {
            System.err.println("LearningPlanPromptTemplate: Error building prompt: " + ex.getMessage());
            logger.error("Failed to construct prompt for role {}", targetRole, ex);
            return "Target Career Role: " + targetRole;
        }
    }
}
