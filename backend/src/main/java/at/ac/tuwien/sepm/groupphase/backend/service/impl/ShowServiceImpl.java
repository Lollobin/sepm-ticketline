package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    public ShowServiceImpl(ShowRepository showRepository, EventRepository eventRepository,
        ArtistRepository artistRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
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
