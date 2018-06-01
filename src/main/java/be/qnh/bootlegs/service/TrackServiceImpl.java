package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.TrackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Transactional
public class TrackServiceImpl implements TrackService {

    private final Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);

    private final TrackRepository trackRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Override
    public Track addOne(Track track) {
        Track newTrack = trackRepository.save(track);
        logger.info("addedTrack = [{}]", newTrack);
        return newTrack;
    }

    @Override
    public Iterable<Track> findAll() {
        return trackRepository.findAll();
    }

    @Override
    public Track findOneById(Long id) {
        Optional<Track> foundTrack = trackRepository.findById(id);
        if (foundTrack.isPresent()) {
            Track result = foundTrack.get();
            logger.info("foundTrack = [{}]", foundTrack.get());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public Track updateOneById(Long id, Track track) {
        Optional<Track> foundTrack = trackRepository.findById(id);
        if (foundTrack.isPresent()) {
            Track trackToUpdate = foundTrack.get();
            trackToUpdate.setSequenceNr(track.getSequenceNr());
            trackToUpdate.setTitle(track.getTitle());
            trackToUpdate.setLocationUrl(track.getLocationUrl());
            Track result = trackRepository.save(track);
            logger.info("updatedTrack = [{}]", result);
            return result;
        }
        return null;
    }

    @Override
    public Track deleteOneById(Long id) {
        Optional<Track> foundTrack = trackRepository.findById(id);
        if (foundTrack.isPresent()) {
            trackRepository.deleteById(id);
            logger.info("deletedTrack = [{}]", foundTrack.get());
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