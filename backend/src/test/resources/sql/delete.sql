
delete from PLAYS_IN where SHOW_ID < 0 and ARTIST_ID < 0;

delete from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;
delete from TICKET where TICKET_ID < 0;
delete from SHOW where SHOW_ID < 0;

delete from EVENT where EVENT_ID < 0;

delete from ARTIST where ARTIST_ID < 0;


delete from READ_ARTICLE where USER_ID < 0 and ARTICLE_ID < 0;

delete from ARTICLE where ARTICLE_ID < 0;

delete from IMAGE where IMAGE_ID < 0;

delete from BOOKED_IN where TRANSACTION_ID < 0 and TICKET_ID < 0;

delete from TRANSACTION where TRANSACTION_ID < 0;

delete from APPLICATION_USER where USER_ID < 0;

delete from SEAT where SEAT_ID < 0;
delete from SECTOR where SECTOR_ID < 0;






commit;

delete from SEATING_PLAN where SEATING_PLAN_ID < 0;

delete from SEATING_PLAN_LAYOUT where SEATING_PLAN_LAYOUT_ID < 0;

delete from LOCATION where LOCATION_ID < 0 ;
 delete from ADDRESS where ADDRESS_ID < 0;

commit;
