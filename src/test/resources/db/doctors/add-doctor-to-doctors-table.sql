insert into doctor_schedules (id, appointments_duration_in_mins, end_time, is_deleted, start_time)
values (1, 20, '18:00:00', false, '08:00:00');
insert into doctors (id, first_name, last_name, phone, rating_sum, schedule_id, average_rating, experience, is_deleted)
values (1, 'John', 'Doe', '0123456789', 0, 1, 0, 5, false);