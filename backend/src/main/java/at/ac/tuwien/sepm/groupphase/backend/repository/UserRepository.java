package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    ApplicationUser findUserByEmail(String eMail);

    ApplicationUser save(ApplicationUser user);


    @Query("select a from ApplicationUser a where a.lockedAccount = true")
    List<ApplicationUser> findByLockedState();

    List<ApplicationUser> findByLockedAccountEquals(boolean lockedAccount);


    @Transactional
    @Modifying
    @Query("update ApplicationUser a set a.lockedAccount = ?1 where a.userId = ?2")
    int unlockApplicationUser(boolean lockedAccount, long userId);


}
