package com.example.skillgap.security;

import com.example.skillgap.entity.Student;
import com.example.skillgap.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom Spring Security UserDetailsService implementation fetching student principals by email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final StudentRepository studentRepository;

    public CustomUserDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Locates the user based on the email username.
     *
     * @param email the email identifying the student whose data is required
     * @return fully populated UserDetails instance
     * @throws UsernameNotFoundException if the student cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Student student = studentRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Student not found with email: " + email));

            return new User(
                    student.getEmail(),
                    student.getPassword(),
                    java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (UsernameNotFoundException ex) {
            System.err.println("CustomUserDetailsService: Student not found for email " + email);
            logger.warn("Student authentication lookup failed for email: {}", email);
            throw ex;
        } catch (Exception ex) {
            System.err.println("CustomUserDetailsService: Error loading user by email: " + ex.getMessage());
            logger.error("Unexpected error in loadUserByUsername for email: {}", email, ex);
            throw new UsernameNotFoundException("Error loading user credentials", ex);
        }
    }
}
