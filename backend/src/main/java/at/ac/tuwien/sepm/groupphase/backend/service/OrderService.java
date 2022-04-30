package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import java.util.List;

public interface OrderService {

    /**
     * Gets all orders of a person
     * @return all orders belonging to the current user
     */
    public List<TransactionDto> get();
}
