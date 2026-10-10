package com.example.skillgap.service;

import com.example.skillgap.dto.request.LoginRequest;
import com.example.skillgap.dto.request.RegisterRequest;
import com.example.skillgap.dto.response.AuthResponse;
import com.example.skillgap.entity.Student;
import com.example.skillgap.exception.BadRequestException;
import com.example.skillgap.exception.UnauthorizedException;
import com.example.skillgap.repository.StudentRepository;
import com.example.skillgap.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service orchestrating student registration, credential validation, and JWT token issuance.
 */
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthService(StudentRepository studentRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    /**
     * Registers a new student, securely hashing their password with BCrypt.
     *
     * @param request the registration request DTO
     * @return AuthResponse containing generated JWT and profile metadata
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            if (studentRepository.existsByEmail(request.getEmail())) {
                System.err.println("AuthService: Registration rejected, email already in use: " + request.getEmail());
                throw new BadRequestException("An account with this email already exists: " + request.getEmail());
            }

            Student student = Student.builder()
                    .name(request.getName().trim())
                    .email(request.getEmail().trim().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .careerGoal(request.getCareerGoal() != null ? request.getCareerGoal().trim() : "Data Analyst")
                    .build();

            Student saved = studentRepository.save(student);
            String token = tokenProvider.generateToken(saved.getEmail());

            return AuthResponse.builder()
                    .token(token)
                    .studentId(saved.getStudentId())
                    .name(saved.getName())
                    .email(saved.getEmail())
                    .careerGoal(saved.getCareerGoal())
                    .build();
        } catch (BadRequestException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("AuthService: Error in register: " + ex.getMessage());
            logger.error("Student registration failed for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Could not complete student registration", ex);
        }
    }

    /**
     * Authenticates existing student credentials and issues a valid JWT Bearer token.
     *
     * @param request the login request DTO
     * @return AuthResponse containing token and user profile
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            Student student = studentRepository.findByEmail(request.getEmail().trim().toLowerCase())
                    .orElseThrow(() -> {
                        System.err.println("AuthService: Login failed, student not found for email: " + request.getEmail());
                        return new UnauthorizedException("Invalid email or password");
                    });

            if (!passwordEncoder.matches(request.getPassword(), student.getPassword())) {
                System.err.println("AuthService: Login failed, password mismatch for email: " + request.getEmail());
                throw new UnauthorizedException("Invalid email or password");
            }

            String token = tokenProvider.generateToken(student.getEmail());

            return AuthResponse.builder()
                    .token(token)
                    .studentId(student.getStudentId())
                    .name(student.getName())
                    .email(student.getEmail())
                    .careerGoal(student.getCareerGoal())
                    .build();
        } catch (UnauthorizedException ex) {
            throw ex;
        } catch (Exception ex) {
            System.err.println("AuthService: Error in login: " + ex.getMessage());
            logger.error("Authentication error for email: {}", request.getEmail(), ex);
            throw new RuntimeException("Could not authenticate student", ex);
        }
    }
}
