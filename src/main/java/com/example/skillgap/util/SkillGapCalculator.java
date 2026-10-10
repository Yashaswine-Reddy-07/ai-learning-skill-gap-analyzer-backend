package com.example.skillgap.util;

import com.example.skillgap.dto.response.SkillGapResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

/**
 * Utility class providing calculations and deterministic prioritization for student skill gaps.
 */
public final class SkillGapCalculator {

    private static final Logger logger = LoggerFactory.getLogger(SkillGapCalculator.class);

    private SkillGapCalculator() {
        // Prevent instantiation
    }

    /**
     * Calculates the raw skill gap: Required Proficiency - Actual Quiz Proficiency.
     *
     * @param requiredLevel benchmark level required for the target career role
     * @param actualLevel   assessed level of the student
     * @return gap value (positive means deficit; zero or negative means sufficient)
     */
    public static int calculateGap(int requiredLevel, int actualLevel) {
        try {
            int gap = requiredLevel - actualLevel;
            return gap;
        } catch (Exception ex) {
            System.err.println("SkillGapCalculator: Error calculating gap: " + ex.getMessage());
            logger.error("Failed to calculate skill gap for requiredLevel={}, actualLevel={}", requiredLevel, actualLevel, ex);
            return 0;
        }
    }

    /**
     * Determines whether a given skill gap indicates that the student needs improvement.
     *
     * @param gap the computed skill gap
     * @return true if gap > 0 (needs improvement), false otherwise
     */
    public static boolean requiresImprovement(int gap) {
        try {
            return gap > 0;
        } catch (Exception ex) {
            System.err.println("SkillGapCalculator: Error in requiresImprovement: " + ex.getMessage());
            logger.error("Error evaluating requiresImprovement for gap={}", gap, ex);
            return false;
        }
    }

    /**
     * Sorts a list of SkillGapResponse items deterministically according to strict prioritization rules:
     * 1. Largest gap first (descending).
     * 2. Lower quiz score (ascending).
     * 3. Number of weak topics (descending).
     *
     * @param gaps the mutable or immutable list of gaps to prioritize
     * @return sorted list of gaps with priority sequence numbers assigned (1, 2, 3...)
     */
    public static List<SkillGapResponse> prioritizeGaps(List<SkillGapResponse> gaps) {
        try {
            if (gaps == null || gaps.isEmpty()) {
                return List.of();
            }

            Comparator<SkillGapResponse> comparator = (a, b) -> {
                int gapCompare = Integer.compare(b.getGap(), a.getGap()); // Largest gap first
                if (gapCompare != 0) return gapCompare;

                double scoreA = a.getScore() != null ? a.getScore() : 0.0;
                double scoreB = b.getScore() != null ? b.getScore() : 0.0;
                int scoreCompare = Double.compare(scoreA, scoreB); // Lower score first
                if (scoreCompare != 0) return scoreCompare;

                int topicsA = a.getWeakTopics() != null ? a.getWeakTopics().size() : 0;
                int topicsB = b.getWeakTopics() != null ? b.getWeakTopics().size() : 0;
                return Integer.compare(topicsB, topicsA); // More weak topics first
            };

            List<SkillGapResponse> sorted = gaps.stream()
                    .sorted(comparator)
                    .toList();

            // Assign priority sequence (1 to N)
            for (int i = 0; i < sorted.size(); i++) {
                sorted.get(i).setPriority(i + 1);
            }

            return sorted;
        } catch (Exception ex) {
            System.err.println("SkillGapCalculator: Error prioritizing gaps: " + ex.getMessage());
            logger.error("Failed to prioritize skill gaps list", ex);
            return gaps;
        }
    }
}
