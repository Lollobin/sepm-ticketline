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

    @Query("""
        select a from Artist a
        where upper(concat(a.firstName, ' ', a.lastName, ' ', coalesce(concat(a.bandName, ' '), ''), a.knownAs)) like upper(concat('%', :search, '%'))
        or upper(concat(coalesce(concat(a.bandName, ' '), ''), a.firstName, ' ', a.lastName, ' ', a.knownAs)) like upper(concat('%', :search, '%'))
        """)
    Page<Artist> search(@Param(value = "search") String search, Pageable pageable);


}
