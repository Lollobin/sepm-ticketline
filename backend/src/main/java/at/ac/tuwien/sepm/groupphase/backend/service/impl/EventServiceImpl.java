package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAll(){
        LOGGER.debug("Find all events");
        return eventRepository.findAll();
    }

    @Override
    public Event findById(Long id){
        LOGGER.debug("Find event with id {}", id);
        return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event with ID " + id + " was not found"));
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.debug("Create new event {}", event);
        return eventRepository.save(event);
    }
}
