package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.ConcertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepository;

    @Autowired
    public ConcertServiceImpl(ConcertRepository concertRepository) {
        this.concertRepository = concertRepository;
    }

    @PostConstruct
    private void init() {
        Concert concert1 = new Concert();
        concert1.setTitle("A late night play in Boston");
        concert1.setDate(LocalDate.of(1981, 3, 26));
        concert1.setCity("Boston");
        concert1.setCountry("USA");
        concert1.setQuality(RecordingQuality.GOOD);
        Concert concert2 = new Concert();
        concert2.setTitle("When we were young");
        concert2.setDate(LocalDate.of(1984, 10, 30));
        concert2.setCity("Rotterdam");
        concert2.setCountry("Netherlands");
        concert2.setQuality(RecordingQuality.EXCELENT);
        Concert concert3 = new Concert();
        concert3.setTitle("The First Night On Earth");
        concert3.setDate(LocalDate.of(1997, 4, 25));
        concert3.setCity("Las Vegas");
        concert3.setCountry("USA");
        concert2.setQuality(RecordingQuality.FAIR);
        List<Concert> concertList = new ArrayList<>(Arrays.asList(concert1, concert2, concert3));
        concertRepository.saveAll(concertList);
    }

    // crud methods
    @Override
    public Concert addOne(Concert concert) {
        return concert == null ? null : concertRepository.save(concert);
    }

    @Override
    public Iterable<Concert> findAll() {
        return concertRepository.findAll();
    }

    @Override
    public Concert findOneById(Long id) {
        return concertRepository.findById(id).orElse(null);
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
            return concertRepository.save(concertToUpdate);
        }
        return null;
    }

    @Override
    public Concert deleteOneById(Long id) {
        Optional<Concert> foundConcert = concertRepository.findById(id);
        if (foundConcert.isPresent()) {
            concertRepository.deleteById(id);
            return foundConcert.get();
        } else {
            return null;
        }
    }

    // end crud methods ///////////////////////////////////////////////////////////////

    @Override
    public Concert findByDateEquals(LocalDate date) {
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
    @Transactional
    public boolean addTrackToConcert(Long concert_id, Track track) {
        Concert aConcert = findOneById(concert_id);
        if (aConcert != null) {
            return aConcert.getTrackList().add(track);
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delTrackFromConcert(Long concert_id, Track track) {
        Concert aConcert = findOneById(concert_id);
        if (aConcert != null) {
            return aConcert.getTrackList().remove(track);
        } else {
            return false;
        }
    }

}
