package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.repository.ConcertRepository;
import be.qnh.bootlegs.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final ConcertRepository concertRepository;

    @Autowired
    public TourServiceImpl(TourRepository tourTourRepository, ConcertRepository concertRepository) {
        this.tourRepository = tourTourRepository;
        this.concertRepository = concertRepository;
    }

    @PostConstruct
    public void init() {
        Tour tour1 = new Tour();
        tour1.setTitle("New Boy");
        tour1.setStartyear(1983);
        tour1.setContinent(Continent.EUROPE);
        Tour tour2 = new Tour();
        tour2.setTitle("October");
        tour2.setStartyear(1985);
        tour2.setContinent(Continent.NORTHAMERICA);
        Tour tour3 = new Tour();
        tour3.setTitle("Joshua Tree");
        tour3.setStartyear(1987);
        tour3.setContinent(Continent.AUSTRALIA);

        Concert concert1 = new Concert();
        concert1.setTitle("ConcertTitle 1");
        concert1.setDate(LocalDate.of(1981, 3, 26));
        concert1.setCity("Boston");
        concert1.setCountry("USA");
        concert1.setQuality(RecordingQuality.GOOD);
        Concert concert2 = new Concert();
        concert2.setTitle("ConcertTitle 2");
        concert2.setDate(LocalDate.of(1984, 10, 30));
        concert2.setCity("Rotterdam");
        concert2.setCountry("Netherlands");
        concert2.setQuality(RecordingQuality.EXCELENT);
        Concert concert3 = new Concert();
        concert3.setTitle("ConcertTitle 3");
        concert3.setDate(LocalDate.of(1997, 4, 25));
        concert3.setCity("Las Vegas");
        concert3.setCountry("USA");
        concert3.setQuality(RecordingQuality.FAIR);
        Concert concert4 = new Concert();
        concert4.setTitle("ConcertTitle 4");
        concert4.setDate(LocalDate.of(1981, 3, 26));
        concert4.setCity("Boston");
        concert4.setCountry("USA");
        concert4.setQuality(RecordingQuality.GOOD);
        Concert concert5 = new Concert();
        concert5.setTitle("ConcertTitle 5");
        concert5.setDate(LocalDate.of(1984, 10, 30));
        concert5.setCity("Rotterdam");
        concert5.setCountry("Netherlands");
        concert5.setQuality(RecordingQuality.EXCELENT);
        Concert concert6 = new Concert();
        concert6.setTitle("ConcertTitle 6");
        concert6.setDate(LocalDate.of(1997, 4, 25));
        concert6.setCity("Las Vegas");
        concert6.setCountry("USA");
        concert6.setQuality(RecordingQuality.FAIR);

        tour1.setConcertList(new ArrayList<>(Arrays.asList(concert1, concert2)));
        tour2.setConcertList(new ArrayList<>(Arrays.asList(concert2, concert4)));
        tour3.setConcertList(new ArrayList<>(Arrays.asList(concert5, concert6)));

        List<Tour> tourList = new ArrayList<>(Arrays.asList(tour1, tour2, tour3));
        tourRepository.saveAll(tourList);
    }

    // crud methods
    @Override
    public Tour addOne(Tour tour) {
        return tour == null ? null : tourRepository.save(tour);
    }

    @Override
    public Iterable<Tour> findAll() {
        return tourRepository.findAll();
    }

    @Override
    public Tour findOneById(Long id) {
        return tourRepository.findById(id).orElse(null);
    }

    @Override
    public Tour udpdateOneById(Long id, Tour tour) {
        Optional<Tour> foundTour = tourRepository.findById(id);
        if (foundTour.isPresent()) {
            Tour tourToUpdate = foundTour.get();
            tourToUpdate.setConcertList(tour.getConcertList());
            tourToUpdate.setContinent(tour.getContinent());
            tourToUpdate.setStartyear(tour.getStartyear());
            tourToUpdate.setEndYear(tour.getEndYear());
            tourToUpdate.setLeg(tour.getLeg());
            tourToUpdate.setTitle(tour.getTitle());
            return tourRepository.save(tourToUpdate);
        } else {
            return null;
        }
    }

    @Override
    public Tour deleteOneById(Long id) {
        Optional<Tour> foundTour = tourRepository.findById(id);
        if (foundTour.isPresent()) {
            tourRepository.deleteById(id);
            return foundTour.get();
        } else {
            return null;
        }
    }

    // end crud methods ///////////////////////////////////////////////////////////////

    @Override
    public Iterable<Tour> findByTitleLikeIgnoreCase(String keyWord) {
        keyWord = "%" + keyWord + "%";
        return tourRepository.findByTitleLikeIgnoreCase(keyWord);
    }

    @Override
    public Iterable<Tour> findByStartyearGreaterThanEqual(int startYear) {
        return tourRepository.findByStartyearGreaterThanEqual(startYear);
    }

    @Override
    public Iterable<Tour> findByStartyearEquals(int startyear) {
        return tourRepository.findByStartyearEquals(startyear);
    }

    @Override
    public Iterable<Tour> findByContinentEquals(Continent continent) {
        return tourRepository.findByContinentEquals(continent);
    }
}
