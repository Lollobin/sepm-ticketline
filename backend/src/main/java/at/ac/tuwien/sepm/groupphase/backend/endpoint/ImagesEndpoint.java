package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ImagesApi;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ImagesEndpoint implements ImagesApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ImageService imageService;

    public ImagesEndpoint(ImageService imageService) {
        this.imageService = imageService;
    }


    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> imagesPost(MultipartFile fileName) {
        LOGGER.info("POST /images with body {}", fileName);

        long imageId = imageService.save(fileName);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(imageId).toUri();

        return ResponseEntity.created(location).build();
    }

    @Override
    public ResponseEntity<Resource> imagesIdGet(Long id) {
        LOGGER.info("GET /images/{}", id);
        Resource resource = imageService.getImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }
}
