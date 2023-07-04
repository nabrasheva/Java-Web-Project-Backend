insert into event_manager.events (name, date, location, description, created_date, last_modified_date) values ('Wedding', '2023-06-05', 'Sofia', 'Wedding preparation', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events (name, date, location, description, created_date, last_modified_date) values ('Birthday party', '2023-05-31', 'Pirdop', null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events (name, date, location, description, created_date, last_modified_date) values ('Graduation', '2024-06-12', 'Sofia', 'Finally', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');

insert into event_manager.users (username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date, is_enabled) values ('Niya123', 'niya@test.com', '123', 'Niya', null, null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', TRUE);
insert into event_manager.users (username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date, is_enabled) values ('Georgi', 'georgi@test.com', '123', 'Georgi', 'Cekov', null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', TRUE);
insert into event_manager.users (username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date, is_enabled) values ('Tsvetina', 'tsvetina@test.com', '123', null, 'Rasheva', null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', TRUE);

insert into event_manager.tasks (name, description, due_date, status, creator_email, created_date, last_modified_date, event_id) values ('Order flowers', null, '2023-05-31', 'TO_DO', 'niya@test.com', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 1);
insert into event_manager.tasks (name, description, due_date, status, creator_email, created_date, last_modified_date, event_id) values ('Invite guests', null, '2023-06-23', 'TO_DO', 'niya@test.com', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 1);
insert into event_manager.tasks (name, description, due_date, status, creator_email, created_date, last_modified_date, event_id) values ('Order party cake', null, '2023-06-14', 'TO_DO', 'georgi@test.com', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 2);
insert into event_manager.tasks (name, description, due_date, status, creator_email, created_date, last_modified_date, event_id) values ('Buy dress', null, '2023-06-22', 'TO_DO', 'tsvetina@test.com', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 3);

insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('PLANNER', null, 1, 1, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('PLANNER', null, 2, 2, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('PLANNER', null, 3, 3, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('ADMIN', null, 3, 1, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('ADMIN', null, 2, 3, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (role, category, user_id, event_id, created_date, last_modified_date) values ('ADMIN', null, 1, 2, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');

--insert into event_manager.users_tasks (user_id, task_id) values (1, 1);
--insert into event_manager.users_tasks (user_id, task_id) values (1, 2);
--insert into event_manager.users_tasks (user_id, task_id) values (2, 3);
