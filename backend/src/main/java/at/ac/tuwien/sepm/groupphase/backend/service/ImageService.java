package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * Saves a new image locally and saves it in the database.
     *
     * @param body Image, which will be saved
     * @return ID of the newly persisted image
     */
    long save(MultipartFile body);

    /**
     * Update the article field of already persisted images when newly created article contains an
     * image.
     *
     * @param imageId used to identify which persisted image should be updated
     * @param article sets the article to the image
     */
    void updateArticle(Long imageId, Article article);

    /**
     * Get the Resource of an image.
     *
     * @param imageId gets the ressource of the image with the given ID
     * @return image resource
     */
    Resource getImageById(Long imageId);

}
