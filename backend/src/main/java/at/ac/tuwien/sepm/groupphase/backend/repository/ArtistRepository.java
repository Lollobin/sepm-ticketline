package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    List<Artist> findByBandNameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrKnownAsContainingIgnoreCaseAllIgnoreCase(
        String bandName, String firstName, String lastName, String knownAs, Pageable pageable);

    @Query("""
        select a from Artist a
        where upper(a.bandName) like upper(concat('%', :search, '%')) or upper(a.knownAs) like upper(concat('%', :search, '%')) or upper(a.firstName) like
        upper(concat('%', :search, '%')) or upper(a.lastName) like upper(concat('%', :search, '%'))""")
    Page<Artist> search(
        @Param(value = "search") String search, Pageable pageable);


}
