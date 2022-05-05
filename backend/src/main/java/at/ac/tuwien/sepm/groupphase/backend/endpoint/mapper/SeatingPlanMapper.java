package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import org.mapstruct.Mapper;

@Mapper
public interface SeatingPlanMapper {
    SeatingPlanDto showToShowDto(SeatingPlan show);

    SeatingPlan showDtoToShow(SeatingPlanDto showDto);
}
