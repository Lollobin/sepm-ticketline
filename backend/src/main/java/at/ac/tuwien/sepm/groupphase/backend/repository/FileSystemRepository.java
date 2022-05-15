package at.ac.tuwien.sepm.groupphase.backend.repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {

    String resourcesDir = new File(FileSystemRepository.class.getResource("/")
        .getFile()).getPath();


    public String save(byte[] content, String imageName) throws IOException {
        Path newFile = Paths.get(resourcesDir + new Date().getTime() + "-" + imageName);
        Files.createDirectories(newFile.getParent());

        Files.write(newFile, content);

        return newFile.toAbsolutePath()
            .toString();
    }

    public FileSystemResource findInFileSystem(String location) {
        return new FileSystemResource(Paths.get(location));
    }
}
