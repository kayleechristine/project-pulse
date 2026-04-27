CREATE TABLE peer_evaluations (
    id               INT          NOT NULL AUTO_INCREMENT,
    evaluator_id     INT          NOT NULL,
    evaluatee_id     INT          NOT NULL,
    week_id          INT          NOT NULL,
    public_comments  TEXT,
    private_comments TEXT,
    submitted_at     DATETIME(6),
    CONSTRAINT pk_peer_evaluations PRIMARY KEY (id),
    CONSTRAINT uq_peer_evaluations UNIQUE (evaluator_id, evaluatee_id, week_id),
    CONSTRAINT fk_peer_eval_evaluator FOREIGN KEY (evaluator_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_peer_eval_evaluatee FOREIGN KEY (evaluatee_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_peer_eval_week     FOREIGN KEY (week_id)       REFERENCES active_weeks (id) ON DELETE CASCADE
);

CREATE TABLE peer_evaluation_items (
    id            INT NOT NULL AUTO_INCREMENT,
    evaluation_id INT NOT NULL,
    criterion_id  INT NOT NULL,
    score         INT NOT NULL,
    CONSTRAINT pk_peer_evaluation_items PRIMARY KEY (id),
    CONSTRAINT uq_peer_evaluation_items UNIQUE (evaluation_id, criterion_id),
    CONSTRAINT fk_peer_eval_item_eval      FOREIGN KEY (evaluation_id) REFERENCES peer_evaluations (id) ON DELETE CASCADE,
    CONSTRAINT fk_peer_eval_item_criterion FOREIGN KEY (criterion_id)  REFERENCES rubric_criteria (id)  ON DELETE CASCADE
);
