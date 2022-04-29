package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
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
public class ShowEndpoint implements ShowsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShowService showService;
    private final ShowMapper showMapper;

    @Autowired
    public ShowEndpoint(ShowService showService, ShowMapper showMapper){
        this.showService = showService;
        this.showMapper = showMapper;
    }

    @Override
    public ResponseEntity<List<ShowDto>> showsGet(ShowSearchDto search){
        LOGGER.info("GET /shows");
        return new ResponseEntity<>(showService.findAll().stream().map(showMapper::showToShowDto).toList(), HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> showsPost(ShowWithoutIdDto showWithoutIdDto){
        LOGGER.info("POST /shows body: {}", showWithoutIdDto);
        ShowDto newShowDto = showMapper.showToShowDto(
            showService.createShow(
                showMapper.showWithoutIdDtoToShow(showWithoutIdDto)
            ));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
            buildAndExpand(newShowDto.getShowId()).toUri();
        LOGGER.debug("id {}", newShowDto.getShowId());
        return ResponseEntity.created(location).build();
    }
}
