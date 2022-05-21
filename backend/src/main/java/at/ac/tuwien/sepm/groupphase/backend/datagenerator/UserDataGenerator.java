package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final Faker faker = new Faker();

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void generateUsers(int numberOfUsers) {

        if (!userRepository.findAll().isEmpty()) {
            LOGGER.debug("users already generated");
            return;
        }

        LOGGER.debug("generating {} users", numberOfUsers);

        for (int i = 0; i < numberOfUsers; i++) {
            ApplicationUser user = generateRandomApplicationUser();
            LOGGER.debug("saving user {}", user);
            userRepository.save(user);
        }

        ApplicationUser admin = generateRandomApplicationUser();
        admin.setEmail("admin@email.com");
        admin.setPassword("password");
        admin.setHasAdministrativeRights(true);
        admin.setLoginTries(0);
        admin.setMustResetPassword(false);
        admin.setLockedAccount(false);
        userRepository.save(admin);

        ApplicationUser user = generateRandomApplicationUser();
        user.setEmail("user@email.com");
        user.setPassword("password");
        user.setHasAdministrativeRights(false);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(false);
        userRepository.save(user);

    }

    private ApplicationUser generateRandomApplicationUser() {
        ApplicationUser user = new ApplicationUser();
        user.setEmail(faker.internet().emailAddress());
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setGender(faker.options().option(Gender.class));
        user.setAddress(generateRandomAddress());
        user.setPassword(faker.internet().password(10, 30));
        // TODO: find better way to generate data for following fields
        user.setHasAdministrativeRights(false);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(false);
        return user;
    }

    private Address generateRandomAddress() {
        Address address = new Address();
        address.setHouseNumber(faker.address().buildingNumber());
        address.setStreet(faker.address().streetName());
        address.setCity(faker.address().city());
        address.setCountry(faker.address().country());
        address.setZipCode(faker.address().zipCode());
        return address;
    }
}
