package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserEncodePasswordMapper {

    private final GenderMapper genderMapper;
    private final PasswordEncoder passwordEncoder;

    private final AddressMapper addressMapper;

    public UserEncodePasswordMapper(GenderMapper genderMapper, PasswordEncoder passwordEncoder, AddressMapper addressMapper) {
        this.genderMapper = genderMapper;
        this.passwordEncoder = passwordEncoder;
        this.addressMapper = addressMapper;
    }

    public ApplicationUser userWithPasswordDtoToAppUser(UserWithPasswordDto userWithPasswordDto) {
        if (userWithPasswordDto == null) return null;
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setPassword(passwordEncoder.encode(userWithPasswordDto.getPassword()));
        applicationUser.setGender(genderMapper.genderDtoToGender(userWithPasswordDto.getGender()));
        applicationUser.setFirstName(userWithPasswordDto.getFirstName());
        applicationUser.setLastName(userWithPasswordDto.getLastName());
        applicationUser.setEmail(userWithPasswordDto.getEmail());
        applicationUser.setAddress(addressMapper.addressDtoToAddress(userWithPasswordDto.getAddress()));
        return applicationUser;
    }
}
