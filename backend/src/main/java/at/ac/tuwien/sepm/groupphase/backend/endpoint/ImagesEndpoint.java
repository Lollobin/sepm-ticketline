package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ImagesApi;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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


    @Override
    public ResponseEntity<Void> imagesPost(MultipartFile fileName) {
        LOGGER.info("POST /images with body {}", fileName);

        long imageId = imageService.save(fileName);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(imageId).toUri();

        return ResponseEntity.noContent().location(location).build();
    }
}
