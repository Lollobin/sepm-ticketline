package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SortDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
    public ResponseEntity<ShowSearchResultDto> showsGet(ShowSearchDto search, Integer pageSize,
        Integer requestedPage, SortDto sort) {
        LOGGER.info("GET /shows with searchDto: {}", search);

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Direction.valueOf(
                String.valueOf(sort)),
            "showId");
        ShowSearchResultDto resultDto;
        if (search.getDate() == null && (search.getEvent() == null || search.getEvent().isBlank())
            && search.getPrice() == null && search.getSeatingPlan() == null
            && search.getLocation() == null && search.getEventId() == null) {

            resultDto = showService.findAll(pageable);

            return ResponseEntity.ok(resultDto);

        }
        resultDto = showService.search(search, pageable);

        return ResponseEntity.ok(resultDto);
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> showsPost(ShowWithoutIdDto showWithoutIdDto) {
        LOGGER.info("POST /shows body: {}", showWithoutIdDto);

        ShowDto newShowDto;
        try {
            newShowDto = showMapper.showToShowDto(
                showService.createShow(showMapper.showWithoutIdDtoToShow(showWithoutIdDto),
                    Long.valueOf(showWithoutIdDto.getSeatingPlan()),
                    showWithoutIdDto.getSectorPrices()));
        } catch (NotFoundException e) {
            throw new ConflictException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e.getMessage());
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(newShowDto.getShowId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<ShowDto> showsIdGet(Long id) {
        LOGGER.info("GET shows/{}", id);
        ShowDto foundShow = this.showMapper.showToShowDto(this.showService.findOne(id));
        return ResponseEntity.ok(foundShow);
    }
}
