package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.hibernate.type.BinaryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class TransactionDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final OrderRepository orderRepository;

    public TransactionDataGenerator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    private void generateMessage() {
        if (!orderRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
        } else {
            User user = new User();
            user.setEmail("test@email.com");
            user.setFirstName("Benno");
            user.setLastName("Kossatz");
            user.setGender(Gender.MALE);
            user.setStreet("TestStreet 123");
            user.setZipCode("21938");
            user.setCity("testCity");
            user.setCountry("Austria");

            byte[] emptyByte = new byte[]{};
            user.setPassword(emptyByte);
            user.setSalt(emptyByte);
            user.setHasAdministrativeRights(true);
            user.setLoginTries(0);
            user.setMustResetPassword(false);
            user.setLockedAccount(false);


            Transaction transaction = new Transaction();
            transaction.setTransactionId(1);
            transaction.setDate(LocalDate.now());
            transaction.setUser(user);

            orderRepository.save(transaction);
        }
    }
}
