package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.IAuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final TransactionRepository transactionRepository;
    private final IAuthenticationFacade authenticationFacade;

    public OrderServiceImpl(TransactionRepository transactionRepository,
        IAuthenticationFacade authenticationFacade) {
        this.transactionRepository = transactionRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public List<Transaction> findAllByCurrentUser() {
        String email = authenticationFacade.getEmail();
        LOGGER.debug("Looking for orders by '{}'", email);

        return transactionRepository.findAllByUserEmail(email);
    }
}
