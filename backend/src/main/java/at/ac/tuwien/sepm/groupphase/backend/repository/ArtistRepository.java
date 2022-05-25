package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    /**
     * Case insensitive search for a page of artists. It is checked whether the artist fields contain
     * the search string in various ways.
     *
     * @param search String to be searched for
     * @param pageable contains information about the page we request
     * @return Page of found artists
     */
    @Query("""
        select a from Artist a
        where upper(concat(coalesce(concat(a.firstName, ' '), ''), coalesce(concat(a.lastName, ' '), ''),
            coalesce(concat(a.bandName, ' '), ''), coalesce(concat(a.knownAs, ' '), ''))) like upper(concat('%', :search, '%'))
        or upper(concat(coalesce(concat(a.firstName, ' '), ''), coalesce(concat(a.lastName, ' '), ''),
            coalesce(concat(a.knownAs, ' '), ''), coalesce(concat(a.bandName, ' '), ''))) like upper(concat('%', :search, '%'))
        or upper(concat(coalesce(concat(a.bandName, ' '), ''), coalesce(concat(a.knownAs, ' '), ''),
            coalesce(concat(a.firstName, ' '), ''), coalesce(concat(a.lastName, ' '), ''))) like upper(concat('%', :search, '%'))
        or upper(concat(coalesce(concat(a.knownAs, ' '), ''), coalesce(concat(a.bandName, ' '), ''),
            coalesce(concat(a.firstName, ' '), ''), coalesce(concat(a.lastName, ' '), ''))) like upper(concat('%', :search, '%'))
        or upper(concat( coalesce(concat(a.bandName, ' '), ''), coalesce(concat(a.firstName, ' '), ''),
            coalesce(concat(a.lastName, ' '), ''), coalesce(concat(a.knownAs, ' '), ''))) like upper(concat('%', :search, '%'))
        or upper(concat( coalesce(concat(a.knownAs, ' '), ''), coalesce(concat(a.firstName, ' '), ''),
            coalesce(concat(a.lastName, ' '), ''), coalesce(concat(a.bandName, ' '), ''))) like upper(concat('%', :search, '%'))
        """)
    Page<Artist> search(@Param(value = "search") String search, Pageable pageable);


}
