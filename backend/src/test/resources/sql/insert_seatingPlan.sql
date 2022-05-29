delete from SEATING_PLAN where SEATING_PLAN_ID < 0;
commit;

insert into SEATING_PLAN values ( -1, 'plan1', -1, -1 ),
                                ( -2, 'plan2', -2, -1 ),
                                ( -3, 'plan3', -3, -1 )