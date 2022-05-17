package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginFailureHandlerTest {

    @Disabled
    @Test
    void whenAuthenticationFailure_thenUserServiceFindUserByEmail() {
    }

    @Disabled
    @Test
    void whenAuthenticationFailure_thenUserServiceIncreaseAttempts() {
    }

    @Disabled
    @Test
    void whenAuthenticationFailure_andMaxNumOfAttemptsReached_thenUserServiceLockUser() {
    }

    @Disabled
    @Test
    void whenAuthenticationFailure_andAfterLock_thenHandoverCustomExceptionMessage() {
    }
}
