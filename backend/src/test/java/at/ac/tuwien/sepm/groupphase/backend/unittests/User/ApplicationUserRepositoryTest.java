package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;



import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class ApplicationUserRepositoryTest{
    @Autowired
    private UserRepository userRepository;

    @Disabled
    @Test
    void shouldSaveOneUser() {
        ApplicationUser testuser = new ApplicationUser();
        userRepository.save(testuser);
        assertTrue(testuser.getUserId()>0);
    }


}
