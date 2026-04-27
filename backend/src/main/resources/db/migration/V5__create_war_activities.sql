CREATE TABLE war_activities (
    id            INT          NOT NULL AUTO_INCREMENT,
    student_id    INT          NOT NULL,
    week_id       INT          NOT NULL,
    category      VARCHAR(50)  NOT NULL,
    description   TEXT         NOT NULL,
    planned_hours DOUBLE       NOT NULL,
    actual_hours  DOUBLE,
    status        VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_war_activities PRIMARY KEY (id),
    CONSTRAINT fk_war_activity_student FOREIGN KEY (student_id) REFERENCES users (id) ON DELETE CASCADE
);
