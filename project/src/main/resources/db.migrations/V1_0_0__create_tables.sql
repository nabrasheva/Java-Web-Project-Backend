---
CREATE TABLE event_manager.events (
   id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(32),
   date DATE ,
   location VARCHAR(64),
   description VARCHAR(128),
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT
);

CREATE TABLE event_manager.users (
     id  BIGSERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(32) NOT NULL,
    email VARCHAR(16) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(32),
    last_name VARCHAR(32),
    profile_picture_url VARCHAR(255),
    date_of_birth DATE,
    address VARCHAR(64),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    version BIGINT NOT NULL
);

CREATE TABLE event_manager.tasks (
    id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(32) NOT NULL,
   description VARCHAR(128),
   due_date DATE,
   status VARCHAR(16),
   creator_username VARCHAR(32) NOT NULL,
   event_id BIGINT NOT NULL REFERENCES events(id) ,
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT NOT NULL
);

---
CREATE TABLE event_manager.events_users (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(16),
    category VARCHAR(16),
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE
);

CREATE TABLE event_manager.events_users_tasks (
    event_user_id BIGINT REFERENCES events_users(id),
    task_id BIGINT REFERENCES tasks(id),
    PRIMARY KEY (event_user_id, task_id)
);

