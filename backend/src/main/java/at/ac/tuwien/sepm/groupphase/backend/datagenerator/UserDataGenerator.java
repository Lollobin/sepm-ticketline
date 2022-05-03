package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Profile("generateData")
@Component
public class UserDataGenerator {

    private final UserRepository userRepository;

    public UserDataGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void generateUser() {

        userRepository.deleteAll();

        if (userRepository.findAll().size() > 0) {
            List<ApplicationUser> all = userRepository.findAll();

        } else {


            ApplicationUser user = new ApplicationUser();
            user.setEmail("admin@emaiwadwl.com");
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setGender(Gender.FEMALE);
            user.setStreet("TestStreet 123");
            user.setZipCode("21938");
            user.setCity("testCity");
            user.setCountry("Austria");

            byte[] emptyByte = new byte[]{1, 2};
            user.setPassword(emptyByte);
            user.setSalt(emptyByte);
            user.setHasAdministrativeRights(true);
            user.setLoginTries(0);
            user.setMustResetPassword(false);
            user.setLockedAccount(true);

            userRepository.save(user);

            ApplicationUser user4 = new ApplicationUser();
            user4.setEmail("admin@emaiwa123dwl.com");
            user4.setFirstName("Admin");
            user4.setLastName("User");
            user4.setGender(Gender.FEMALE);
            user4.setStreet("TestStreet 123");
            user4.setZipCode("21938");
            user4.setCity("testCity");
            user4.setCountry("Austria");

            emptyByte = new byte[]{1, 2};
            user4.setPassword(emptyByte);
            user4.setSalt(emptyByte);
            user4.setHasAdministrativeRights(true);
            user4.setLoginTries(0);
            user4.setMustResetPassword(false);
            user4.setLockedAccount(true);

            userRepository.save(user4);


            ApplicationUser user3 = new ApplicationUser();
            user3.setEmail("afaefdmin@emaiw32adwl.com");
            user3.setFirstName("Admin");
            user3.setLastName("User");
            user3.setGender(Gender.FEMALE);
            user3.setStreet("TestStreet 123");
            user3.setZipCode("21938");
            user3.setCity("testCity");
            user3.setCountry("Austria");

            emptyByte = new byte[]{1, 2};
            user3.setPassword(emptyByte);
            user3.setSalt(emptyByte);
            user3.setHasAdministrativeRights(true);
            user3.setLoginTries(0);
            user3.setMustResetPassword(false);
            user3.setLockedAccount(true);

            userRepository.save(user3);

            ApplicationUser user2 = new ApplicationUser();
            user2.setEmail("user@email.com");
            user2.setFirstName("Admin");
            user2.setLastName("User");
            user2.setGender(Gender.MALE);
            user2.setStreet("TestStreet 123");
            user2.setZipCode("21938");
            user2.setCity("testCity");
            user2.setCountry("Austria");
            user2.setPassword(emptyByte);
            user2.setSalt(emptyByte);
            user2.setHasAdministrativeRights(true);
            user2.setLoginTries(0);
            user2.setMustResetPassword(false);
            user2.setLockedAccount(false);
            userRepository.save(user2);
        }
    }
}
