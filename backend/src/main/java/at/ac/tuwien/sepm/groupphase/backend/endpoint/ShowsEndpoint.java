package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public class ShowsEndpoint implements ShowsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowService showService;
    private final ShowMapper showMapper;

    public ShowsEndpoint(ShowService showService, ShowMapper showMapper) {
        this.showService = showService;
        this.showMapper = showMapper;
    }

    @Override
    public ResponseEntity<ShowDto> showsIdGet(Integer id) {
        ShowDto foundShow = this.showMapper.showToShowDto(
            this.showService.findOne(Long.valueOf(id)));
        return ResponseEntity.ok(foundShow);
    }
}
