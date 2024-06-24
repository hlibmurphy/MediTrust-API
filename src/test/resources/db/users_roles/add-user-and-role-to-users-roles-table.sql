INSERT INTO users_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE email = 'test_email@email.com'), 1);