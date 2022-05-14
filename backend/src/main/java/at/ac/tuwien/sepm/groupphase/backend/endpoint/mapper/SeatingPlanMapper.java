package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import org.mapstruct.Mapper;

@Mapper
public interface SeatingPlanMapper {
    SeatingPlanDto seatingPlanToSeatingPlanDto(SeatingPlan seatingPlan);

    SeatingPlan seatingPlanDtoToSeatingPlan(SeatingPlanDto seatingPlanDto);
}
