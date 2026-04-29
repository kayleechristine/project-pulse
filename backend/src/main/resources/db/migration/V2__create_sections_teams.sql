CREATE TABLE sections (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    section_name VARCHAR(255) NOT NULL,
    start_date   DATE         NOT NULL,
    end_date     DATE         NOT NULL,
    rubric_id    BIGINT,
    CONSTRAINT pk_sections PRIMARY KEY (id),
    CONSTRAINT uq_sections_name UNIQUE (section_name)
);

CREATE TABLE teams (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    section_id  BIGINT,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    website_url VARCHAR(255),
    CONSTRAINT pk_teams PRIMARY KEY (id),
    CONSTRAINT uq_teams_name UNIQUE (name)
);

CREATE TABLE team_students (
    team_id    BIGINT NOT NULL,
    student_id INT    NOT NULL,
    CONSTRAINT pk_team_students PRIMARY KEY (team_id, student_id),
    CONSTRAINT fk_team_students_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE
);

CREATE TABLE team_member (
    id         INT NOT NULL AUTO_INCREMENT,
    team_id    INT,
    student_id INT,
    CONSTRAINT pk_team_member PRIMARY KEY (id)
);