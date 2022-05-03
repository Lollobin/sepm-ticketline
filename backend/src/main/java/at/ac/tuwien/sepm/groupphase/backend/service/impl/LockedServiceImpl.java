package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LockedServiceImpl implements LockedService {

    private final UserRepository userRepository;

    public LockedServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<ApplicationUser> unlockApplicationUser(Long id, boolean unlock){

        if(userRepository.existsById(id)){
//            int ret = userRepository.unlockApplicationUser(id, unlock);
//            // das prüft wie viele zeilen verändert wurden
//            if (ret != 1) {
//
//            }
//            List allAfter = userRepository.findAll();

            return userRepository.findById(id);
        } else {
            throw new NotFoundException("User with id " + id + " is not present");
        }



    }
}
