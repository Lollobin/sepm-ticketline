delete from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;
commit;

insert into SECTOR_PRICE values ( -1, -1, 154.32 ),
                                ( -1, -2, 53 ),
                                ( -1, -7, 70 ),
                                ( -1, -6, 99 ),
                                ( -1, -3, 180 );

select * from SECTOR_PRICE where SECTOR_ID < 0;