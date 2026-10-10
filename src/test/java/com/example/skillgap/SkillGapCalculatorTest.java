package com.example.skillgap;

import com.example.skillgap.dto.response.SkillGapResponse;
import com.example.skillgap.util.SkillGapCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests verifying skill gap calculation and deterministic prioritization ordering.
 */
class SkillGapCalculatorTest {

    @Test
    @DisplayName("Verify skill gap formula: Gap = Required Proficiency - Quiz Proficiency (Section 25 & 71)")
    void testCalculateGap() {
        // Required 4, Actual 2 -> Gap 2
        assertEquals(2, SkillGapCalculator.calculateGap(4, 2));

        // Required 4, Actual 4 -> Gap 0
        assertEquals(0, SkillGapCalculator.calculateGap(4, 4));

        // Required 3, Actual 4 -> Gap -1
        assertEquals(-1, SkillGapCalculator.calculateGap(3, 4));

        // Verify improvement need determination
        assertTrue(SkillGapCalculator.requiresImprovement(2));
        assertFalse(SkillGapCalculator.requiresImprovement(0));
        assertFalse(SkillGapCalculator.requiresImprovement(-1));
    }

    @Test
    @DisplayName("Verify skill gap prioritization: largest gap first, then lower score (Section 26)")
    void testPrioritizeGaps() {
        List<SkillGapResponse> list = new ArrayList<>();

        list.add(SkillGapResponse.builder()
                .skillName("Python")
                .gap(1)
                .score(60.0)
                .weakTopics(List.of("Functions"))
                .build());

        list.add(SkillGapResponse.builder()
                .skillName("SQL")
                .gap(2)
                .score(40.0)
                .weakTopics(List.of("JOIN", "Subqueries"))
                .build());

        list.add(SkillGapResponse.builder()
                .skillName("Statistics")
                .gap(2)
                .score(35.0)
                .weakTopics(List.of("Normal Distribution"))
                .build());

        list.add(SkillGapResponse.builder()
                .skillName("Power BI")
                .gap(1)
                .score(70.0)
                .weakTopics(List.of())
                .build());

        List<SkillGapResponse> prioritized = SkillGapCalculator.prioritizeGaps(list);

        // Gap 2 skills must come before Gap 1 skills
        assertEquals("Statistics", prioritized.get(0).getSkillName(), "Lower score (35%) between Gap 2 skills should be Priority 1");
        assertEquals(1, prioritized.get(0).getPriority());

        assertEquals("SQL", prioritized.get(1).getSkillName(), "Higher score (40%) between Gap 2 skills should be Priority 2");
        assertEquals(2, prioritized.get(1).getPriority());

        assertEquals("Python", prioritized.get(2).getSkillName(), "Lower score (60%) between Gap 1 skills should be Priority 3");
        assertEquals(3, prioritized.get(2).getPriority());

        assertEquals("Power BI", prioritized.get(3).getSkillName(), "Higher score (70%) between Gap 1 skills should be Priority 4");
        assertEquals(4, prioritized.get(3).getPriority());
    }
}
