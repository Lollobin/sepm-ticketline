delete from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;
commit;

insert into SECTOR_PRICE values ( -1, -1, 154.32 ),
                                ( -1, -2, 53 );

select * from SECTOR_PRICE where SECTOR_ID < 0;