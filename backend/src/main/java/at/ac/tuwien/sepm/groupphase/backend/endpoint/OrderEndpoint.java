package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.OrdersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TransactionMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class OrderEndpoint implements OrdersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final OrderService orderService;
    private final TransactionMapper transactionMapper;

    public OrderEndpoint(OrderService orderService, TransactionMapper transactionMapper) {
        this.orderService = orderService;
        this.transactionMapper = transactionMapper;
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<List<OrderDto>> ordersGet() {
        LOGGER.info("GET /orders");
        return ResponseEntity.ok(
            transactionMapper.transActionToOrderDto((orderService.findAllByCurrentUser())));
    }
}
