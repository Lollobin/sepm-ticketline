package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookedInRepository extends JpaRepository<BookedIn, Long> {

}
