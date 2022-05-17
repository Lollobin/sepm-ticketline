package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

@Mapper
public interface EventMapper {

    Event eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(Event event);

    Event eventWithoutIdDtoToEvent(EventWithoutIdDto eventWithoutIdDto);

    EventWithoutIdDto eventToEventWithoutIdDto(Event event);
}
