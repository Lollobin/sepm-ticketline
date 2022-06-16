delete from TICKET where TICKET_ID < 0 and SEAT_ID < 0;
delete from SEAT where SEAT_ID < 0;
commit;

insert into SEAT values ( -1, null, null, -1),
                        ( -2, null, null, -2);

select * from SEAT where SEAT_ID < 0;

