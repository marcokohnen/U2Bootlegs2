package be.qnh.bootlegs.repository;

import be.qnh.bootlegs.domain.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;

public interface TrackRepository extends JpaRepository<Track, Long> {

    Iterable<Track> findByTitleLikeIgnoreCase(@NotNull String title);
}
