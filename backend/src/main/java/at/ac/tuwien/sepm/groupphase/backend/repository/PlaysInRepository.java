package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.PlaysIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.PlaysInId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaysInRepository extends JpaRepository<PlaysIn, PlaysInId> {

}
