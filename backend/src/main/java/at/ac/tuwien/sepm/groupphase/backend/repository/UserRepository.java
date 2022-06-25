package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Gets a page of users who are not deleted.
     *
     * @return Page of non deleted ApplicationsUsers
     */
    Page<ApplicationUser> findByDeletedIsFalse(Pageable pageable);

    /**
     * Gets a page of users where lockedAccount is equal to the parameter.
     *
     * @param lockedAccount if true then only locked users will be returned and vice versa
     * @return Page of ApplicationsUsers based on lockedAccount
     */
    Page<ApplicationUser> findByLockedAccountEqualsAndDeletedIsFalse(boolean lockedAccount, Pageable pageable);

    /**
     * checks if there exists a user with the given email.
     *
     * @param email to be searched for
     * @return boolean if the given user exists
     */
    Boolean existsByEmail(String email);

    /**
     * Unlocks a user and resets number of failed attempts.
     *
     * @param lockedAccount if false user will be unlocked and loginTries will be reset to 0
     *                      (locking not yet implemented)
     * @param userId        specifies which user will be unlocked
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.lockedAccount = ?1, a.loginTries=0 where a.userId = ?2 and a.deleted = false")
    void unlockApplicationUser(boolean lockedAccount, long userId);


    /**
     * Increase the number of failed attempts of a user by 1.
     *
     * @param email unique user
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.loginTries = a.loginTries+1 where a.email = :email")
    void increaseNumberOfFailedLoginAttempts(@Param("email") String email);

    /**
     * Locks a user account.
     *
     * @param email mail of user account to be locked
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.lockedAccount = true where a.email = :email")
    void lockApplicationUser(@Param("email") String email);

    /**
     * Resets number of failed login attempts of user.
     *
     * @param email mail of the user.
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update ApplicationUser a set a.loginTries = 0 where a.email = :email")
    void resetNumberOfFailedLoginAttempts(@Param("email") String email);


    /**
     * Fetches a user with the given reset token.
     *
     * @param token unique token of user to be fetched
     * @return unique user if it exists
     */
    ApplicationUser findByResetPasswordToken(String token);
}
