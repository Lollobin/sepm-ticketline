delete from EVENT where EVENT_ID < 0;
commit;

Delete
from EVENT
where EVENT_ID < 0;
commit;

insert into EVENT
values (-1, 'Pop', 'Das ist ein gutes event', 4, 'Popevent'),
       (-2, 'Jazz', 'Jazz ist der Punkt', 4, 'Jazzevent'),
       (-3, 'Romantic', 'Romantic ist the way', 4, 'Romanticevent'),
       (-4, 'Schlager', 'Schlager beste', 4, 'Schlagerevent')
