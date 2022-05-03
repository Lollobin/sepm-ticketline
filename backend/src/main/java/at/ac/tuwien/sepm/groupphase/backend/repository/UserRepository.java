package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UserRepository extends JpaRepository<ApplicationUser,Long> {
   ApplicationUser findUserByEmail(String eMail);
   ApplicationUser save(ApplicationUser user);


   @Query("select a from ApplicationUser a where a.lockedAccount = true")
   List<ApplicationUser> findByLockedState();

}
