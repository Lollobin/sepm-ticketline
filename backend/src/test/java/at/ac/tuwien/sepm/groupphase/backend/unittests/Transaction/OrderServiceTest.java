package at.ac.tuwien.sepm.groupphase.backend.unittests.Transaction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.OrderServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderServiceTest implements TestData {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private SectorPriceRepository sectorPriceRepository;
    @Mock
    private BookedInRepository bookedInRepository;
    private OrderService orderService;

    private final List<Transaction> transactionsToReturn = new ArrayList<>();

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(transactionRepository, authenticationFacade, sectorPriceRepository, bookedInRepository);
    }

    @Test
    void whenExistingUser_thenTransactionsByUserShouldBeFound() {
        ApplicationUser loggedInUser = new ApplicationUser();
        loggedInUser.setEmail(USER_EMAIL);

        Transaction transaction = new Transaction();
        transaction.setDate(TRANSACTION_DATE);
        transaction.setUser(loggedInUser);

        transactionsToReturn.add(transaction);

        when(transactionRepository.findAllByUserEmail(USER_EMAIL)).thenReturn(transactionsToReturn);
        when(authenticationFacade.getEmail()).thenReturn(USER_EMAIL);

        List<Transaction> found = orderService.findAllByCurrentUser();

        assertAll(
            () -> assertEquals(1, found.size()),
            () -> assertEquals(USER_EMAIL, found.get(0).getUser().getEmail())
        );
    }
}
