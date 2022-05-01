package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserEncodePasswordMapper {

    private final GenderMapper genderMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEncodePasswordMapper(GenderMapper genderMapper, PasswordEncoder passwordEncoder) {
        this.genderMapper = genderMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ApplicationUser userWithPasswordDtoToAppUser(UserWithPasswordDto userWithPasswordDto) {
        if (userWithPasswordDto == null) return null;
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setPassword(passwordEncoder.encode(userWithPasswordDto.getPassword()));
        applicationUser.setGender(genderMapper.genderDtoToGender(userWithPasswordDto.getGender()));
        applicationUser.setFirstName(userWithPasswordDto.getFirstName());
        applicationUser.setLastName(userWithPasswordDto.getLastName());
        applicationUser.setEmail(userWithPasswordDto.getEmail());
        applicationUser.setStreet(userWithPasswordDto.getStreet());
        applicationUser.setCountry(userWithPasswordDto.getCountry());
        applicationUser.setCity(userWithPasswordDto.getCity());
        applicationUser.setZipCode(userWithPasswordDto.getZipCode());
        applicationUser.setCountry(userWithPasswordDto.getCountry());
        return applicationUser;
    }
}
