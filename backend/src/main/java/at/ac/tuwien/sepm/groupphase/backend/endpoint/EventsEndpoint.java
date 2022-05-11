package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.EventsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class EventsEndpoint implements EventsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final NativeWebRequest request;

    public EventsEndpoint(EventService eventService, EventMapper eventMapper, NativeWebRequest request) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<List<EventDto>> eventsGet(EventSearchDto search) {
        LOGGER.info("GET /events");
        List<EventDto> eventDtos = eventService.findAll().stream().map(eventMapper::eventToEventDto).toList();
        return ResponseEntity.ok().body(eventDtos);
    }

    @Override
    public ResponseEntity<Void> eventsPost(EventWithoutIdDto eventWithoutIdDto) {
        LOGGER.info("POST /events body: {}", eventWithoutIdDto);

        try {

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
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }

    }

    @Override
    public ResponseEntity<EventDto> eventsIdGet(Long id) {
        LOGGER.info("GET events/{}", id);
        EventDto event = this.eventMapper.eventToEventDto(
            eventService.findOne(id));
        return ResponseEntity.ok(event);
    }
}
