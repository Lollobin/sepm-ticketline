package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface ShowMapper {

    ShowDto showToShowDto(Show show);

    Show showDtoToShow(ShowDto showDto);

    default Integer map(Event value) {
        return Math.toIntExact(value.getEventId());
    }

    default Event map(Integer value) {
        Event eventStub = new Event();
        eventStub.setEventId(value);
        return eventStub;
    }

    default List<Integer> map(Set<Artist> value) {
        return value.stream().map(Artist::getArtistId).map(Math::toIntExact).toList();
    }

    default Set<Artist> map(List<Integer> value) {
        return Set.of(value.stream().map(id -> {
            Artist artist = new Artist();
            artist.setArtistId(id);
            return artist;
        }).toArray(Artist[]::new));
    }
}
