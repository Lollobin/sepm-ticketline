package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SortDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.EventsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class EventsEndpoint implements EventsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final EventMapper eventMapper;
    private final EventService eventService;

    public EventsEndpoint(EventMapper eventMapper, EventService eventService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<EventSearchResultDto> eventsGet(EventSearchDto search,
        Integer pageSize, Integer requestedPage, SortDto sort) {

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Direction.fromString(sort.getValue()), "name");

        if ((search.getName() == null || search.getName().isBlank())
            && (search.getCategory() == null || search.getCategory().isBlank())
            && search.getDuration() == null) {

            EventSearchResultDto eventSearchResultDto = this.eventService.findAll(pageable);
            return ResponseEntity.ok().body(eventSearchResultDto);
        } else {
            EventSearchResultDto eventSearchResultDto = this.eventService.search(search, pageable);
            return ResponseEntity.ok().body(eventSearchResultDto);
        }
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> eventsPost(EventWithoutIdDto eventWithoutIdDto) {
        LOGGER.info("POST /events body: {}", eventWithoutIdDto);

        EventDto eventDto = eventMapper.eventToEventDto(
            eventService.createEvent(
                eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdDto)
            ));

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(eventDto.getEventId())
            .toUri();

        return ResponseEntity.created(location).build();

    }

    @Override
    public ResponseEntity<EventDto> eventsIdGet(Long id) {
        LOGGER.info("GET events/{}", id);
        EventDto event = this.eventMapper.eventToEventDto(
            eventService.findOne(id));
        return ResponseEntity.ok(event);
    }
}
