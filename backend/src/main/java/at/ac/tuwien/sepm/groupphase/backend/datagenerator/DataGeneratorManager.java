package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import java.lang.invoke.MethodHandles;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class DataGeneratorManager {

    private static final int NUMBER_OF_USERS = 20;

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDataGenerator userDataGenerator;

    public DataGeneratorManager(
        UserDataGenerator userDataGenerator) {
        this.userDataGenerator = userDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        LOGGER.debug("starting data generation");
        userDataGenerator.generateUsers(NUMBER_OF_USERS);
    }

}
