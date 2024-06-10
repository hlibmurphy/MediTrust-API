INSERT INTO specialties (id, name, is_deleted)
VALUES (1, 'Dentist', false);
INSERT INTO doctors_specialties (doctor_id, specialties_id)
VALUES (1, 1)