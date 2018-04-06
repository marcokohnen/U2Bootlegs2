package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    Concert findByDateEquals(LocalDate date);

    Iterable<Concert> findByTitleLike(String title);

    Iterable<Concert> findByCountryLike(String country);

    Iterable<Concert> findByCityLike(String city);

    Iterable<Concert> findByQuality(RecordingQuality quality);
}
