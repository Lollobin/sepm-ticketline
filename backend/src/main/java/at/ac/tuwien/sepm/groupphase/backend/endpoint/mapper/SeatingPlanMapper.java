package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SeatingPlanMapper {
    @Mapping(target = "locationId", source = "location")
    @Mapping(target = "seatingPlanLayoutId", source = "seatingPlanLayout")
    SeatingPlanDto seatingPlanToSeatingPlanDto(SeatingPlan seatingPlan);

    SeatingPlan seatingPlanDtoToSeatingPlan(SeatingPlanDto seatingPlanDto);

    default Long map(Location location) {
        return location.getId();
    }

    default Long map(SeatingPlanLayout seatingPlanLayout) {
        return seatingPlanLayout.getSeatingPlanLayoutId();
    }
}
