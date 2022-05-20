package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
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
    public ResponseEntity<List<ShowDto>> showsGet(ShowSearchDto search) {
        LOGGER.info("GET /shows");
        return new ResponseEntity<>(
            showService.findAll().stream().map(showMapper::showToShowDto).toList(), HttpStatus.OK);
    }


    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> showsPost(ShowWithoutIdDto showWithoutIdDto) {
        LOGGER.info("POST /shows body: {}", showWithoutIdDto);

        Show mappedShow;
        try {
            mappedShow = showMapper.showWithoutIdDtoToShow(showWithoutIdDto);
        } catch (NotFoundException e){
            throw new ConflictException(e.getMessage());
        }

        ShowDto newShowDto = showMapper.showToShowDto(
            showService.createShow(
                mappedShow, Long.valueOf(showWithoutIdDto.getSeatingPlan())
            ));

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newShowDto.getShowId())
            .toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<ShowDto> showsIdGet(Long id) {
        LOGGER.info("GET shows/{}", id);
        ShowDto foundShow = this.showMapper.showToShowDto(
            this.showService.findOne(id));
        return ResponseEntity.ok(foundShow);
    }
}
