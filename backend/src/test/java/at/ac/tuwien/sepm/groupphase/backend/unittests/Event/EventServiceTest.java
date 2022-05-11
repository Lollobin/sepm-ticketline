package at.ac.tuwien.sepm.groupphase.backend.unittests.Event;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.validator.EventValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock private EventRepository eventRepository;
    private final EventValidator eventValidator = new EventValidator();
    private EventService eventService;
    private final Event fakePersistedEvent = new Event();
    private final EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();

    @Spy
    private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);


    @BeforeEach
    void setUp(){
        eventService = new EventServiceImpl(eventRepository, eventValidator);
    }

    @Test
    void shouldSaveNewEvent(){

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);


        eventWithoutIdToSave.setName(EVENT_NAME);
        eventWithoutIdToSave.setDuration(EVENT_DURATION);
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        eventService.createEvent(eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdToSave));

        verify(eventRepository).save(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();

        assertEquals(EVENT_NAME, capturedEvent.getName());
        assertEquals(EVENT_DURATION.longValue(), capturedEvent.getDuration());
        assertEquals(EVENT_CATEGORY, capturedEvent.getCategory());
        assertEquals(EVENT_CONTENT, capturedEvent.getContent());

    }
    @Test
    void shouldNotSaveEventDueToName(){

        eventWithoutIdToSave.setName(EVENT_INVALID_NAME);
        eventWithoutIdToSave.setDuration(EVENT_DURATION);
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);


        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdToSave)));
        Assertions.assertEquals("Name of event can not be empty ", exception.getMessage());

    }

    @Test
    void shouldNotSaveEventDueToDuration(){

        eventWithoutIdToSave.setName(EVENT_NAME);
        eventWithoutIdToSave.setDuration(EVENT_INVALID_DURATION_LOWER);
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        //testing if duration can be lower than 10
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdToSave)));
        Assertions.assertEquals("Duration has to be at least 10 minutes and less than 6 hours (360 minutes)", exception.getMessage());

        //testing if duration can be higher than 360

        eventWithoutIdToSave.setDuration(EVENT_INVALID_DURATION_UPPER);
        exception = Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdToSave)));
        Assertions.assertEquals("Duration has to be at least 10 minutes and less than 6 hours (360 minutes)", exception.getMessage());


    }

    @Test
    void shouldNotSaveEventDueToLength(){

        eventWithoutIdToSave.setName(EVENT_INVALID_NAME_LENGTH);
        eventWithoutIdToSave.setDuration(EVENT_DURATION);
        eventWithoutIdToSave.setCategory(EVENT_INVALID_CATEGORY_LENGTH);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);


        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> eventService.createEvent(eventMapper.eventWithoutIdDtoToEvent(eventWithoutIdToSave)));
        Assertions.assertEquals("Name of event is too long & Category contains too many characters", exception.getMessage());
    }






}
