package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

@Mapper
public class ShowHelpMapper {

    public long idOfEvent(Event event){
        return event.getEventId();
    }

}
