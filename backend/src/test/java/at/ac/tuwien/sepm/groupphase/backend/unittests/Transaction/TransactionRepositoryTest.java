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
        entityManager.persist(ADDRESS_ENTITY);
        entityManager.flush();

        ApplicationUser testUser = new ApplicationUser();
        testUser.setFirstName(USER_FNAME);
        testUser.setLastName(USER_LNAME);
        testUser.setGender(USER_GENDER);
        testUser.setAddress(ADDRESS_ENTITY);
        testUser.setEmail(USER_EMAIL);
        testUser.setPassword(USER_PASSWORD);
        entityManager.persist(testUser);

        ApplicationUser adminUser = new ApplicationUser();
        adminUser.setFirstName(USER_FNAME);
        adminUser.setLastName(USER_LNAME);
        adminUser.setGender(USER_GENDER);
        adminUser.setAddress(ADDRESS_ENTITY);
        adminUser.setEmail(ADMIN_USER);
        adminUser.setPassword(USER_PASSWORD);
        entityManager.persist(adminUser);

        entityManager.flush();

        Transaction testTransaction = new Transaction();
        testTransaction.setUser(testUser);
        testTransaction.setDate(TRANSACTION_DATE);
        entityManager.persist(testTransaction);

        Transaction adminTransaction = new Transaction();
        adminTransaction.setUser(adminUser);
        adminTransaction.setDate(TRANSACTION_DATE);
        entityManager.persist(adminTransaction);

        entityManager.flush();
        entityManager.clear();

        List<Transaction> found = transactionRepository.findAllByUserEmail(USER_EMAIL);

        assertAll(
            () -> assertEquals(1, found.size()),
            () -> assertEquals(USER_EMAIL, found.get(0).getUser().getEmail())
        );


    }


}
