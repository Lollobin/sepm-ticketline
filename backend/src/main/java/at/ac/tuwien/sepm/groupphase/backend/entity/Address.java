package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(nullable = false, length = 40)
    private String houseNumber;

    @Column(nullable = false, length = 100)
    private String street;

    @Column(nullable = false, length = 16)
    private String zipCode;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;

    public Address(String houseNumber, String street, String zipCode, String city,
        String country) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

    public Address() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String number) {
        this.houseNumber = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "" + "addressId=" + addressId
            + ", number='" + houseNumber + '\''
            + ", street='" + street + '\''
            + ", zipCode='" + zipCode + '\''
            + ", city='" + city + '\''
            + ", country='" + country + '\''
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Address address = (Address) o;
        return Objects.equals(addressId, address.addressId)
            && Objects.equals(houseNumber, address.houseNumber)
            && Objects.equals(street, address.street)
            && Objects.equals(zipCode, address.zipCode)
            && Objects.equals(city, address.city)
            && Objects.equals(country, address.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, houseNumber, street, zipCode, city, country);
    }
}
