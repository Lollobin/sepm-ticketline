package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     * Saves a new image locally and saves it in the database.
     *
     * @param body Image, which will be saved
     * @return ID of the newly persisted image
     */
    long save(MultipartFile body);

    void updateArticle(Long imageId, Article article);

}
