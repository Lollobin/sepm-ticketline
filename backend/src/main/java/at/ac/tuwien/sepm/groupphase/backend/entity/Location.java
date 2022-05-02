package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.*;

@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long locationId;

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "addressId",
        referencedColumnName = "addressId",
        nullable = false
    )
    private Address address;

    public long getId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public void setId(long locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
            "locationId=" + locationId +
            ", name='" + name + '\'' +
            address +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId && name.equals(location.name) && address.equals(location.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, name, address);
    }
}
