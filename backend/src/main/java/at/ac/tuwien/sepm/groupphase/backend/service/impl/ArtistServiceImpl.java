package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArtistServiceImpl implements ArtistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistServiceImpl(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
    }

    @Override
    public Artist findOne(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        Hibernate.initialize(artist);
        if (artist.isPresent()) {
            return artist.get();
        } else {
            throw new NotFoundException(String.format("Could not find event with id %s", id));
        }
    }

    @Override
    public ArtistsSearchResultDto search(String search, Pageable pageable) {
        LOGGER.debug("Getting artists which contain search term: {}", search);
        Page<Artist> artistPage = artistRepository.search(search, pageable);

        return setArtistResultDto(artistPage);
    }

    @Override
    public ArtistsSearchResultDto findAll(Pageable pageable) {
        LOGGER.debug("getting all artists with pageable: {}", pageable);

        Page<Artist> artistPage = artistRepository.findAll(pageable);

        return setArtistResultDto(artistPage);
    }

    private ArtistsSearchResultDto setArtistResultDto(Page<Artist> artistPage) {
        LOGGER.trace("Setting ArtistResultDto values");
        ArtistsSearchResultDto searchResultDto = new ArtistsSearchResultDto();

        searchResultDto.setArtists(
            artistPage.getContent().stream().map(artistMapper::artistToArtistDto).toList());
        searchResultDto.setNumberOfResults((int) artistPage.getTotalElements());
        searchResultDto.setCurrentPage(artistPage.getNumber());
        searchResultDto.setPagesTotal(artistPage.getTotalPages());

        return searchResultDto;
    }
}
