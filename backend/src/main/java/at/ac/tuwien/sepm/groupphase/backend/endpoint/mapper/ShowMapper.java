package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper.DateMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.helpmapper.ShowHelpMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.EventServiceImpl;
import org.mapstruct.Mapper;

@Mapper(uses = { DateMapper.class, ShowHelpMapper.class, EventServiceImpl.class })
public interface ShowMapper {

    Show showDtoToShow(ShowDto showDto);

    ShowDto showToShowDto(Show show);

    Show showWithoutIdDtoToShow(ShowWithoutIdDto showWithoutIdDto);

}
