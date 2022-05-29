delete from SEATING_PLAN_LAYOUT where SEATING_PLAN_LAYOUT_ID < 0;
commit;

insert into SEATING_PLAN_LAYOUT values ( -1, 'src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan1.json' )