package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithTicketsSoldDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TopEventsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
public class TopEventsEndpoint implements TopEventsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final EventMapper eventMapper;
    private final EventService eventService;


    public TopEventsEndpoint(EventMapper eventMapper, EventService eventService) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
    }

    @Override
    public ResponseEntity<List<EventWithTicketsSoldDto>> topEventsGet(TopEventSearchDto search) {
        LOGGER.info("GET /topEvents body: {}", search);

        return ResponseEntity.ok(
            eventMapper.eventToEventWithTicketsSoldDto(eventService.getTopEvents())
        );
    }
}
