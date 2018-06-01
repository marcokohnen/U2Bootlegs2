package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.ConcertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ConcertServiceImpl implements ConcertService {

    private final Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);

    private final ConcertRepository concertRepository;

    @Autowired
    public ConcertServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    // crud methods
    @Override
    public Concert addOne(Concert concert) {
        Concert newConcert = concertRepository.save(concert);
        logger.info("addedConcert = [{}]", newConcert);
        return newConcert;
    }

    @Override
    public Iterable<Concert> findAll() {
        return concertRepository.findAll();
    }

    @Override
    public Concert findOneById(Long id) {
        Optional<Concert> foundConcert = concertRepository.findById(id);
        if (foundConcert.isPresent()) {
            Concert result = foundConcert.get();
            logger.info("foundConcert = [{}]", foundConcert.get());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public Concert udpdateOneById(Long id, Concert concert) {
        Optional<Concert> foundConcert = concertRepository.findById(id);
        if (foundConcert.isPresent()) {
            Concert concertToUpdate = foundConcert.get();
            concertToUpdate.setCountry(concert.getCountry());
            concertToUpdate.setCity(concert.getCity());
            concertToUpdate.setDate(concert.getDate());
            concertToUpdate.setTitle(concert.getTitle());
            concertToUpdate.setQuality(concert.getQuality());
            concertToUpdate.setVenue(concert.getVenue());
            Concert result = concertRepository.save(concertToUpdate);
            logger.info("updatedConcert = [{}]", result);
            return result;
        }
        return null;
    }

    @Override
    public Concert deleteOneById(Long id) {
        Optional<Concert> foundConcert = concertRepository.findById(id);
        if (foundConcert.isPresent()) {
            concertRepository.deleteById(id);
            logger.info("deletedConcert = [{}]", foundConcert);
            return foundConcert.get();
        } else {
            return null;
        }
    }

    // end crud methods ///////////////////////////////////////////////////////////////

    @Override
    public Iterable<Concert> findByDateEquals(LocalDate date) {
        return concertRepository.findByDateEquals(date);
    }

    @Override
    public Iterable<Concert> findByTitleLikeIgnoreCase(String title) {
        title = "%" + title + "%";
        return concertRepository.findByTitleLikeIgnoreCase(title);
    }

    @Override
    public Iterable<Concert> findByCountryLikeIgnoreCase(String country) {
        country = "%" + country + "%";
        return concertRepository.findByCountryLikeIgnoreCase(country);
    }

    @Override
    public Iterable<Concert> findByCityLikeIgnoreCase(String city) {
        city = "%" + city + "%";
        return concertRepository.findByCityLikeIgnoreCase(city);
    }

    @Override
    public Iterable<Concert> findByRecordingQuality(RecordingQuality quality) {
        return concertRepository.findByQuality(quality);
    }

    @Override
    public Long findTourIdByConcertId(Long concertId) {
        return concertRepository.findTourIdByConcertId(concertId);
    }

    @Override
    public Track addTrackToConcert(Long concert_id, Track newTrack) {
        Concert aConcert = findOneById(concert_id);
        if (aConcert != null) {
            if (aConcert.getTrackList().add(newTrack)) {
                logger.info("New Track = [{}]", newTrack + " added to Concert = [{}]", aConcert);
                return newTrack;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
