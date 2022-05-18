package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketInfoService;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketInfoServiceImpl implements TicketInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final TicketRepository ticketRepository;
    private final AuthenticationUtil authenticationFacade;

    public TicketInfoServiceImpl(TicketRepository ticketRepository,
        AuthenticationUtil authenticationFacade) {
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public List<Ticket> findAllByCurrentUser() {
        String email = authenticationFacade.getEmail();
        LOGGER.debug("Looking for tickets by '{}'", email);

        OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);

        return ticketRepository.findAllByPurchasedByEmailAndShowDateAfterOrReservedByEmailAndShowDateAfterOrderByShowDateAsc(
            email, yesterday, email, yesterday);
    }
}
