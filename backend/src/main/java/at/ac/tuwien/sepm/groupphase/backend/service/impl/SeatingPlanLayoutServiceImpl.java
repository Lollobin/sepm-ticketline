package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanLayoutService;
import java.util.Optional;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public class SeatingPlanLayoutServiceImpl implements SeatingPlanLayoutService {

    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final FileSystemRepository fileSystemRepository;

    public SeatingPlanLayoutServiceImpl(SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        FileSystemRepository fileSystemRepository) {
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
        this.fileSystemRepository = fileSystemRepository;
    }

    @Override
    public FileSystemResource findOne(Long id) {
        Optional<SeatingPlanLayout> seatingPlanLayout = this.seatingPlanLayoutRepository.findById(
            id);
        if (seatingPlanLayout.isEmpty()) {
            throw new NotFoundException(
                "Could not find given seating plan layout with the given id");
        }
        FileSystemResource foundFile = this.fileSystemRepository.findInFileSystem(
            seatingPlanLayout.get().getSeatingLayoutPath());

        if (!(foundFile.exists() && foundFile.isReadable() && foundFile.isFile())) {
            throw new NotFoundException("Could not find or open given seating plan layout file");
        }
        return foundFile;
    }
}
