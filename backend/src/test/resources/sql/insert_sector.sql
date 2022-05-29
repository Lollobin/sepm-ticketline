delete  from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;

delete from SECTOR where SECTOR_ID < 0;
commit;

insert into SECTOR values ( -1, -1 ),
                          (-2, -2);

select * from SECTOR where SECTOR_ID < 0;