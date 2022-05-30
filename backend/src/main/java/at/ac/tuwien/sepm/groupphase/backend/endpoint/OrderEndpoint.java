package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrdersPageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.OrdersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TransactionMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
    public ResponseEntity<OrdersPageDto> ordersGet(Integer pageSize, Integer requestedPage) {
        LOGGER.info("GET /orders");

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Sort.by("date").descending());

        return ResponseEntity.ok(
            transactionMapper.transactionPageToOrdersPageDto(
                orderService.findAllByCurrentUser(pageable))
        );
    }
}
