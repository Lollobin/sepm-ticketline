package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {


    /**
     * Fetches a user with the given email.
     *
     * @param email unique email of user to be fetched
     * @return unique user if it exists
     */
    ApplicationUser findUserByEmail(String email);

    /**
     * Saves a new User and the encapsuled Address entity to the database. All repository
     * ooperations are cascaded to the Address entity.
     *
     * @param user Entity to be saved
     * @return Saved User with the generated ids for the User and the persisted Address Entity
     */
    ApplicationUser save(ApplicationUser user);

    /**
     * Gets all users where lockedAccount is equal to the parameter.
     *
     * @param lockedAccount if true then only locked users will be returned and vice versa
     * @return List of ApplicationsUsers based on lockedAccount
     */
    List<ApplicationUser> findByLockedAccountEquals(boolean lockedAccount);

    /**
     * Unlocks a user.
     *
     * @param lockedAccount if false user will be unlocked and loginTries will be reset to 0
     *                      (locking not yet implemented)
     * @param userId        specifies which user will be unlocked
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.lockedAccount = ?1, a.loginTries=0 where a.userId = ?2")
    void unlockApplicationUser(boolean lockedAccount, long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.loginTries = a.loginTries+1 where a.email = :email")
    void increaseNumberOfFailedLoginAttempts(@Param("email") String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.lockedAccount = true where a.email = :email")
    void lockApplicationUser(@Param("email") String email);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.loginTries = 0 where a.email = :email")
    void resetNumberOfFailedLoginAttempts(@Param("email") String email);
}
