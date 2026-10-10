package com.example.skillgap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class providing deterministic scoring algorithms and proficiency level mapping.
 */
public final class ScoringUtils {

    private static final Logger logger = LoggerFactory.getLogger(ScoringUtils.class);

    private ScoringUtils() {
        // Prevent instantiation
    }

    /**
     * Calculates the score percentage given the count of correct answers and total questions.
     * Formula: (correctCount / totalQuestions) * 100.
     *
     * @param correctCount   the number of correct answers
     * @param totalQuestions the total number of questions evaluated
     * @return score percentage bounded between 0.0 and 100.0
     */
    public static double calculateScorePercentage(int correctCount, int totalQuestions) {
        try {
            if (totalQuestions <= 0) {
                logger.warn("Total questions is less than or equal to zero: {}. Returning 0.0.", totalQuestions);
                return 0.0;
            }
            if (correctCount < 0) {
                logger.warn("Correct count is negative: {}. Clamping to 0.", correctCount);
                correctCount = 0;
            }
            if (correctCount > totalQuestions) {
                logger.warn("Correct count {} exceeds total questions {}. Clamping.", correctCount, totalQuestions);
                correctCount = totalQuestions;
            }
            double score = ((double) correctCount / totalQuestions) * 100.0;
            return Math.round(score * 10.0) / 10.0;
        } catch (Exception ex) {
            System.err.println("ScoringUtils: Error in calculateScorePercentage: " + ex.getMessage());
            logger.error("Failed to calculate score percentage for correctCount={}, totalQuestions={}", correctCount, totalQuestions, ex);
            return 0.0;
        }
    }

    /**
     * Converts a score percentage to a proficiency level from 1 to 5.
     * Ranges:
     *  0–20%   -> Level 1 (Beginner)
     *  21–40%  -> Level 2 (Basic)
     *  41–60%  -> Level 3 (Intermediate)
     *  61–80%  -> Level 4 (Advanced)
     *  81–100% -> Level 5 (Expert)
     *
     * @param scorePercentage the percentage score
     * @return calculated proficiency level (1 to 5)
     */
    public static int convertScoreToProficiencyLevel(double scorePercentage) {
        try {
            if (scorePercentage < 0.0) {
                logger.warn("Score percentage is negative: {}. Clamping to 0.0.", scorePercentage);
                scorePercentage = 0.0;
            }
            if (scorePercentage > 100.0) {
                logger.warn("Score percentage exceeds 100: {}. Clamping to 100.0.", scorePercentage);
                scorePercentage = 100.0;
            }

            if (scorePercentage <= 20.0) {
                return 1;
            } else if (scorePercentage <= 40.0) {
                return 2;
            } else if (scorePercentage <= 60.0) {
                return 3;
            } else if (scorePercentage <= 80.0) {
                return 4;
            } else {
                return 5;
            }
        } catch (Exception ex) {
            System.err.println("ScoringUtils: Error in convertScoreToProficiencyLevel: " + ex.getMessage());
            logger.error("Failed to convert score {} to proficiency level", scorePercentage, ex);
            return 1;
        }
    }

    /**
     * Returns the human-readable proficiency title corresponding to a level 1 to 5.
     *
     * @param level integer level between 1 and 5
     * @return descriptive name (Beginner, Basic, Intermediate, Advanced, Expert)
     */
    public static String getProficiencyTitle(int level) {
        try {
            return switch (level) {
                case 1 -> "Beginner";
                case 2 -> "Basic";
                case 3 -> "Intermediate";
                case 4 -> "Advanced";
                case 5 -> "Expert";
                default -> {
                    logger.warn("Encountered unknown proficiency level: {}. Defaulting to Beginner.", level);
                    yield "Beginner";
                }
            };
        } catch (Exception ex) {
            System.err.println("ScoringUtils: Error in getProficiencyTitle: " + ex.getMessage());
            logger.error("Failed to map proficiency title for level {}", level, ex);
            return "Beginner";
        }
    }
}
