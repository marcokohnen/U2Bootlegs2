package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Transactional
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track addOne(Track track) {
        return track == null ? null : trackRepository.save(track);
    }

    @Override
    public Iterable<Track> findAll() {
        return trackRepository.findAll();
    }

    @Override
    public Track findOneById(Long id) {
        return trackRepository.findById(id).orElse(null);
    }

    @Override
    public Track updateOneById(Long id, Track track) {
        Optional<Track> foundTrack = trackRepository.findById(id);
        if (foundTrack.isPresent()) {
            Track trackToUpdate = foundTrack.get();
            trackToUpdate.setSequenceNr(track.getSequenceNr());
            trackToUpdate.setTitle(track.getTitle());
            trackToUpdate.setLocationUrl(track.getLocationUrl());
            return trackRepository.save(trackToUpdate);
        }
        return null;
    }

    @Override
    public Track deleteOneById(Long id) {
        Optional<Track> foundTrack = trackRepository.findById(id);
        if (foundTrack.isPresent()) {
            trackRepository.deleteById(id);
            return foundTrack.get();
        } else {
            return null;
        }
    }

    @Override
    public Iterable<Track> findByTitleLikeIgnoreCase(@NotNull String title) {
        title = "%" + title + "%";
        return trackRepository.findByTitleLikeIgnoreCase(title);
    }

}
