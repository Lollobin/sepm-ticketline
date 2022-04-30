package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.OrdersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TransactionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class OrderEndpoint implements OrdersApi {

    private final OrderService orderService;

    public OrderEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<List<TransactionDto>> ordersGet() {

        return ResponseEntity.ok(orderService.get());
    }
}
