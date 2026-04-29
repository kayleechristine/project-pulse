CREATE TABLE rubrics (
    id   BIGINT       NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_rubrics PRIMARY KEY (id),
    CONSTRAINT uq_rubrics_name UNIQUE (name)
);

CREATE TABLE rubric_criteria (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255),
    description VARCHAR(255),
    max_score   DOUBLE,
    rubric_id   BIGINT,
    CONSTRAINT pk_rubric_criteria PRIMARY KEY (id),
    CONSTRAINT fk_rubric_criteria_rubric FOREIGN KEY (rubric_id) REFERENCES rubrics (id) ON DELETE CASCADE
);
