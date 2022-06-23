package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class ImageGenerator {

    private static final File[] files = new File(
        "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/images").listFiles();
    private final ImageRepository imageRepository;
    private final FileSystemRepository fileSystemRepository;
    private final Random numGenerator = new Random();


    public ImageGenerator(ImageRepository imageRepository,
        FileSystemRepository fileSystemRepository) {
        this.imageRepository = imageRepository;
        this.fileSystemRepository = fileSystemRepository;
    }

    public List<Long> generateSpecificImages(String[] names) {
        List<Long> imageIds = new ArrayList<>();

        for (String name : names) {

            File imageFile = new File(
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/specificImages/"
                    + name);
            try {
                byte[] content = Files.readAllBytes(Path.of(imageFile.getAbsolutePath()));
                String savedImagePath = fileSystemRepository.save(content, name);

                Image image = new Image();
                image.setFilePath(savedImagePath);

                imageIds.add(imageRepository.save(image).getImageId());
            } catch (IOException e) {
                //WHEN IMAGE NOT FOUND DO NOTHING
            }
        }
        return imageIds;
    }

    public List<Long> generateData(int numberOfImages) throws IOException {

        List<Long> imageIds = new ArrayList<>();
        List<String> imageNames = new ArrayList<>();
        for (int i = 0; i < numberOfImages; i++) {
            int fileNum = numGenerator.nextInt(files.length);

            String pathToImage = files[fileNum].getAbsolutePath();
            String currentImageName = files[fileNum].getName();
            if (imageNames.contains(currentImageName)) {
                i--;
            } else {

                imageNames.add(files[fileNum].getName());
                byte[] content = Files.readAllBytes(Path.of(pathToImage));

                String savedImagePath = fileSystemRepository.save(content, currentImageName);

                Image image = new Image();
                image.setFilePath(savedImagePath);

                imageIds.add(imageRepository.save(image).getImageId());
            }


        }

        return imageIds;

    }
}
