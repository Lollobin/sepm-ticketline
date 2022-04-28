package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.invoke.MethodHandles;
import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/shows")
public class ShowEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShowService showService;
    private final ShowMapper showMapper;

    @Autowired
    public ShowEndpoint(ShowService showService, ShowMapper showMapper){
        this.showService = showService;
        this.showMapper = showMapper;
    }

    @Secured("ROLE_USER")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find all shows", security = @SecurityRequirement(name = "apiKey"))
    public Stream<ShowDto> findAll(){
        LOGGER.info("GET /shows");
        return showService.findAll().stream().map(showMapper::showToShowDto);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new show", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<ShowDto> createShow(@Valid @RequestBody ShowWithoutIdDto showWithoutIdDto){
        LOGGER.info("POST /shows body: {}", showWithoutIdDto);
        ShowDto newShowDto = showMapper.showToShowDto(
            showService.createShow(
                showMapper.showWithoutIdDtoToShow(showWithoutIdDto)
            ));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
            buildAndExpand(newShowDto.getShowId()).toUri();
        LOGGER.debug("id {}", newShowDto.getShowId());
        return ResponseEntity.created(location).body(newShowDto);
    }
}
