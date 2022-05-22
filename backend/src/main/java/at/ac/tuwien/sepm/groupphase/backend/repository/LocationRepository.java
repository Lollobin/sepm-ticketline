package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Searches for locations that contain the params. The search is case-insensitive and looks if
     * search params start with values stored in the database.
     *
     * @param name     searches for location with that name
     * @param street   searches for location with in that matches the street
     * @param city     searches for locations that match the city
     * @param zipCode  searches for locations that match the zip-code
     * @param country  searches for location  that match the country
     * @param pageable contains information about the page
     * @return a page of locations
     */
    @Query("""
        select l from Location l
        where
        (:city is null and upper(l.name) like upper(concat(:name, '%'))) or
        (:name is null and upper(l.address.city) like upper(concat(:city, '%'))) or
        (upper(concat(l.name, ' ', l.address.city)) like upper(concat(:name, '%', ' ', :city, '%'))) or
        (upper(l.address.street) like upper(concat(:street, '%'))) or
        (upper(l.address.zipCode) like upper(:zipCode)) or
        (:city is null and upper(l.address.country) like upper(concat(:country, '%'))) or
        upper(concat(l.address.country, ' ', l.address.city)) like upper(concat(:country, ' ', :city, '%'))""")
    Page<Location> search(@Param("name") String name, @Param("street") String street,
        @Param("city") String city, @Param("zipCode") String zipCode,
        @Param("country") String country, Pageable pageable);


}
