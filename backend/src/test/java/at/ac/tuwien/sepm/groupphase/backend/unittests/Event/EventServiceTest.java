package at.ac.tuwien.sepm.groupphase.backend.unittests.Event;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_CATEGORY_LENGTH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_DURATION_LOWER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_DURATION_UPPER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_NAME_LENGTH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;


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
    Pageable pageable = PageRequest.of(0, 10, Direction.fromString("ASC"), "firstName");

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
        eventToSave.setCategory(EVENT_INVALID_CATEGORY_LENGTH);
        eventToSave.setContent(EVENT_CONTENT);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
            () -> eventService.createEvent(eventToSave));
        Assertions.assertEquals("Name of event is too long & Category contains too many characters",
            exception.getMessage());
    }

    @Test
    void findAllWithStandardPageable_shouldReturnAllEvents() {

        Event event = new Event();
        event.setName(EVENT_NAME);
        event.setContent(EVENT_CONTENT);
        event.setCategory(EVENT_CATEGORY);
        event.setDuration(EVENT_DURATION);

        Event event2 = new Event();
        event2.setName(EVENT2_NAME);
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);
        event2.setDuration(EVENT2_DURATION);

        Event event3 = new Event();
        event3.setName(EVENT3_NAME);
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);
        event3.setDuration(EVENT3_DURATION);

        List<Event> events = new ArrayList<>();
        events.add(event2);
        events.add(event3);
        events.add(event);

        Page<Event> eventPage = new PageImpl<>(events);

        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        EventSearchResultDto eventSearchResultDto = eventService.findAll(pageable);

        assertAll(
            () -> assertEquals(eventSearchResultDto.getEvents().size(), 3),
            () -> assertEquals(eventSearchResultDto.getEvents().get(0).getName(), EVENT2_NAME),
            () -> assertEquals(eventSearchResultDto.getEvents().get(0).getDuration(), BigDecimal.valueOf(EVENT2_DURATION)),
            () -> assertEquals(eventSearchResultDto.getEvents().get(0).getCategory(), EVENT2_CATEGORY),
            () -> assertEquals(eventSearchResultDto.getEvents().get(0).getContent(), EVENT2_CONTENT),
            () -> assertEquals(eventSearchResultDto.getEvents().get(1).getName(), EVENT3_NAME),
            () -> assertEquals(eventSearchResultDto.getEvents().get(2).getName(), EVENT_NAME)
        );

        events.clear();
        eventPage = new PageImpl<>(events);
        when(eventRepository.findAll(pageable)).thenReturn(eventPage);

        EventSearchResultDto empty = eventService.findAll(pageable);

        assertThat(empty.getEvents()).isEmpty();
    }

    @Test
    void searchWithStandardPageable_shouldReturnCorrectEvents() {

        Event event = new Event();
        event.setName(EVENT_NAME);
        event.setContent(EVENT_CONTENT);
        event.setCategory(EVENT_CATEGORY);
        event.setDuration(EVENT_DURATION);

        Event event2 = new Event();
        event2.setName(EVENT2_NAME);
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);
        event2.setDuration(EVENT2_DURATION);

        Event event3 = new Event();
        event3.setName(EVENT3_NAME);
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);
        event3.setDuration(EVENT3_DURATION);

        List<Event> events1 = new ArrayList<>();
        events1.add(event2);
        events1.add(event3);
        events1.add(event);

        List<Event> events2 = new ArrayList<>();
        events2.add(event);

        Page<Event> eventPage1 = new PageImpl<>(events1);
        Page<Event> eventPage2 = new PageImpl<>(events2);
        Page<Event> eventPage3 = new PageImpl<>(new ArrayList<>());

        when(eventRepository.search("a", null, null, null, null, null, pageable))
            .thenReturn(eventPage1);
        when(eventRepository.search(EVENT_NAME.substring(1, EVENT_NAME.length() - 2), null, null, null, null, null, pageable))
            .thenReturn(eventPage2);
        when(eventRepository.search("INVALID NAME", null, null, null, null, null, pageable))
            .thenReturn(eventPage3);

        EventSearchDto eventSearchDto1 = new EventSearchDto();
        eventSearchDto1.setName("a");
        EventSearchDto eventSearchDto2 = new EventSearchDto();
        eventSearchDto2.setName(EVENT_NAME.substring(1, EVENT_NAME.length() - 2));
        EventSearchDto eventSearchDto3 = new EventSearchDto();
        eventSearchDto3.setName("INVALID NAME");

        EventSearchResultDto eventSearchResultDto1 = eventService.search(eventSearchDto1, pageable);
        EventSearchResultDto eventSearchResultDto2 = eventService.search(eventSearchDto2, pageable);
        EventSearchResultDto eventSearchResultDto3 = eventService.search(eventSearchDto3, pageable);

        assertAll(
            () -> assertEquals(eventSearchResultDto1.getEvents().size(), 3),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(0).getName(), event2.getName()),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(0).getCategory(), event2.getCategory()),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(1).getName(), event3.getName()),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(1).getCategory(), event3.getCategory()),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(2).getName(), event.getName()),
            () -> assertEquals(eventSearchResultDto1.getEvents().get(2).getCategory(), event.getCategory()),

            () -> assertEquals(eventSearchResultDto2.getEvents().size(), 1),
            () -> assertEquals(eventSearchResultDto2.getEvents().get(0).getName(), event.getName()),
            () -> assertEquals(eventSearchResultDto2.getEvents().get(0).getCategory(), event.getCategory()),

            () -> assertThat(eventSearchResultDto3.getEvents()).isEmpty()
        );
    }
}
