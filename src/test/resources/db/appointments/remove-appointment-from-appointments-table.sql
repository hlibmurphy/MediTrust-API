delete from appointments where id = (SELECT id FROM appointments
                                               WHERE date = CURRENT_DATE
                                                 AND start_time = '09:00:00');
