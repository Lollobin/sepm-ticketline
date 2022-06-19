delete
from TICKET
where TICKET_ID < 0;
commit;

insert into TICKET
values (-1, -1, null, -1, -1),
       (-2, -1, null, -2, -1),
       (-3, -1, null, -3, -3),
       (-4, -1, null, -4, -3),
       (-5, -1, null, -5, -3)
