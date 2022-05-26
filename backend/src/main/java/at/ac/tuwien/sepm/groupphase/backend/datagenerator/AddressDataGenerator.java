package at.ac.tuwien.sepm.groupphase.backend.datagenerator;


import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class AddressDataGenerator {

    private final Faker faker = new Faker();

    public Address generateRandomAddress() {
        Address address = new at.ac.tuwien.sepm.groupphase.backend.entity.Address();
        address.setHouseNumber(faker.address().buildingNumber());
        address.setStreet(faker.address().streetName());
        address.setCity(faker.address().city());
        address.setCountry(faker.address().country());
        address.setZipCode(faker.address().zipCode());
        return address;
    }
}
