delete from READ_ARTICLE where USER_ID < 0 and ARTICLE_ID < 0;
commit ;

insert into READ_ARTICLE values ( -1,  -1),
                                ( -1,  -2),
                                ( -1,  -3),
                                ( -1,  -4),
                                (-2, -3),
                                ( -3,  -1),
                                (-3, -3)
