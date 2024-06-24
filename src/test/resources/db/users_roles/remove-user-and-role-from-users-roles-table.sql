DELETE FROM users_roles WHERE user_id =
                              (SELECT id FROM users WHERE email = 'test_email@email.com');