package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowInformationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowTicketsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowTicketsEndpoint implements ShowTicketsApi {

    private final ShowTicketService showTicketService;

    public ShowTicketsEndpoint(ShowTicketService showTicketsEndpoint) {
        this.showTicketService = showTicketsEndpoint;
    }

    @Override
    public ResponseEntity<ShowInformationDto> showTicketsIdGet(Integer id) {
        return ResponseEntity.ok(this.showTicketService.findOne(Long.valueOf(id)));
    }
}
