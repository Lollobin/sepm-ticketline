package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithTicketsSoldDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.result.EventWithTickets;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {

    Event eventDtoToEvent(EventDto eventDto);

    EventDto eventToEventDto(Event event);

    Event eventWithoutIdDtoToEvent(EventWithoutIdDto eventWithoutIdDto);

    EventWithoutIdDto eventToEventWithoutIdDto(Event event);

    EventWithTicketsSoldDto eventToEventWithTicketsSoldDto(EventWithTickets event);

    List<EventWithTicketsSoldDto> eventToEventWithTicketsSoldDto(List<EventWithTickets> events);
}
