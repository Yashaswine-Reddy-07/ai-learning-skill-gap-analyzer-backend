package com.example.skillgap;

import com.example.skillgap.util.ScoringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests verifying exact scoring calculations and boundary-value level conversions.
 */
class ScoringUtilsTest {

    @Test
    @DisplayName("Verify score percentage calculation formula (correctCount / totalQuestions) * 100")
    void testCalculateScorePercentage() {
        assertEquals(0.0, ScoringUtils.calculateScorePercentage(0, 5));
        assertEquals(20.0, ScoringUtils.calculateScorePercentage(1, 5));
        assertEquals(40.0, ScoringUtils.calculateScorePercentage(2, 5));
        assertEquals(60.0, ScoringUtils.calculateScorePercentage(3, 5));
        assertEquals(80.0, ScoringUtils.calculateScorePercentage(4, 5));
        assertEquals(100.0, ScoringUtils.calculateScorePercentage(5, 5));
        assertEquals(0.0, ScoringUtils.calculateScorePercentage(0, 0));
    }

    @Test
    @DisplayName("Verify exact score-to-level threshold conversion rules per Section 24 and 71")
    void testConvertScoreToProficiencyLevel() {
        // 0% - 20% -> Level 1 (Beginner)
        assertEquals(1, ScoringUtils.convertScoreToProficiencyLevel(0.0));
        assertEquals(1, ScoringUtils.convertScoreToProficiencyLevel(10.0));
        assertEquals(1, ScoringUtils.convertScoreToProficiencyLevel(20.0));

        // 21% - 40% -> Level 2 (Basic)
        assertEquals(2, ScoringUtils.convertScoreToProficiencyLevel(20.5));
        assertEquals(2, ScoringUtils.convertScoreToProficiencyLevel(21.0));
        assertEquals(2, ScoringUtils.convertScoreToProficiencyLevel(40.0));

        // 41% - 60% -> Level 3 (Intermediate)
        assertEquals(3, ScoringUtils.convertScoreToProficiencyLevel(40.5));
        assertEquals(3, ScoringUtils.convertScoreToProficiencyLevel(41.0));
        assertEquals(3, ScoringUtils.convertScoreToProficiencyLevel(60.0));

        // 61% - 80% -> Level 4 (Advanced)
        assertEquals(4, ScoringUtils.convertScoreToProficiencyLevel(60.5));
        assertEquals(4, ScoringUtils.convertScoreToProficiencyLevel(61.0));
        assertEquals(4, ScoringUtils.convertScoreToProficiencyLevel(80.0));

        // 81% - 100% -> Level 5 (Expert)
        assertEquals(5, ScoringUtils.convertScoreToProficiencyLevel(80.5));
        assertEquals(5, ScoringUtils.convertScoreToProficiencyLevel(81.0));
        assertEquals(5, ScoringUtils.convertScoreToProficiencyLevel(100.0));
    }

    @Test
    @DisplayName("Verify proficiency titles")
    void testGetProficiencyTitle() {
        assertEquals("Beginner", ScoringUtils.getProficiencyTitle(1));
        assertEquals("Basic", ScoringUtils.getProficiencyTitle(2));
        assertEquals("Intermediate", ScoringUtils.getProficiencyTitle(3));
        assertEquals("Advanced", ScoringUtils.getProficiencyTitle(4));
        assertEquals("Expert", ScoringUtils.getProficiencyTitle(5));
    }
}
