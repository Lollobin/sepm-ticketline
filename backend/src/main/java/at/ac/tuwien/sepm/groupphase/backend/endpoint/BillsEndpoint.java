package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.BillsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.InvoicePdfServiceImpl;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillsEndpoint implements BillsApi {

    InvoicePdfServiceImpl invoicePdfService;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public BillsEndpoint(
        InvoicePdfServiceImpl invoicePdfService) {
        this.invoicePdfService = invoicePdfService;
    }

    @Override
    public ResponseEntity<Resource> billsIdGet(
        Long id) {
        try {
            invoicePdfService.getInvoicePdf(id);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            LOGGER.error("mimimi");
            return ResponseEntity.internalServerError().build();
        }
    }
}
