---
CREATE TABLE event_manager.Events (
   id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(32),
   date DATE ,
   location VARCHAR(64),
   description VARCHAR(128),
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT
);

CREATE TABLE event_manager.Users (
     id  BIGSERIAL PRIMARY KEY,
    username VARCHAR(32),
    email VARCHAR(16),
    password VARCHAR(255),
    first_name VARCHAR(32),
    last_name VARCHAR(32),
    profile_picture_url VARCHAR(255),
    date_of_birth DATE,
    address VARCHAR(64),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    version BIGINT
);

CREATE TABLE event_manager.Tasks (
    id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(32),
   description VARCHAR(128),
   due_date DATE,
   status VARCHAR(16),
   creator_username VARCHAR(32),
   event_id BIGINT REFERENCES Events(id),
   created_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT
);

---
CREATE TABLE event_manager.Event_User (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(16),
    category VARCHAR(16),
    user_id BIGINT REFERENCES Users(id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES Events(id) ON DELETE CASCADE
);

CREATE TABLE event_manager.event_users_tasks (
    event_user_id BIGINT REFERENCES Event_User(id),
    task_id BIGINT REFERENCES Tasks(id),
    PRIMARY KEY (event_user_id, task_id)
);

