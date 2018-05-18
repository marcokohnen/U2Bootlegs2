package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    Iterable<Concert> findByDateEquals(LocalDate date);

    Iterable<Concert> findByTitleLikeIgnoreCase(String title);

    Iterable<Concert> findByCountryLikeIgnoreCase(String country);

    Iterable<Concert> findByCityLikeIgnoreCase(String city);

    Iterable<Concert> findByQuality(RecordingQuality quality);

    @Query(value = "SELECT tour_id from concerts where id =?1", nativeQuery = true)
    Long findTourIdByConcertId(Long concertId);
}
