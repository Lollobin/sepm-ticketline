package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_INVALID_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ShowServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ShowValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ShowRepository showRepository;
    @Mock
    private SectorRepository sectorRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private SeatingPlanRepository seatingPlanRepository;

    private final ShowValidator showValidator = new ShowValidator();
    private ShowService showService;
    private final Event fakePersistedEvent = new Event();
    private final Show fakePersistedShow = new Show();
    private final EventValidator eventValidator = new EventValidator();
    private EventService eventService;

    @Spy
    private ShowMapper showMapper = Mappers.getMapper(ShowMapper.class);


    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, eventValidator);
        showService = new ShowServiceImpl(showRepository, showValidator, sectorRepository,
            seatRepository, ticketRepository, seatingPlanRepository);
        showRepository.deleteAll();
    }

    @Test
    void should_CreateNewShow_When_ShowIsValid() {

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(
            Event.class);

        ArgumentCaptor<Show> showArgumentCaptor = ArgumentCaptor.forClass(
            Show.class);

        fakePersistedEvent.setEventId(1L);
        fakePersistedEvent.setCategory(EVENT_CATEGORY);
        fakePersistedEvent.setContent(EVENT_CONTENT);
        fakePersistedEvent.setName(EVENT_NAME);
        fakePersistedEvent.setDuration(EVENT_DURATION);

        fakePersistedShow.setDate(SHOW_DATE);
        fakePersistedShow.setArtists(null);
        fakePersistedShow.setShowId(1L);
        fakePersistedShow.setEvent(fakePersistedEvent);

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        showToSave.setArtists(null);
        showToSave.setDate(SHOW_DATE);

        when(eventRepository.save(fakePersistedEvent)).thenReturn(fakePersistedEvent);

        eventService.createEvent(fakePersistedEvent);

        verify(eventRepository).save(eventArgumentCaptor.capture());

        when(showRepository.save(showToSave)).thenReturn(fakePersistedShow);

        showService.createShow(showToSave, 1L);
        verify(showRepository).save(showArgumentCaptor.capture());

        assertThat(showArgumentCaptor.getValue().getDate()).isEqualTo(SHOW_DATE);
        assertThat(showArgumentCaptor.getValue().getArtists()).isNull();
        assertThat(showArgumentCaptor.getValue().getEvent().getName()).isEqualTo(EVENT_NAME);
    }

    @Test
    void should_ThrowValidationException_When_DateInPast() {

        fakePersistedEvent.setEventId(1L);
        fakePersistedEvent.setCategory(EVENT_CATEGORY);
        fakePersistedEvent.setContent(EVENT_CONTENT);
        fakePersistedEvent.setName(EVENT_NAME);
        fakePersistedEvent.setDuration(EVENT_DURATION);

        fakePersistedShow.setDate(SHOW_DATE);
        fakePersistedShow.setArtists(null);
        fakePersistedShow.setShowId(1L);
        fakePersistedShow.setEvent(fakePersistedEvent);

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        showToSave.setArtists(null);
        showToSave.setDate(SHOW_INVALID_DATE);

        when(eventRepository.save(fakePersistedEvent)).thenReturn(fakePersistedEvent);

        eventService.createEvent(fakePersistedEvent);

        assertThrows(ValidationException.class, () -> showService.createShow(showToSave, 1L));

    }

}
