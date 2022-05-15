package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import java.util.List;

import at.ac.tuwien.sepm.groupphase.backend.service.validation.ShowValidator;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowRepository showRepository;
    private final ShowValidator showValidator;

    @Autowired
    public ShowServiceImpl(ShowRepository showRepository, ShowValidator showValidator) {
        this.showRepository = showRepository;
        this.showValidator = showValidator;
    }


    @Override
    public List<Show> findAll() {
        LOGGER.debug("Find all shows");
        return showRepository.findAll();
    }

    @Override
    public Show createShow(Show show) {
        LOGGER.debug("Create new show {}", show);

        showValidator.checkIfShowCorrect(show);

        return showRepository.save(show);
    }

    @Override
    public Show findOne(Long id) {
        Optional<Show> show = showRepository.findById(id);
        Hibernate.initialize(show);
        if (show.isPresent()) {
            return show.get();
        } else {
            throw new NotFoundException(String.format("Could not find show with id %s", id));
        }
    }
}
