delete from LOCATION where LOCATION_ID < 0;
commit;


insert into LOCATION values ( -1, 'Gasometer', -1 ),
                            ( -2, 'Bollwerk', -3 ),
                            ( -3, 'Albertina', -2 ),
                            ( -4, 'Stadthalle', -4 ),
                            ( -5, 'Tomorrowland', -5 );

select * from LOCATION where LOCATION_ID < 0;