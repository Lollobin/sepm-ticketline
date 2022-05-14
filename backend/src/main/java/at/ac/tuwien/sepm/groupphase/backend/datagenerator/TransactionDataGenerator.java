package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// TODO: Replace with proper dataGenerator class and create dataGenerator for users and addresses
@Profile("generateData")
@Component
public class TransactionDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public TransactionDataGenerator(
        TransactionRepository transactionRepository,
        UserRepository userRepository,
        AddressRepository addressRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    private void generateTransaction() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
        } else {
            Address address = new Address();
            address.setStreet("TestStreet 123");
            address.setZipCode("21938");
            address.setCity("testCity");
            address.setCountry("Austria");
            address.setHouseNumber("2");


            Address address2 = new Address();
            address2.setStreet("TestStreet 1233");
            address2.setZipCode("219338");
            address2.setCity("test3City");
            address2.setCountry("Aust3ria");
            address2.setHouseNumber("2");

            ApplicationUser user = new ApplicationUser();
            user.setEmail("admin@email.com");
            user.setFirstName("Admin");
            user.setLastName("User");
            user.setGender(Gender.FEMALE);
            user.setAddress(address);

            user.setPassword("password");
            user.setHasAdministrativeRights(true);
            user.setLoginTries(0);
            user.setMustResetPassword(false);
            user.setLockedAccount(false);

            userRepository.save(user);

            ApplicationUser user2 = new ApplicationUser();
            user2.setEmail("user@email.com");
            user2.setFirstName("Admin");
            user2.setLastName("User");
            user2.setGender(Gender.MALE);
            user2.setAddress(address2);
            user2.setPassword("password");
            user2.setHasAdministrativeRights(true);
            user2.setLoginTries(0);
            user2.setMustResetPassword(false);
            user2.setLockedAccount(false);
            userRepository.save(user2);

            Transaction transaction = new Transaction();
            transaction.setDate(LocalDate.of(2005, 11, 20));
            transaction.setUser(user);
            transactionRepository.save(transaction);

            Transaction transaction2 = new Transaction();
            transaction2.setDate(LocalDate.of(2005, 11, 20));
            transaction2.setUser(user);
            transactionRepository.save(transaction2);

            Transaction transaction3 = new Transaction();
            transaction3.setDate(LocalDate.of(2005, 11, 20));
            transaction3.setUser(user2);
            transactionRepository.save(transaction3);
        }
    }
}
