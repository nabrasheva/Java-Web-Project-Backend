insert into event_manager.events (id, name, date, location, description, created_date, last_modified_date) values (1, 'Wedding', '2023-06-05', 'Sofia', 'Wedding preparation', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events (id, name, date, location, description, created_date, last_modified_date) values (2, 'Birthday party', '2023-05-31', 'Pirdop', null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events (id, name, date, location, description, created_date, last_modified_date) values (3, 'Graduation', '2024-06-12', 'Sofia', 'Finally', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');

insert into event_manager.users (id, username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date) values (1, 'Niya123', 'niya@test.com', '123', 'Niya', null, null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.users (id, username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date) values (2, 'Georgi', 'georgi@test.com', '123', 'Georgi', 'Cekov', null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.users (id, username, email, password, first_name, last_name, profile_picture_url, date_of_birth, address, created_date, last_modified_date) values (3, 'Tsvetina', 'tsvetina@test.com', '123', null, 'Rasheva', null, null, null, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');

insert into event_manager.tasks (id, name, description, due_date, status, creator_username, created_date, last_modified_date, event_id) values (1, 'Order flowers', null, '2023-05-31', 'TO_DO', 'Niya', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 1);
insert into event_manager.tasks (id, name, description, due_date, status, creator_username, created_date, last_modified_date, event_id) values (2, 'Invite guests', null, '2023-06-23', 'TO_DO', 'Niya', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 1);
insert into event_manager.tasks (id, name, description, due_date, status, creator_username, created_date, last_modified_date, event_id) values (3, 'Order party cake', null, '2023-06-14', 'TO_DO', 'Georgi', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 2);
insert into event_manager.tasks (id, name, description, due_date, status, creator_username, created_date, last_modified_date, event_id) values (4, 'Buy dress', null, '2023-06-22', 'TO_DO', 'Tsvetina', '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000', 3);

insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (1, 'PLANNER', null, 1, 1, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (4, 'PLANNER', null, 2, 2, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (5, 'PLANNER', null, 3, 3, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (6, 'ADMIN', null, 3, 1, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (7, 'ADMIN', null, 2, 3, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');
insert into event_manager.events_users (id, role, category, user_id, event_id, created_date, last_modified_date) values (8, 'ADMIN', null, 1, 2, '2023-05-29 00:00:00.000000', '2023-05-29 00:00:00.000000');

insert into event_manager.users_tasks (task_id, user_id) values (1, 1);
insert into event_manager.users_tasks (task_id, user_id) values (2, 1);
insert into event_manager.users_tasks (task_id, user_id) values (3, 2);
insert into event_manager.users_tasks (task_id, user_id) values (4, 3);
