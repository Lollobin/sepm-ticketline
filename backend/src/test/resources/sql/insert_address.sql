
delete from ADDRESS where ADDRESS_ID < 0;
commit;

insert into ADDRESS values ( -1, 'Wien', 'Austria', '6', 'Guglgasse', '1110'),
                           ( -2, 'Wien', 'Austria', '1', 'Albertinaplatz', '1010'),
                           ( -3, 'Klagenfurt', 'Austria', '46', 'Gerberweg', '9020'),
                           ( -4, 'Graz', 'Austria', '1', 'Messeplatz', '8010'),
                           ( -5, 'Boom', 'Belgien', '1', 'Schommelei', '2850');

select * from ADDRESS where ADDRESS_ID < 0;

