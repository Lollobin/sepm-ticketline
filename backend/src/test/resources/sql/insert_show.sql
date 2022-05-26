delete  from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;
Delete from SHOW
where SHOW_ID < 0;

commit;


insert into SHOW
values (-1, current_timestamp(0) - interval '3' DAY, -1),
       (-2, current_timestamp(0) - interval '6' MONTH - interval '30' MINUTE, -3);

select *
from SHOW
where SHOW_ID < 0;


