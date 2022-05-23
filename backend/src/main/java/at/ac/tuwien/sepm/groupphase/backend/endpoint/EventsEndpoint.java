package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.EventsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<EventSearchResultDto> eventsGet(
        EventSearchDto search, Integer pageSize, Integer requestedPage, String sort) {
        if (search.getName() == null && search.getCategory() == null
            && search.getDuration() == null) {
            /* this is just a placeholderlogic to keep current eventsgetall
            functional (if used anywhere, what i dont think) */
            List<EventDto> list = this.eventsGetAll();
            EventSearchResultDto result = new EventSearchResultDto().events(list).currentPage(0)
                .numberOfResults(list.size()).pagesTotal(1);
            return ResponseEntity.ok().body(result);
        }
        return EventsApi.super.eventsGet(search, pageSize, requestedPage, sort);
    }

    private List<EventDto> eventsGetAll() {
        LOGGER.info("GET /events");
        return eventService.findAll().stream().map(eventMapper::eventToEventDto)
            .toList();
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
