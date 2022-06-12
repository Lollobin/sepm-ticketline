package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.BillsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TransactionPdfService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillsEndpoint implements BillsApi {

    TransactionPdfService transactionPdfService;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public BillsEndpoint(
        TransactionPdfService transactionPdfService) {
        this.transactionPdfService = transactionPdfService;
    }

    @Override
    public ResponseEntity<Resource> billsIdGet(
        Long id) {
        try {
            return ResponseEntity.ok().body(transactionPdfService.getTransactionPdf(id));
        } catch (IOException e) {
            LOGGER.error("Error building PDF", e);
        }
        return ResponseEntity.noContent().build();
    }
}
