package com.example.skillgap;

import com.example.skillgap.dto.request.QuizAnswerDto;
import com.example.skillgap.dto.request.QuizSubmitRequest;
import com.example.skillgap.dto.response.QuizQuestionResponse;
import com.example.skillgap.dto.response.QuizResponse;
import com.example.skillgap.dto.response.QuizResultResponse;
import com.example.skillgap.entity.*;
import com.example.skillgap.repository.*;
import com.example.skillgap.service.QuizService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests verifying QuizService generation, scoring, and response confidentiality.
 */
@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleRequirementRepository roleRequirementRepository;
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private QuizAttemptRepository quizAttemptRepository;
    @Mock
    private QuizResultRepository quizResultRepository;
    @Mock
    private StudentSkillRepository studentSkillRepository;

    @InjectMocks
    private QuizService quizService;

    private Role mockRole;
    private Skill mockSkill;
    private RoleRequirement mockReq;
    private Question mockQuestion1;
    private Question mockQuestion2;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockRole = Role.builder().roleId(1L).roleName("Data Analyst").build();
        mockSkill = Skill.builder().skillId(10L).skillName("SQL").category("Database").build();
        mockReq = RoleRequirement.builder().requirementId(100L).role(mockRole).skill(mockSkill).requiredLevel(4).build();

        mockQuestion1 = Question.builder()
                .questionId(101L)
                .skill(mockSkill)
                .questionText("What does LEFT JOIN return?")
                .optionA("Only matched rows")
                .optionB("All left table rows and matched right table rows")
                .optionC("Cartesian product")
                .optionD("None of the above")
                .correctOption("B")
                .difficulty("EASY")
                .topic("JOIN")
                .build();

        mockQuestion2 = Question.builder()
                .questionId(102L)
                .skill(mockSkill)
                .questionText("What clause filters aggregates?")
                .optionA("WHERE")
                .optionB("HAVING")
                .optionC("GROUP BY")
                .optionD("ORDER BY")
                .correctOption("B")
                .difficulty("MEDIUM")
                .topic("GROUP BY")
                .build();

        mockStudent = Student.builder()
                .studentId(50L)
                .name("Alex Johnson")
                .email("alex@example.com")
                .password("hashed_pwd")
                .careerGoal("Data Analyst")
                .build();
    }

    @Test
    @DisplayName("Verify Quiz Generation does NOT leak correctOption to frontend (Security Rule 21)")
    void testGenerateQuizExcludesCorrectOption() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(mockRole));
        when(roleRequirementRepository.findByRole_RoleId(1L)).thenReturn(List.of(mockReq));
        when(questionRepository.findBySkill_SkillId(10L)).thenReturn(List.of(mockQuestion1, mockQuestion2));

        QuizResponse response = quizService.generateQuiz(1L);

        assertNotNull(response);
        assertEquals(1L, response.getRoleId());
        assertEquals("Data Analyst", response.getRoleName());
        assertEquals(2, response.getTotalQuestions());

        for (QuizQuestionResponse q : response.getQuestions()) {
            assertNotNull(q.getQuestionId());
            assertNotNull(q.getQuestionText());
            assertNotNull(q.getOptionA());
            assertNotNull(q.getOptionB());
            assertNotNull(q.getOptionC());
            assertNotNull(q.getOptionD());
            // QuizQuestionResponse does not have a correctOption field at all!
        }
    }

    @Test
    @DisplayName("Verify Quiz Submission calculates correct percentage, level, gap, and wrong topics")
    void testSubmitQuizCalculations() {
        when(studentRepository.findById(50L)).thenReturn(Optional.of(mockStudent));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(mockRole));
        when(roleRequirementRepository.findByRole_RoleId(1L)).thenReturn(List.of(mockReq));
        when(questionRepository.findAllById(any())).thenReturn(List.of(mockQuestion1, mockQuestion2));

        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenAnswer(invocation -> {
            QuizAttempt attempt = invocation.getArgument(0);
            attempt.setAttemptId(999L);
            return attempt;
        });

        // Student answers Q1 correctly ("B") and Q2 incorrectly ("A")
        QuizSubmitRequest submitReq = QuizSubmitRequest.builder()
                .studentId(50L)
                .roleId(1L)
                .answers(List.of(
                        QuizAnswerDto.builder().questionId(101L).selectedOption("B").build(),
                        QuizAnswerDto.builder().questionId(102L).selectedOption("A").build()
                ))
                .build();

        QuizResultResponse result = quizService.submitQuiz(submitReq);

        assertNotNull(result);
        assertEquals(999L, result.getAttemptId());
        assertEquals(50.0, result.getOverallScore(), "1 out of 2 correct should equal 50.0%");
        assertEquals(3, result.getOverallLevel(), "50% falls in Level 3 (Intermediate)");

        assertEquals(1, result.getSkills().size());
        var skillResult = result.getSkills().get(0);
        assertEquals("SQL", skillResult.getSkillName());
        assertEquals(50.0, skillResult.getScore());
        assertEquals(3, skillResult.getLevel());
        assertEquals(4, skillResult.getRequiredLevel());
        assertEquals(1, skillResult.getGap(), "Required 4 - Level 3 = Gap 1");
        assertTrue(skillResult.getWrongTopics().contains("GROUP BY"), "Missed question topic should be detected");

        verify(quizAttemptRepository, times(1)).save(any(QuizAttempt.class));
        verify(quizResultRepository, times(1)).saveAll(any());
    }
}
