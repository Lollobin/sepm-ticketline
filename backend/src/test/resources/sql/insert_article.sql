delete
from READ_ARTICLE
where USER_ID < 0
  and ARTICLE_ID < 0;
commit;
delete
from ARTICLE
where ARTICLE_ID < 0;
commit;

insert into ARTICLE
values (-1, parsedatetime('2022-05-20 14:30', 'yyyy-MM-dd HH:mm'), 'summary1', 'text1', 'title1'),
       (-2, parsedatetime('2020-04-15 14:30', 'yyyy-MM-dd HH:mm'), 'summary2', 'text2', 'title2'),
       (-3, parsedatetime('2021-11-14 14:30', 'yyyy-MM-dd HH:mm'), 'summary3', 'text3', 'title3'),
       (-4, parsedatetime('2019-02-15 14:30', 'yyyy-MM-dd HH:mm'), 'summary4', 'text4', 'title4')