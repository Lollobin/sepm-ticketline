package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {


    /**
     * Find a user in the context of Spring Security based on the email address <br> For more
     * information have a look at this tutorial: https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    /**
     * Find an application user based on the email address.
     *
     * @param email the email address
     * @return a application user
     */
    ApplicationUser findApplicationUserByEmail(String email);

    /**
     * Save a new User.
     *
     * @param user the user Object to be saved
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException when Validation
     *                                                                            fails because of
     *                                                                            Duplicate Email,
     *                                                                            invalid field
     *                                                                            values, or whitespace-only
     *                                                                            values.
     */
    void save(UserWithPasswordDto user);

    /**
     * Updates a User with the given id from token in the database.
     *
     * @param userWithPasswordDto with new user data to be updated to
     */
    void put(UserWithPasswordDto userWithPasswordDto);

    /**
     * * Return a page of users whose locked status is according to the parameter.
     *
     * @param filterLocked true searches for locked users, false searches for all users.
     * @return page of users
     */
    Page<ApplicationUser> findAll(Boolean filterLocked, Pageable pageable);

    /**
     * Returns the information of the current user.
     *
     * @return user entity of the current user
     */
    ApplicationUser findByCurrentUser();

    /**
     * Increase the Number of failed login attempts by one.
     *
     * @param user whose login attempt number will be increased
     */
    void increaseNumberOfFailedLoginAttempts(ApplicationUser user);

    /**
     * Set the lockedAccount field to true.
     *
     * @param user whose account will be locked
     */
    void lockUser(ApplicationUser user);

    /**
     * Reset the number of failed login attempts of user to zero.
     *
     * @param user whose login attempts will be reset
     */
    void resetNumberOfFailedLoginAttempts(ApplicationUser user);

    /**
     * Request a password reset mail. this will be called if a user requests the reset himself
     *
     * @param passwordResetDto contains email and clientURI of the user that requested the password reset
     */
    void requestPasswordReset(PasswordResetDto passwordResetDto);

    /**
     * Attempt to change the password of the user with unique resettoken.
     *
     * @param passwordUpdateDto contains the new password and the unique token
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException when password not
     *                                                                            valid or token not
     *                                                                            valid.
     */
    void attemptPasswordUpdate(PasswordUpdateDto passwordUpdateDto);
}
