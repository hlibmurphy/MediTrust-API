insert into doctor_schedules (id, appointments_duration_in_mins, end_time, is_deleted, start_time)
values (1, 60, '09:00:00', false, '08:00:00');
insert into doctors (id, first_name, last_name, phone, rating_sum, schedule_id, average_rating, experience, is_deleted)
values (1, 'John', 'Doe', '0123456789', 0, 1, 0, 5, false);
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'MONDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'TUESDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'WEDNESDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'THURSDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'FRIDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'SATURDAY');
insert into doctor_schedule_working_days (doctor_schedule_id, working_days)
values (1, 'SUNDAY');