package at.ac.tuwien.sepm.groupphase.backend.service;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface TransactionPdfService {

    /**
     * Depending on the type of Booking this returns a PDF with relevant transaction data. Can be
     * either invoice, cancellation invoice or reservation confirmation. Users can only access their
     * own transactions.
     *
     * @param id id of the transaction
     * @return PDF resource
     * @throws IOException when parsing of pdf goes wrong
     */
    Resource getTransactionPdf(Long id) throws IOException;
}
