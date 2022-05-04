package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowRepository transactionRepository;

    public ShowServiceImpl(ShowRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Show findOne(Long id) {
        return transactionRepository.getById(id);
    }
}
