package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    public Location() {
    }

    public Location(Long locationId) {
        this.locationId = locationId;
    }

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "addressId", referencedColumnName = "addressId", nullable = false)
    private Address address;

    public Long getId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setId(Long locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{"
            + "locationId="
            + locationId
            + ", name='"
            + name
            + '\''
            + ", "
            + address
            + '\''
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
        Location location = (Location) o;
        return Objects.equals(locationId, location.locationId)
            && Objects.equals(name, location.name)
            && Objects.equals(address, location.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, name, address);
    }
}
