CREATE TABLE users (
    id                      INT             NOT NULL AUTO_INCREMENT,
    email                   VARCHAR(255)    NOT NULL,
    username                VARCHAR(255),
    first_name              VARCHAR(255),
    last_name               VARCHAR(255),
    password                VARCHAR(255),
    enabled                 BOOLEAN         NOT NULL DEFAULT FALSE,
    account_status          VARCHAR(50)     NOT NULL DEFAULT 'INVITED',
    invitation_token        VARCHAR(255),
    invitation_token_expiry DATETIME(6),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_invitation_token UNIQUE (invitation_token)
);

CREATE TABLE user_roles (
    user_id INT         NOT NULL,
    role    VARCHAR(50) NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE invitation_tokens (
    id         BIGINT          NOT NULL AUTO_INCREMENT,
    token      VARCHAR(255)    NOT NULL,
    email      VARCHAR(255)    NOT NULL,
    role       VARCHAR(50)     NOT NULL,
    expires_at DATETIME(6)     NOT NULL,
    used       BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at DATETIME(6)     NOT NULL,
    CONSTRAINT pk_invitation_tokens PRIMARY KEY (id),
    CONSTRAINT uk_invitation_tokens_token UNIQUE (token)
);
