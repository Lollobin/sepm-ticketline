delete
from SECTOR_PRICE
where SECTOR_ID < 0
  and SHOW_ID < 0;
Delete
from SHOW
where SHOW_ID < 0;

commit;


insert into SHOW
values (-1, parsedatetime('2022-08-15 14:30', 'yyyy-MM-dd HH:mm'), -1),
       (-2, parsedatetime('2022-05-25 18:45', 'yyyy-MM-dd HH:mm'), -3),
       (-3, parsedatetime('2022-08-15 19:30', 'yyyy-MM-dd HH:mm'), -2),
       (-4, parsedatetime('2021-11-23 13:00', 'yyyy-MM-dd HH:mm'), -2),
       (-5, parsedatetime('2024-07-04 19:00', 'yyyy-MM-dd HH:mm'), -2),
       (-6, parsedatetime('2022-05-25 15:00', 'yyyy-MM-dd HH:mm'), -4),
       (-7, parsedatetime('2022-05-25 08:15', 'yyyy-MM-dd HH:mm'), -4);

select *
from SHOW
where SHOW_ID < 0;


