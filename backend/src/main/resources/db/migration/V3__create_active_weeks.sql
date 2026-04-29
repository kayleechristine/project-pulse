CREATE TABLE active_weeks (
    id         INT        NOT NULL AUTO_INCREMENT,
    section_id BIGINT,
    start_date DATE,
    end_date   DATE,
    active     TINYINT(1) NOT NULL DEFAULT 0,
    CONSTRAINT pk_active_weeks PRIMARY KEY (id)
);
