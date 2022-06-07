package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final FileSystemRepository fileSystemRepository;
    private final ImageRepository imageRepository;

    public ImageServiceImpl(FileSystemRepository fileSystemRepository,
        ImageRepository imageRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.imageRepository = imageRepository;
    }

    @Override
    public long save(MultipartFile body) {

        LOGGER.trace("saving new image");

        Image toSave = new Image();

        try {

            toSave.setFilePath(fileSystemRepository.save(body.getBytes(),
                body.getOriginalFilename()));
            imageRepository.save(toSave);
            return toSave.getImageId();

        } catch (IOException e) {
            throw new ConflictException("Error occurred when saving image");
        }


    }


    @Override
    public void updateArticle(Long imageId, Article article) {

        LOGGER.trace("Setting for imageId {} article: {}", imageId, article);

        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isPresent()) {
            Image imageToUpdate = optionalImage.get();
            imageToUpdate.setArticle(article);
            imageRepository.save(imageToUpdate);
        } else {
            throw new NotFoundException("image not found");
        }


    }

}
