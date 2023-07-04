CREATE TABLE event_manager.events
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    name               VARCHAR(32) NOT NULL,
    date               DATE        NOT NULL,
    location           VARCHAR(64),
    description        VARCHAR(128),
    created_date       TIMESTAMP   NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date TIMESTAMP   NOT NULL DEFAULT CURRENT_DATE
);


CREATE TABLE event_manager.users
(
    id                  BIGSERIAL PRIMARY KEY NOT NULL,
    username            VARCHAR(32)           NOT NULL,
    email               VARCHAR(64)           NOT NULL,
    password            VARCHAR(255)          NOT NULL,
    first_name          VARCHAR(32),
    last_name           VARCHAR(32),
    profile_picture_url VARCHAR(255),
    date_of_birth       DATE,
    address             VARCHAR(64),
    created_date        TIMESTAMP             NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date  TIMESTAMP             NOT NULL DEFAULT CURRENT_DATE,
    is_enabled          BOOLEAN               NOT NULL
);


CREATE TABLE event_manager.tasks
(
    id                 BIGSERIAL PRIMARY KEY NOT NULL,
    name               VARCHAR(32) NOT NULL,
    description        VARCHAR(128),
    due_date           DATE NOT NULL,
    status             VARCHAR(16) NOT NULL,
    creator_email   VARCHAR(64) NOT NULL,
    created_date       TIMESTAMP   NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date TIMESTAMP   NOT NULL DEFAULT CURRENT_DATE,
    event_id           BIGINT      NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event_manager.events (id) ON DELETE CASCADE
);

CREATE TABLE event_manager.events_users
(
    id       BIGSERIAL PRIMARY KEY NOT NULL,
    role     VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    user_id  BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    created_date        TIMESTAMP             NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date  TIMESTAMP             NOT NULL DEFAULT CURRENT_DATE,
    FOREIGN KEY (user_id) REFERENCES event_manager.users (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES event_manager.events (id) ON DELETE CASCADE
);
/*
CREATE TABLE event_manager.users_tasks
(
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (user_id) REFERENCES event_manager.users (id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES event_manager.tasks (id) ON DELETE CASCADE

);*/

ALTER TABLE event_manager.events_users
    ADD CONSTRAINT constraint_name UNIQUE (user_id, event_id, role);

ALTER TABLE event_manager.users
    ADD CONSTRAINT constraint_name1 UNIQUE (email);


ALTER TABLE event_manager.events
    ADD CONSTRAINT constraint_name2 UNIQUE (name);

ALTER TABLE event_manager.tasks
    ADD CONSTRAINT constraint_name3 UNIQUE (name);