package com.example.skillgap.service;

import com.example.skillgap.dto.response.StudentResponse;
import com.example.skillgap.entity.Student;
import com.example.skillgap.exception.ResourceNotFoundException;
import com.example.skillgap.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service managing student profile retrieval and career objective preferences.
 */
@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves student profile details by unique student ID.
     *
     * @param studentId the student primary key
     * @return StudentResponse DTO without credential exposure
     */
    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long studentId) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        System.err.println("StudentService: Student not found with ID: " + studentId);
                        return new ResourceNotFoundException("Student not found with ID: " + studentId);
                    });

            return StudentResponse.builder()
                    .studentId(student.getStudentId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .careerGoal(student.getCareerGoal())
                    .build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("StudentService: Error in getStudentById: " + ex.getMessage());
            logger.error("Failed to retrieve student with ID: {}", studentId, ex);
            throw new RuntimeException("Error retrieving student profile", ex);
        }
    }

    /**
     * Updates the target career goal on a student profile.
     *
     * @param studentId  the student primary key
     * @param careerGoal the new career goal
     * @return updated StudentResponse DTO
     */
    @Transactional
    public StudentResponse updateCareerGoal(Long studentId, String careerGoal) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        System.err.println("StudentService: Cannot update goal, student not found ID: " + studentId);
                        return new ResourceNotFoundException("Student not found with ID: " + studentId);
                    });

            student.setCareerGoal(careerGoal);
            Student saved = studentRepository.save(student);

            return StudentResponse.builder()
                    .studentId(saved.getStudentId())
                    .name(saved.getName())
                    .email(saved.getEmail())
                    .careerGoal(saved.getCareerGoal())
                    .build();
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("StudentService: Error in updateCareerGoal: " + ex.getMessage());
            logger.error("Failed to update career goal for student: {}", studentId, ex);
            throw new RuntimeException("Error updating career goal", ex);
        }
    }
}
