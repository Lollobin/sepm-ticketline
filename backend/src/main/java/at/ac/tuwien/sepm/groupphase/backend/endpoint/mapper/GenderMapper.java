package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenderMapper {

    GenderMapper INSTANCE = Mappers.getMapper(GenderMapper.class);

    @ValueMappings({
        @ValueMapping(target = "MALE", source = "MALE"),
        @ValueMapping(target = "FEMALE", source = "FEMALE"),
        @ValueMapping(target = "OTHER", source = "OTHER")
    })
    Gender genderDtoToGender(GenderDto genderDto);

    GenderDto genderToGenderDto(Gender gender);
}
