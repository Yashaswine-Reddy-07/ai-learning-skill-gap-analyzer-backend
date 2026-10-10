package com.example.skillgap;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

/**
 * Main entry point for the AI-Powered Personalized Learning and Skill Gap Analysis System.
 */
@SpringBootApplication
public class SkillGapAnalyzerApplication {

    private static final Logger logger = LoggerFactory.getLogger(SkillGapAnalyzerApplication.class);

    /**
     * Initializes environment variables from .env if present and launches the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            loadDotenvIfPresent();
            SpringApplication.run(SkillGapAnalyzerApplication.class, args);
            logger.info("Skill Gap Analyzer Backend Application started successfully.");
        } catch (Exception ex) {
            System.err.println("SkillGapAnalyzerApplication: Critical startup failure: " + ex.getMessage());
            logger.error("Application failed to start", ex);
        }
    }

    /**
     * Safely checks for a local .env file in the current directory or parent directory and
     * injects variables into System properties if not already set.
     */
    private static void loadDotenvIfPresent() {
        try {
            File envFile = new File(".env");
            if (!envFile.exists()) {
                envFile = new File("backend/.env");
            }

            if (envFile.exists()) {
                Dotenv dotenv = Dotenv.configure()
                        .directory(envFile.getParent() != null ? envFile.getParent() : ".")
                        .filename(envFile.getName())
                        .ignoreIfMissing()
                        .load();

                dotenv.entries().forEach(entry -> {
                    if (System.getProperty(entry.getKey()) == null && System.getenv(entry.getKey()) == null) {
                        System.setProperty(entry.getKey(), entry.getValue());
                    }
                });
                logger.info("Loaded environment variables from {}", envFile.getAbsolutePath());
            }
        } catch (Exception ex) {
            System.err.println("SkillGapAnalyzerApplication: Non-fatal note while reading .env: " + ex.getMessage());
        }
    }
}
