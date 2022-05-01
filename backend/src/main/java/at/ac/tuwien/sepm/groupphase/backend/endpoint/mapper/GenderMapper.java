package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.springframework.stereotype.Component;

@Component
public class GenderMapper {
    public Gender genderDtoToGender(GenderDto genderDto) {
        if (genderDto == null) return null;
        if (genderDto.getValue().equals("male")) return Gender.MALE;
        else if (genderDto.getValue().equals("female")) return Gender.FEMALE;
        else return Gender.OTHER;
    }

    public GenderDto genderToGenderDto(Gender gender) {
        if (gender == null) return null;
        if (gender.equals(Gender.MALE)) return GenderDto.MALE;
        else if (gender.equals(Gender.FEMALE)) return GenderDto.FEMALE;
        else return GenderDto.OTHER;
    }

}
