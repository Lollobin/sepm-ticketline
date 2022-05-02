package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.JpaUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class TransactionDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final TransactionRepository transactionRepository;
    private final JpaUserRepository jpaUserRepository;

    public TransactionDataGenerator(TransactionRepository transactionRepository,
        JpaUserRepository jpaUserRepository) {
        this.transactionRepository = transactionRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @PostConstruct
    private void generateMessage() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
        } else {
            ApplicationUser user = new ApplicationUser();
            user.setEmail("admin@email.com");
            user.setFirstName("Benno");
            user.setLastName("Kossatz");
            user.setGender(Gender.MALE);
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
            user.setLockedAccount(false);

            jpaUserRepository.save(user);

            ApplicationUser user2 = new ApplicationUser();
            user2.setEmail("user@email.com");
            user2.setFirstName("Benno");
            user2.setLastName("Kossatz");
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

            jpaUserRepository.save(user2);

            Transaction transaction = new Transaction();
            transaction.setTransactionId(1);
            transaction.setDate(LocalDate.now());
            transaction.setUser(user);

            transactionRepository.save(transaction);

            Transaction transaction2 = new Transaction();
            transaction2.setTransactionId(2);
            transaction2.setDate(LocalDate.now());
            transaction2.setUser(user2);

            transactionRepository.save(transaction2);
        }
    }
}
