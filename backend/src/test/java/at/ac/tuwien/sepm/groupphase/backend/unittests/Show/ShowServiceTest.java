package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEATINGPLAN_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_INVALID_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorPriceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
class ShowServiceTest {

    private final ShowValidator showValidator = new ShowValidator();
    private final Event fakePersistedEvent = new Event();
    private final Show fakePersistedShow = new Show();
    private final EventValidator eventValidator = new EventValidator();
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
    private ShowService showService;
    private EventService eventService;

    Pageable pageable = PageRequest.of(0, 10, Direction.ASC, "id");

    @Spy
    private ShowMapper showMapper = Mappers.getMapper(ShowMapper.class);


    @BeforeEach
    void setUp() {
        eventService = new EventServiceImpl(eventRepository, eventValidator);
        showService = new ShowServiceImpl(showRepository, showValidator, sectorRepository,
            seatRepository, ticketRepository, seatingPlanRepository, sectorPriceRepository,
            showMapper);
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
        assertThat(showArgumentCaptor.getValue().getArtists()).isNull();
        assertThat(showArgumentCaptor.getValue().getEvent().getName()).isEqualTo(EVENT_NAME);
    }

//    @Test
//    void searchWithOnlyEventId_shouldCallFindEventByIdInRepo(){
//
//        ShowSearchDto searchDto = new ShowSearchDto();
//        searchDto.setEventId(1L);

//    List<Show> list = new ArrayList<>();

//    when(showRepository.findEventById(1L).thenReturn(new PageImpl<>(list));
//
//        showService.search(searchDto);
//
//        verify(showRepository).findShowById(1L);
//
//    }
//
//    @Test
//    void searchWithOnlyLocationId_shouldCallFindLocationByIdInRepo(){
//
//        ShowSearchDto searchDto = new ShowSearchDto();
//        searchDto.setLocationId(1L);

//    List<Show> list = new ArrayList<>();



//    when(showRepository.findLocationById(1L).thenReturn(new PageImpl<>(list));

//
//        showService.search(searchDto);
//
//        verify(showRepository).findLocationById(1L);
//
//    }

    @Test
    void searchWithAllButLocationIdAndEventId_shouldCallSearchInRepo(){

        ShowSearchDto searchDto = new ShowSearchDto();
        searchDto.setEvent("test");
        searchDto.setDate(SHOW_DATE);
        searchDto.setPrice(BigDecimal.valueOf(30));
        searchDto.setSeatingPlan(1L);

        List<Show> list = new ArrayList<>();



        when(showRepository.search(SHOW_DATE, SHOW_DATE.getHour(), SHOW_DATE.getMinute(), "test", BigDecimal.valueOf(30), 1L, pageable)).thenReturn(new PageImpl<>(list));

        showService.search(searchDto, pageable);

        verify(showRepository).search(SHOW_DATE, SHOW_DATE.getHour(), SHOW_DATE.getMinute(), "test", BigDecimal.valueOf(30), 1L, pageable);

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

        assertThrows(NotFoundException.class,
            () -> showService.createShow(showToSave, 1L, null));

    }
}
