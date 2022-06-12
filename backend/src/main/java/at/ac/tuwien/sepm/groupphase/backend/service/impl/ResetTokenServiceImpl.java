package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.ResetTokenService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ResetTokenServiceImpl implements ResetTokenService {

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
