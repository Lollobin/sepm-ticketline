delete from TICKET where TICKET_ID < 0;
commit;

insert into TICKET values ( -1, null, null, -1, -1),
                        ( -2, null, null, -2, -2);

select * from SEAT where SEAT_ID < 0;

