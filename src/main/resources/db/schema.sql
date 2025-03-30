-- PostgreSQL database schema for the training management system

-- DROP TABLE IF EXISTS participations;
-- DROP TABLE IF EXISTS training_sessions;
-- DROP TABLE IF EXISTS domains;
-- DROP TABLE IF EXISTS trainers;
-- DROP TABLE IF EXISTS employers;
-- DROP TABLE IF EXISTS participants;
-- DROP TABLE IF EXISTS structures;
-- DROP TABLE IF EXISTS profiles;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS roles;


-- Table for roles
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- Table for users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL, -- UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- Table for profiles
CREATE TABLE profiles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL -- UNIQUE
);

-- Table for structures (departments or offices)
CREATE TABLE structures (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL -- UNIQUE
);

-- Table for participants
CREATE TABLE participants (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number INT,
    structure_id INT NOT NULL,
    profile_id INT NOT NULL,
    FOREIGN KEY (structure_id) REFERENCES structures(id),
    FOREIGN KEY (profile_id) REFERENCES profiles(id)
);

-- Table for employers (for trainers)
CREATE TABLE employers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL -- UNIQUE
);

-- Table for trainers (formateurs)
CREATE TABLE trainers (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number INT,
    type VARCHAR(10) NOT NULL CHECK (type IN ('internal', 'external')),
    employer_id INT,
    FOREIGN KEY (employer_id) REFERENCES employers(id)
);

-- Table for training domains
CREATE TABLE domains (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table for training sessions
CREATE TABLE training_sessions (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    duration_days INT NOT NULL,
    budget DOUBLE PRECISION NOT NULL,
    domain_id INT NOT NULL,
    FOREIGN KEY (domain_id) REFERENCES domains(id)
);

-- Table to link participants to training sessions
CREATE TABLE participations (
    training_id INT NOT NULL,
    participant_id INT NOT NULL,
    PRIMARY KEY (training_id, participant_id),
    FOREIGN KEY (training_id) REFERENCES training_sessions(id),
    FOREIGN KEY (participant_id) REFERENCES participants(id)
);

CREATE TABLE trains (
    training_session_id BIGINT NOT NULL,
    trainer_id INT NOT NULL,
    PRIMARY KEY (training_id, trainer_id),
    FOREIGN KEY (training_id) REFERENCES training_sessions(id),
    FOREIGN KEY (trainer_id) REFERENCES trainers(id)
)
