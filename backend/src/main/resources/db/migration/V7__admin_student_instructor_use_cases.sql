ALTER TABLE invitation_tokens
    ADD COLUMN section_id BIGINT;

CREATE TABLE team_instructors (
    team_id       BIGINT NOT NULL,
    instructor_id INT    NOT NULL,
    CONSTRAINT pk_team_instructors PRIMARY KEY (team_id, instructor_id),
    CONSTRAINT fk_team_instructors_team FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE
);
