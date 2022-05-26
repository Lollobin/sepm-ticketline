package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEATINGPLAN_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_INVALID_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorPriceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ShowServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ShowValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    @Mock
    private SectorPriceRepository sectorPriceRepository;
    @Mock
    private ArtistRepository artistRepository;

    private final ShowValidator showValidator = new ShowValidator();
    private ShowService showService;
    private final Event fakePersistedEvent = new Event();
    private final Show fakePersistedShow = new Show();
    private final EventValidator eventValidator = new EventValidator();
    private EventService eventService;

    @Spy
    private EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, eventValidator, eventMapper);
        showService = new ShowServiceImpl(showRepository, showValidator, sectorRepository,
            seatRepository, ticketRepository, seatingPlanRepository, sectorPriceRepository,
            artistRepository);
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
        Set<Artist> artists = new HashSet<>();
        Artist artist1 = new Artist();
        artist1.setArtistId(1L);
        Artist artist2 = new Artist();
        artist2.setArtistId(2L);
        artists.add(artist1);
        artists.add(artist2);

        fakePersistedShow.setArtists(artists);
        fakePersistedShow.setShowId(1L);
        fakePersistedShow.setEvent(fakePersistedEvent);

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        showToSave.setDate(SHOW_DATE);
        showToSave.setArtists(artists);

        when(eventRepository.save(fakePersistedEvent)).thenReturn(fakePersistedEvent);

        when(artistRepository.existsById(1L)).thenReturn(true);
        when(artistRepository.existsById(2L)).thenReturn(true);

        eventService.createEvent(fakePersistedEvent);

        verify(eventRepository).save(eventArgumentCaptor.capture());

        when(showRepository.save(showToSave)).thenReturn(fakePersistedShow);

        SeatingPlan fakePersistedSeatingPlan = new SeatingPlan();
        fakePersistedSeatingPlan.setName(SEATINGPLAN_NAME);
        fakePersistedSeatingPlan.setSeatingPlanId(1L);
        when(seatingPlanRepository.findById(any())).thenReturn(
            Optional.of(fakePersistedSeatingPlan));

        Optional<List<Sector>> sectors = Optional.of(new ArrayList<>());
        List<SectorPriceDto> sectorPriceDtos = new ArrayList<>();
        List<Seat> seats = new ArrayList<>();

        Sector sector = new Sector();
        sector.setSectorId(1L);
        sectors.get().add(sector);

        SectorPriceDto sectorPriceDto = new SectorPriceDto();
        sectorPriceDto.setSectorId(1L);
        sectorPriceDto.setPrice(1F);
        sectorPriceDtos.add(sectorPriceDto);

        for (int i = 0; i < 3; i++) {
            Seat seat = new Seat();
            seat.setSeatId((long) (i + 1));
            seats.add(seat);
        }

        when(sectorRepository.findAllBySeatingPlan(any())).thenReturn(sectors);
        when(sectorPriceRepository.save(any())).thenReturn(null);
        when(seatRepository.findBySector(any())).thenReturn(Optional.of(seats));
        when(ticketRepository.save(any())).thenReturn(null);

        showService.createShow(showToSave, 1L, sectorPriceDtos);
        verify(showRepository).save(showArgumentCaptor.capture());
        verify(seatingPlanRepository, times(1)).findById(any());
        verify(sectorRepository, times(1)).findAllBySeatingPlan(any());
        verify(sectorPriceRepository, times(1)).save(any());
        verify(seatRepository, times(1)).findBySector(any());
        verify(ticketRepository, times(3)).save(any());

        assertThat(showArgumentCaptor.getValue().getDate()).isEqualTo(SHOW_DATE);
        assertFalse(showArgumentCaptor.getValue().getArtists().isEmpty());
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

        assertThrows(ValidationException.class, () -> showService.createShow(showToSave, 1L, null));

    }

    @Test
    void whenSeatingPlanDoesNotExist_shouldThrowNotFoundException() {

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        showToSave.setArtists(null);
        showToSave.setDate(SHOW_DATE);

        when(seatingPlanRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
            () -> showService.createShow(showToSave, 1L, null));

    }

    @Test
    void whenSectorDoesNotExist_shouldThrowNotFoundException() {

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        showToSave.setArtists(null);
        showToSave.setDate(SHOW_DATE);

        when(seatingPlanRepository.findById(any())).thenReturn(Optional.of(new SeatingPlan()));
        when(sectorRepository.findAllBySeatingPlan(any())).thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class,
            () -> showService.createShow(showToSave, 1L, null));

    }

    @Test
    void whenArtistDoesNotExist_shouldThrowNotFoundException() {

        Show showToSave = new Show();

        Event showsEvent = new Event();
        showsEvent.setDuration(EVENT_DURATION);
        showsEvent.setCategory(EVENT_CATEGORY);
        showsEvent.setContent(EVENT_CONTENT);
        showsEvent.setName(EVENT_NAME);

        showToSave.setEvent(showsEvent);
        Set<Artist> artists = new HashSet<>();
        Artist artist = new Artist();
        artist.setArtistId(3L);
        artists.add(artist);

        showToSave.setArtists(artists);
        showToSave.setDate(SHOW_DATE);

        when(seatingPlanRepository.findById(any())).thenReturn(Optional.of(new SeatingPlan()));
        when(sectorRepository.findAllBySeatingPlan(any())).thenReturn(Optional.of(new ArrayList<>()));
        when(artistRepository.existsById(3L)).thenReturn(false);

        assertThrows(NotFoundException.class,
            () -> showService.createShow(showToSave, 1L, Collections.EMPTY_LIST));

    }
}
