package at.ac.tuwien.sepm.groupphase.backend.unittests.Transaction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class TransactionRepositoryTest implements TestData {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    void whenFindAllByUserEmail_thenReturnOnlyTransactionsByUser() {
        ApplicationUser testUser = new ApplicationUser();
        testUser.setFirstName(USER_FNAME);
        testUser.setLastName(USER_LNAME);
        testUser.setGender(USER_GENDER);
        testUser.setCity(USER_CITY);
        testUser.setCountry(USER_CTRY);
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(new byte[]{});
        testUser.setSalt(new byte[]{});
        testUser.setStreet(USER_STREET);
        testUser.setZipCode(USER_ZIPCODE);
        entityManager.persist(testUser);

        ApplicationUser adminUser = new ApplicationUser();
        adminUser.setFirstName(USER_FNAME);
        adminUser.setLastName(USER_LNAME);
        adminUser.setGender(USER_GENDER);
        adminUser.setCity(USER_CITY);
        adminUser.setCountry(USER_CTRY);
        adminUser.setEmail(ADMIN_USER);
        adminUser.setPassword(new byte[]{});
        adminUser.setSalt(new byte[]{});
        adminUser.setStreet(USER_STREET);
        adminUser.setZipCode(USER_ZIPCODE);
        entityManager.persist(adminUser);

        Transaction testTransaction = new Transaction();
        testTransaction.setUser(testUser);
        testTransaction.setDate(TRANSACTION_DATE);
        entityManager.persist(testTransaction);

        Transaction adminTransaction = new Transaction();
        adminTransaction.setUser(adminUser);
        adminTransaction.setDate(TRANSACTION_DATE);
        entityManager.persist(adminTransaction);

        entityManager.flush();

        List<Transaction> found = transactionRepository.findAllByUserEmail(USER_EMAIL);

        assertAll(
            () -> assertEquals(1, found.size()),
            () -> assertEquals(USER_EMAIL, found.get(0).getUser().getEmail())
        );


    }


}
