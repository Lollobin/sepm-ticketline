package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class LockerUserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;


    @Test
    public void shouldReturnAllLockedUsers(){

        createUser();


        List<ApplicationUser> lockedUser =  userRepository.findByLockedAccountEquals(true);

        assertThat(lockedUser.size()).isEqualTo(3);
        assertThat(lockedUser.get(0).isLockedAccount()).isEqualTo(true);
        assertThat(lockedUser.get(1).isLockedAccount()).isEqualTo(true);
        assertThat(lockedUser.get(2).isLockedAccount()).isEqualTo(true);

        Assertions.assertNotEquals(lockedUser.get(0), lockedUser.get(1));
        Assertions.assertNotEquals(lockedUser.get(1), lockedUser.get(2));
        Assertions.assertNotEquals(lockedUser.get(0), lockedUser.get(2));


    }
    @Test
    public void shouldChangeLockedToFalse(){
        createUser();

        List<ApplicationUser> allUsers = userRepository.findAll();

        assertThat(allUsers.get(0).isLockedAccount()).isEqualTo(true);
        long id = allUsers.get(0).getUserId();

        userRepository.unlockApplicationUser(false, id);

        Optional<ApplicationUser> optionalApplicationUser = userRepository.findById(id);


        assertThat(optionalApplicationUser.get().isLockedAccount()).isEqualTo(false);
        assertThat(optionalApplicationUser.get().getEmail()).isEqualTo("admin@emaiwadwl.com");

        assertThat(allUsers.get(2).isLockedAccount()).isEqualTo(true);
        long id3 = allUsers.get(2).getUserId();

        userRepository.unlockApplicationUser(false, id3);

        Optional<ApplicationUser> optionalApplicationUser3 = userRepository.findById(id3);


        assertThat(optionalApplicationUser3.get().isLockedAccount()).isEqualTo(false);
        assertThat(optionalApplicationUser3.get().getEmail()).isEqualTo("afaefdmin@emaiw32adwl.com");

        assertThat(allUsers.get(3).isLockedAccount()).isEqualTo(false);
        assertThat(allUsers.get(3).getEmail()).isEqualTo("user@email.com");

    }

    private void createUser(){
        Address address = new Address();
        address.setStreet("TestStreet 123");
        address.setZipCode("21938");
        address.setCity("testCity");
        address.setCountry("Austria");
        address.setHouseNumber("2");

        addressRepository.save(address);

        Address address2 = new Address();
        address2.setStreet("TestStreet 1233");
        address2.setZipCode("219338");
        address2.setCity("test3City");
        address2.setCountry("Aust3ria");
        address2.setHouseNumber("2");

        addressRepository.save(address2);

        Address address3 = new Address();
        address3.setStreet("TestStreet 32123");
        address3.setZipCode("2321938");
        address3.setCity("testC32ity");
        address3.setCountry("Au32stria");
        address3.setHouseNumber("2");

        addressRepository.save(address3);

        Address address4 = new Address();
        address4.setStreet("TestStr321eet 1233");
        address4.setZipCode("219312338");
        address4.setCity("test331City");
        address4.setCountry("Aus312t3ria");
        address4.setHouseNumber("2");
        addressRepository.save(address4);



        ApplicationUser user = new ApplicationUser();
        user.setEmail("admin@emaiwadwl.com");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setGender(Gender.FEMALE);
        user.setAddress(address);

        byte[] emptyByte = new byte[]{1, 2};
        user.setPassword(emptyByte);
        user.setHasAdministrativeRights(true);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(true);

        userRepository.save(user);

        ApplicationUser user4 = new ApplicationUser();
        user4.setEmail("admin@emaiwa123dwl.com");
        user4.setFirstName("Admin");
        user4.setLastName("User");
        user4.setGender(Gender.FEMALE);
        user4.setAddress(address2);

        emptyByte = new byte[]{1, 2};
        user4.setPassword((emptyByte));
        user4.setHasAdministrativeRights(true);
        user4.setLoginTries(0);
        user4.setMustResetPassword(false);
        user4.setLockedAccount(true);

        userRepository.save(user4);


        ApplicationUser user3 = new ApplicationUser();
        user3.setEmail("afaefdmin@emaiw32adwl.com");
        user3.setFirstName("Admin");
        user3.setLastName("User");
        user3.setGender(Gender.FEMALE);
        user3.setAddress(address3);

        emptyByte = new byte[]{1, 2};
        user3.setPassword(emptyByte);
        user3.setHasAdministrativeRights(true);
        user3.setLoginTries(0);
        user3.setMustResetPassword(false);
        user3.setLockedAccount(true);

        userRepository.save(user3);

        ApplicationUser user2 = new ApplicationUser();
        user2.setEmail("user@email.com");
        user2.setFirstName("Admin");
        user2.setLastName("User");
        user2.setGender(Gender.MALE);
        user2.setAddress(address4);

        emptyByte = new byte[]{1, 2};
        user2.setPassword((emptyByte));
        user2.setHasAdministrativeRights(true);
        user2.setLoginTries(0);
        user2.setMustResetPassword(false);
        user2.setLockedAccount(false);
        userRepository.save(user2);
    }
}
