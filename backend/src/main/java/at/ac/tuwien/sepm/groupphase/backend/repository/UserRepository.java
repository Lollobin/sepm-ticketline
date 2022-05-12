package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {

    ApplicationUser findUserByEmail(String email);

    ApplicationUser save(ApplicationUser user);

    List<ApplicationUser> findByLockedAccountEquals(boolean lockedAccount);

    @Transactional
    @Modifying
    @Query("update ApplicationUser a set a.lockedAccount = ?1 where a.userId = ?2")
    void unlockApplicationUser(boolean lockedAccount, long userId);
}
