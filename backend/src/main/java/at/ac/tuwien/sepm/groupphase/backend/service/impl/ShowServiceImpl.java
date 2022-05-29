package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorPriceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ShowValidator;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowRepository showRepository;
    private final ShowValidator showValidator;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final SeatingPlanRepository seatingPlanRepository;
    private final SectorPriceRepository sectorPriceRepository;
    private final ArtistRepository artistRepository;

    private final ShowMapper showMapper;

    @Autowired
    public ShowServiceImpl(ShowRepository showRepository, ShowValidator showValidator,
        SectorRepository sectorRepository, SeatRepository seatRepository,
        TicketRepository ticketRepository, SeatingPlanRepository seatingPlanRepository,
        SectorPriceRepository sectorPriceRepository, ArtistRepository artistRepository,
        ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showValidator = showValidator;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.seatingPlanRepository = seatingPlanRepository;
        this.sectorPriceRepository = sectorPriceRepository;
        this.artistRepository = artistRepository;
        this.showMapper = showMapper;
    }


    @Override
    public ShowSearchResultDto findAll(Pageable pageable) {
        LOGGER.trace("Find all shows");

        Page<Show> showPage = showRepository.findAll(pageable);

        return setShowSearchResultDto(showPage);

    }

    @Override
    public ShowSearchResultDto search(ShowSearchDto showSearchDto, Pageable pageable) {

        LOGGER.trace("Find all shows with pageable: {}", pageable);

        if (showSearchDto.getDate() == null && showSearchDto.getEvent() == null
            && showSearchDto.getPrice() == null && showSearchDto.getSeatingPlan() == null
            && showSearchDto.getEventId() == null && showSearchDto.getLocation() != null) {

            Page<Show> showPage = showRepository.findShowsByLocation(showSearchDto.getLocation(),
                pageable);

            return setShowSearchResultDto(showPage);

        } else if (showSearchDto.getEventId() != null) {
            Page<Show> showPage = showRepository.findShowsByEventId(showSearchDto.getEventId(),
                pageable);

            return setShowSearchResultDto(showPage);
        } else {

            Integer hours = null;
            Integer minutes = null;
            if (showSearchDto.getDate() != null) {
                hours = showSearchDto.getDate().getHour();
                minutes = showSearchDto.getDate().getMinute();
            }

            Page<Show> showPage = showRepository.search(showSearchDto.getDate(), hours, minutes,
                showSearchDto.getEvent(), showSearchDto.getPrice(), showSearchDto.getSeatingPlan(),
                showSearchDto.getLocation(),
                pageable);

            return setShowSearchResultDto(showPage);
        }
    }

    private ShowSearchResultDto setShowSearchResultDto(Page<Show> showPage) {
        LOGGER.trace("setting ShowSearchResultDto");
        ShowSearchResultDto searchResultDto = new ShowSearchResultDto();

        searchResultDto.setShows(
            showPage.getContent().stream().map(showMapper::showToShowDto).toList());
        searchResultDto.setNumberOfResults((int) showPage.getTotalElements());
        searchResultDto.setCurrentPage(showPage.getNumber());
        searchResultDto.setPagesTotal(showPage.getTotalPages());

        return searchResultDto;
    }

    @Override
    public Show createShow(Show show, Long seatingPlanId, List<SectorPriceDto> sectorPriceDtos) {
        LOGGER.debug("Create new show {}", show);

        showValidator.checkIfShowCorrect(show);

        Optional<SeatingPlan> seatingPlan = seatingPlanRepository.findById(seatingPlanId);
        if (seatingPlan.isEmpty()) {
            throw new NotFoundException(
                String.format("Could not find seatingPlan with id %s", seatingPlanId));
        }

        Optional<List<Sector>> sectors = sectorRepository.findAllBySeatingPlan(seatingPlan.get());
        if (sectors.isEmpty()) {
            throw new NotFoundException(
                String.format("Could not find sector with the given seatingPlanId %s",
                    seatingPlanId));
        }
        if (sectors.get().size() != sectorPriceDtos.size()) {
            throw new ConflictException(
                "length of list of sectorPrices does not match with length of list of sectors");
        }

        for (Artist artist : show.getArtists()) {
            if (!artistRepository.existsById(artist.getArtistId())) {
                throw new NotFoundException(
                    String.format("Could not find artist with the given artistId %s",
                        artist.getArtistId()));
            }
        }

        List<SectorPrice> sectorPrices = setUpSectorPrices(sectorPriceDtos, sectors.get());

        show = showRepository.save(show);

        Set<SectorPrice> sectorPriceSet = new HashSet<>();

        for (SectorPrice sectorPrice : sectorPrices) {
            sectorPrice.setShow(show);
            sectorPrice = sectorPriceRepository.save(sectorPrice);
            sectorPriceSet.add(sectorPrice);

        }

        for (Sector sector : sectors.get()) {
            Optional<List<Seat>> seats = seatRepository.findBySector(sector);
            if (seats.isEmpty()) {
                throw new NotFoundException(
                    String.format("No seats found for sector with id %s", sector.getSectorId()));
            }
            for (Seat seat : seats.get()) {
                Ticket ticket = new Ticket();
                ticket.setSeat(seat);
                ticket.setShow(show);
                ticketRepository.save(ticket);
            }
        }

        show.setSectorPrices(sectorPriceSet);

        return show;
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

    private List<SectorPrice> setUpSectorPrices(List<SectorPriceDto> sectorPriceDtos,
        List<Sector> sectors) {
        List<SectorPrice> sectorPrices = new ArrayList<>();
        for (Sector sector : sectors) {
            boolean found = false;
            for (SectorPriceDto sectorPriceDto : sectorPriceDtos) {
                if (sectorPriceDto.getSectorId().equals(sector.getSectorId())) {
                    found = true;
                    sectorPrices.add(
                        new SectorPrice(sector, null,
                            BigDecimal.valueOf(sectorPriceDto.getPrice())));
                    break;
                }
            }
            if (!found) {
                throw new ConflictException(
                    String.format("No sectorPrice defined for sector with id %s",
                        sector.getSectorId()));
            }
        }
        return sectorPrices;
    }
}
