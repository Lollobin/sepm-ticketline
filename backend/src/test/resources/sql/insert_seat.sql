delete
from SEAT
where SEAT_ID < 0;
commit;

insert into SEAT
values (-1, 1, 1, -1),
       (-2, 1, 2, -1),
       (-3, 1, 3, -1),
       (-4, 1, 4, -1),
       (-5, 1, 5, -1)