package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import java.util.List;

public interface OrderService {

    /**
     * Returns all orders of the current user.
     *
     * @return all orders belonging to the current user
     */
    List<Transaction> findAllByCurrentUser();

    /**
     * Generates a Transaction based on a list of tickets.
     *
     * @param tickets The tickets that are used
     * @param user The user that performed the transaction
     * @param bookingType The type of purchase/reservation that is performed
     */
    void generateTransaction(List<Ticket> tickets, ApplicationUser user, BookingType bookingType);
}
