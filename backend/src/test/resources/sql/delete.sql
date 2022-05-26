delete from show where SHOW_ID < 0;

delete from sector where SECTOR_ID < 0;

delete from SECTOR_PRICE where SECTOR_ID < 0 and SHOW_ID < 0;

delete from EVENT where EVENT_ID < 0;

delete from SEATING_PLAN where SEATING_PLAN_ID < 0;

delete from SEATING_PLAN_LAYOUT where SEATING_PLAN_LAYOUT_ID < 0;

delete from LOCATION where LOCATION_ID < 0;

delete from ADDRESS where ADDRESS_ID < 0;