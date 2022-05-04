package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import org.mapstruct.Mapper;

@Mapper
public interface ShowMapper {
    ShowDto showToShowDto(Show show);

    Show showDtoToShow(ShowDto showDto);
}
