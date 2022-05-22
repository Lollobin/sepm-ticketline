package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Profile("generateData")
@Component
public class SeatingPlanLayoutDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final FileSystemRepository fileSystemRepository;

    public SeatingPlanLayoutDataGenerator(
        FileSystemRepository fileSystemRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
    }

    public void generateSeatingPlanLayouts() throws IOException {
        LOGGER.debug("generating seating plan layouts");

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();

        FileInputStream fis = new FileInputStream(ResourceUtils.getFile(
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan1.json")
            .getAbsoluteFile());
        String path = this.fileSystemRepository.save(
            fis.readAllBytes(), "test");
        fis.close();

        seatingPlanLayout.setSeatingLayoutPath(path);

        seatingPlanLayoutRepository.save(seatingPlanLayout);
    }
}
