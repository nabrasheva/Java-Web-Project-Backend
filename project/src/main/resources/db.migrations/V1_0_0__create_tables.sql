---
CREATE TABLE event_manager.Events (
   id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(255),
   date DATE ,
   location VARCHAR(255),
   description VARCHAR(255),
   --created_date TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   --last_modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT
);

CREATE TABLE event_manager.Users (
     id  BIGSERIAL PRIMARY KEY,
    username VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_picture_url VARCHAR(255),
    --date_of_birth DATE,
    address VARCHAR(255),
    --created_date TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    --last_modified_date TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
    version BIGINT
);

CREATE TABLE event_manager.Tasks (
    id  BIGSERIAL PRIMARY KEY,
   name VARCHAR(255),
   description VARCHAR(255),
   due_date DATE,
   status VARCHAR(255),
   creator_username VARCHAR(255),
   event_id BIGINT REFERENCES Events(id),
   --created_date TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   --last_modified_date TIMESTAMP TIMESTAMP NOT NULL DEFAULT CURRENT_DATE,
   version BIGINT
);

---
CREATE TABLE event_manager.Event_User (
    id BIGSERIAL PRIMARY KEY,
    role VARCHAR(255),
    category VARCHAR(255),
    user_id BIGINT REFERENCES Users(id) ON DELETE CASCADE,
    event_id BIGINT REFERENCES Events(id) ON DELETE CASCADE
);

CREATE TABLE event_manager.event_users_tasks (
    event_user_id BIGINT REFERENCES Event_User(id),
    task_id BIGINT REFERENCES Tasks(id),
    PRIMARY KEY (event_user_id, task_id)
);

