package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /*
    List<Transaction> findAllByUserContainsEmailAddress()

    Authentication authentication = authenticationFacade.getAuthentication();

    Object details = authentication.getDetails();

    ApplicationUser user = (ApplicationUser) authentication.getDetails();

     */
}
