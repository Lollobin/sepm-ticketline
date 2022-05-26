delete  from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;
Delete from SHOW
where SHOW_ID < 0;

commit;


insert into SHOW
values (-1, parsedatetime('2022-08-15 14:30', 'yyyy-MM-dd HH:mm'), -1),
       (-2, parsedatetime('2022-05-25 18:45', 'yyyy-MM-dd HH:mm'), -3);

select *
from SHOW
where SHOW_ID < 0;


