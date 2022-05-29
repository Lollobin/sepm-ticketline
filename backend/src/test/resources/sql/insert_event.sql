delete from EVENT where EVENT_ID < 0;
commit;

Delete
from EVENT
where EVENT_ID < 0;
commit;

insert into EVENT
values (-1, 'POP', 'Das ist ein gutes event', 4, 'Popevent'),
       (-2, 'JAZZ', 'Jazz ist der Punkt', 4, 'Jazzevent'),
       (-3, 'POP', 'Romantic ist the way', 4, 'Zweitespopevent'),
       (-4, 'RAP', 'Schlager beste', 4, 'Rapevent')
