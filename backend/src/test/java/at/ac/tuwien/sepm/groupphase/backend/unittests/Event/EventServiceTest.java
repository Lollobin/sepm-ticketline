package at.ac.tuwien.sepm.groupphase.backend.unittests.Event;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_CATEGORY_LENGTH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_DURATION_LOWER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_DURATION_UPPER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_NAME_LENGTH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    private final EventValidator eventValidator = new EventValidator();
    private EventService eventService;
    private final Event fakePersistedEvent = new Event();
    private final Event eventToSave = new Event();

    @Spy
    private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);


    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, eventValidator, eventMapper);
        eventRepository.deleteAll();
    }


    @Test
    void should_CreateNewEvent_When_EventIsValid() {

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        eventToSave.setName(EVENT_NAME);
        eventToSave.setDuration(EVENT_DURATION);
        eventToSave.setCategory(EVENT_CATEGORY);
        eventToSave.setContent(EVENT_CONTENT);

        eventService.createEvent(eventToSave);

        verify(eventRepository).save(eventArgumentCaptor.capture());

        Event capturedEvent = eventArgumentCaptor.getValue();

        assertEquals(EVENT_NAME, capturedEvent.getName());
        assertEquals(EVENT_DURATION, capturedEvent.getDuration());
        assertEquals(EVENT_CATEGORY, capturedEvent.getCategory());
        assertEquals(EVENT_CONTENT, capturedEvent.getContent());

    }

    @Test
    void should_NotCreateEvent_When_NameIsBlank() {

        eventToSave.setName(EVENT_INVALID_NAME);
        eventToSave.setDuration(EVENT_DURATION);
        eventToSave.setCategory(EVENT_CATEGORY);
        eventToSave.setContent(EVENT_CONTENT);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
            () -> eventService.createEvent(
                eventToSave));
        Assertions.assertEquals("Name of event can not be empty ", exception.getMessage());

    }

    @Test
    void should_NotCreateEvent_When_DurationUnder10OrLongerThan360Minutes() {

        eventToSave.setName(EVENT_NAME);
        eventToSave.setDuration(EVENT_INVALID_DURATION_LOWER);
        eventToSave.setCategory(EVENT_CATEGORY);
        eventToSave.setContent(EVENT_CONTENT);

        //testing if duration can be lower than 10
        ValidationException exception = Assertions.assertThrows(ValidationException.class,
            () -> eventService.createEvent(eventToSave));
        Assertions.assertEquals(
            "Duration has to be at least 10 minutes and less than 6 hours (360 minutes)",
            exception.getMessage());

        //testing if duration can be higher than 360

        eventToSave.setDuration(EVENT_INVALID_DURATION_UPPER);
        exception = Assertions.assertThrows(ValidationException.class,
            () -> eventService.createEvent(eventToSave));
        Assertions.assertEquals(
            "Duration has to be at least 10 minutes and less than 6 hours (360 minutes)",
            exception.getMessage());


    }

    @Test
    void should_NotCreateEvent_When_NameTooLong() {

        eventToSave.setName(EVENT_INVALID_NAME_LENGTH);
        eventToSave.setDuration(EVENT_DURATION);
        eventToSave.setContent(EVENT_CONTENT);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
            () -> eventService.createEvent(eventToSave));
        Assertions.assertEquals("Name of event is too long ",
            exception.getMessage());
    }
}
