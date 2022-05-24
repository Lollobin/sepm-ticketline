package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(
        value =
            "SELECT * FROM transaction t"
                + " NATURAL JOIN application_user u"
                + " WHERE u.email = (:email)"
                + " ORDER BY t.date DESC",
        nativeQuery = true)
    List<Transaction> findAllByUserEmail(@Param("email") String email);

    Transaction getByTransactionId(Long transactionId);

    List<Transaction> findAllByUserEmailOrderByDateDesc(String email);
}
