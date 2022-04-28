package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.EventsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class EventEndpoint implements EventsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final NativeWebRequest request;

    @Autowired
    public EventEndpoint(EventService eventService, EventMapper eventMapper, NativeWebRequest request){
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.request = request;
    }

//    @Secured("ROLE_USER")
//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    @Operation(summary = "Find all events", security = @SecurityRequirement(name = "apiKey"))
//    public Stream<EventDto> findAll(){
//        LOGGER.info("GET /events");
//        return eventService.findAll().stream().map(eventMapper::eventToEventDto);
//    }
//
//    @Secured("ROLE_ADMIN")
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    @Operation(summary = "Create a new event", security = @SecurityRequirement(name = "apiKey"))
//    public EventWithoutIdDto createEvent(@Valid @RequestBody EventWithoutIdDto eventWithoutIdDto){
//        LOGGER.info("POST /events body: {}", eventWithoutIdDto);
//        return eventMapper.eventToEventWithoutIdDto(
//            eventService.createEvent(
//                eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdDto)
//            )
//        );
//    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<List<EventDto>> eventsGet(EventSearchDto search){
        LOGGER.info("GET /events");
        List<EventDto> eventDtos = eventService.findAll().stream().map(eventMapper::eventToEventDto).toList();
        return ResponseEntity.ok().body(eventDtos);
    }

    @Override
    public ResponseEntity<EventDto> eventsPost(EventWithoutIdDto eventWithoutIdDto){
        LOGGER.info("POST /events body: {}", eventWithoutIdDto);
        EventDto eventDto = eventMapper.eventToEventDto(
            eventService.createEvent(
                eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdDto)
            )
        );
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(eventDto.getEventId())
            .toUri();

        return ResponseEntity.created(location).body(eventDto);
    }

}
