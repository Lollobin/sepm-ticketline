package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketInfoApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketInfoService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketInfoEndpoint implements TicketInfoApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final TicketInfoService ticketInfoService;
    private final TicketMapper ticketMapper;
    private final ArtistMapper artistMapper;

    public TicketInfoEndpoint(TicketInfoService ticketInfoService, TicketMapper ticketMapper,
        ArtistMapper artistMapper) {
        this.ticketInfoService = ticketInfoService;
        this.ticketMapper = ticketMapper;
        this.artistMapper = artistMapper;
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<List<TicketWithShowInfoDto>> ticketInfoGet() {
        LOGGER.info("GET /ticketInfo");
        return ResponseEntity.ok(
            ticketMapper.ticketToTicketWithShowInfoDto(ticketInfoService.findAllByCurrentUser(), artistMapper));
    }
}
