-- Schema DDL for AI-Powered Personalized Learning & Skill Gap Analyzer

CREATE TABLE IF NOT EXISTS students (
    student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    career_goal VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS skills (
    skill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS role_requirements (
    requirement_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    required_level INT NOT NULL,
    CONSTRAINT fk_role_req_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_req_skill FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    skill_id BIGINT NOT NULL,
    question_text TEXT NOT NULL,
    option_a VARCHAR(500) NOT NULL,
    option_b VARCHAR(500) NOT NULL,
    option_c VARCHAR(500) NOT NULL,
    option_d VARCHAR(500) NOT NULL,
    correct_option VARCHAR(5) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    CONSTRAINT fk_question_skill FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_attempts (
    attempt_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    attempt_date DATETIME NOT NULL,
    total_score DOUBLE NOT NULL,
    CONSTRAINT fk_attempt_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_attempt_role FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS quiz_results (
    result_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    correct_count INT NOT NULL,
    total_questions INT NOT NULL,
    level INT NOT NULL,
    CONSTRAINT fk_result_attempt FOREIGN KEY (attempt_id) REFERENCES quiz_attempts (attempt_id) ON DELETE CASCADE,
    CONSTRAINT fk_result_skill FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_skills (
    student_skill_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency INT NOT NULL,
    CONSTRAINT fk_student_skill_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE,
    CONSTRAINT fk_student_skill_skill FOREIGN KEY (skill_id) REFERENCES skills (skill_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS learning_plans (
    plan_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    skill VARCHAR(100) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    activity TEXT NOT NULL,
    priority INT NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'NOT_STARTED',
    created_date DATETIME,
    CONSTRAINT fk_plan_student FOREIGN KEY (student_id) REFERENCES students (student_id) ON DELETE CASCADE
);
