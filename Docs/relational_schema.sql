-- Relational schema for university_academic_db
-- Hibernate generates these tables from the entities. This file documents the target design.

CREATE TABLE departments (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(100) NOT NULL UNIQUE,
    code             VARCHAR(20)  NOT NULL UNIQUE,
    head_lecturer_id BIGINT UNIQUE
);

CREATE TABLE lecturers (
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    department_id BIGINT NOT NULL REFERENCES departments (id)
);

ALTER TABLE departments
    ADD CONSTRAINT fk_departments_head_lecturer
    FOREIGN KEY (head_lecturer_id) REFERENCES lecturers (id);

CREATE TABLE courses (
    id            BIGSERIAL PRIMARY KEY,
    code          VARCHAR(20)  NOT NULL UNIQUE,
    title         VARCHAR(150) NOT NULL,
    credits       INTEGER      NOT NULL,
    department_id BIGINT NOT NULL REFERENCES departments (id)
);

CREATE TABLE lecturer_courses (
    lecturer_id BIGINT NOT NULL REFERENCES lecturers (id),
    course_id   BIGINT NOT NULL REFERENCES courses (id),
    PRIMARY KEY (lecturer_id, course_id)
);

CREATE TABLE students (
    id             BIGSERIAL PRIMARY KEY,
    student_number VARCHAR(30)  NOT NULL UNIQUE,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    department_id  BIGINT NOT NULL REFERENCES departments (id)
);

CREATE TABLE student_profiles (
    id            BIGSERIAL PRIMARY KEY,
    date_of_birth DATE         NOT NULL,
    gender        VARCHAR(20)  NOT NULL,
    phone_number  VARCHAR(30)  NOT NULL,
    address       VARCHAR(255) NOT NULL,
    student_id    BIGINT NOT NULL UNIQUE REFERENCES students (id)
);

CREATE TABLE registrations (
    id                BIGSERIAL PRIMARY KEY,
    student_id        BIGINT NOT NULL REFERENCES students (id),
    course_id         BIGINT NOT NULL REFERENCES courses (id),
    registration_date DATE        NOT NULL,
    academic_year     VARCHAR(9)  NOT NULL,
    semester          VARCHAR(20) NOT NULL,
    status            VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    grade             DOUBLE PRECISION,
    CONSTRAINT uk_registration_student_course_term
        UNIQUE (student_id, course_id, academic_year, semester)
);
