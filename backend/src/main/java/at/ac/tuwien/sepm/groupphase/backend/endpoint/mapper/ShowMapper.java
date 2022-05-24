package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper(uses = EventServiceImpl.class)
public interface ShowMapper {

    ShowDto showToShowDto(Show show);

    Show showDtoToShow(ShowDto showDto);

    Show showWithoutIdDtoToShow(ShowWithoutIdDto showWithoutIdDto);

    default Integer map(Event value) {
        return Math.toIntExact(value.getEventId());
    }

    default List<Integer> map(Set<Artist> value) {
        return value.stream().map(Artist::getArtistId).map(Math::toIntExact).toList();
    }

    default Set<Artist> map(List<Integer> value) {
        for (int i = 0; i < value.size(); i++) {
            for (int j = i + 1; j < value.size(); j++) {
                if (j != i && Objects.equals(value.get(i), value.get(j))) {
                    throw new ValidationException("shows must not have duplicate artists");
                }
            }
        }
        return Set.of(value.stream().map(id -> {
            Artist artist = new Artist();
            artist.setArtistId(Long.valueOf(id));
            return artist;
        }).toArray(Artist[]::new));
    }
}
