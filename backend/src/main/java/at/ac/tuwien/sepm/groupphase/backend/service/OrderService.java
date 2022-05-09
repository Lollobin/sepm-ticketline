package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import java.util.List;

public interface OrderService {

    /**
     * Returns all orders of the current user.
     *
     * @return all orders belonging to the current user
     */
    List<Transaction> findAllByCurrentUser();
}
