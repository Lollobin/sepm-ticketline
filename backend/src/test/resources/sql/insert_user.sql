delete from APPLICATION_USER where USER_ID < 0;
commit ;

insert into APPLICATION_USER values ( -1, false, 'admin@email.com', 'Tom', 'MALE', true, 'Brady', false, 0, false, '12345678', '123', -1 ),
                                    ( -2, false, 'test2@email.com', 'Paula', 'FEMALE', true, 'Jenkins', false, 0, false, '12345678', '123', -2 ),
                                    ( -3, false, 'test3@email.com', 'Kemo', 'OTHER', false, 'Schulze', false, 0, false, '12345678', '123', -3 ),
                                    ( -4, false, 'test4@email.com', 'Gringo', 'MALE', false, 'Bingo', false, 0, false, '12345678', '123', -4 ),
                                    ( -5, false, 'test5@email.com', 'Bambi', 'FEMALE', false, 'Lambi', false, 0, false, '12345678', '123', -5 )