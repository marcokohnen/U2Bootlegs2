package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.repository.TourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TourServiceImpl implements TourService {

    private final Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);

    private final TourRepository tourRepository;

    @Autowired
    public TourServiceImpl(TourRepository tourTourRepository) {
        this.tourRepository = tourTourRepository;
    }

//    @PostConstruct
//    @Transactional
//    public void init() {
//        Tour tour1 = new Tour();
//        tour1.setTitle("New Boy");
//        tour1.setStartyear(1983);
//        tour1.setEndyear(1984);
//        tour1.setLeg(3);
//        tour1.setContinent(Continent.EUROPE);
//
//        Tour tour2 = new Tour();
//        tour2.setTitle("October");
//        tour2.setStartyear(1985);
//        tour2.setEndyear(1985);
//        tour2.setLeg(2);
//        tour2.setContinent(Continent.NORTHAMERICA);
//
//        Tour tour3 = new Tour();
//        tour3.setTitle("Joshua Tree");
//        tour3.setStartyear(1987);
//        tour3.setEndyear(1988);
//        tour3.setLeg(1);
//        tour3.setContinent(Continent.AUSTRALIA);
//
//        Concert concert1 = new Concert();
//        concert1.setTitle("ConcertTitle 1");
//        concert1.setDate(LocalDate.of(1987, 3, 26));
//        concert1.setCity("Boston");
//        concert1.setCountry("USA");
//        concert1.setVenue("London School of Economics");
//        concert1.setQuality(RecordingQuality.GOOD);
//
//        Concert concert2 = new Concert();
//        concert2.setTitle("ConcertTitle 2");
//        concert2.setDate(LocalDate.of(2004, 10, 30));
//        concert2.setCity("Rotterdam");
//        concert2.setCountry("Netherlands");
//        concert2.setQuality(RecordingQuality.EXCELLENT);
//
//        Concert concert3 = new Concert();
//        concert3.setTitle("ConcertTitle 3");
//        concert3.setDate(LocalDate.of(1997, 4, 25));
//        concert3.setCity("Las Vegas");
//        concert3.setCountry("USA");
//        concert3.setQuality(RecordingQuality.FAIR);
//
//        Concert concert4 = new Concert();
//        concert4.setTitle("ConcertTitle 4");
//        concert4.setDate(LocalDate.of(1987, 3, 26));
//        concert4.setCity("Boston");
//        concert4.setCountry("USA");
//        concert4.setQuality(RecordingQuality.GOOD);
//
//        Concert concert5 = new Concert();
//        concert5.setTitle("ConcertTitle 5");
//        concert5.setDate(LocalDate.of(1984, 10, 30));
//        concert5.setCity("Rotterdam");
//        concert5.setCountry("Netherlands");
//        concert5.setQuality(RecordingQuality.EXCELLENT);
//
//        Concert concert6 = new Concert();
//        concert6.setTitle("ConcertTitle 6");
//        concert6.setDate(LocalDate.of(1997, 4, 25));
//        concert6.setCity("Las Vegas");
//        concert6.setCountry("USA");
//        concert6.setQuality(RecordingQuality.FAIR);
//
//        Track track1 = new Track();
//        track1.setTitle("Track1");
//        track1.setSequenceNr(1);
//        track1.setLocationUrl("url1");
//
//        Track track2 = new Track();
//        track2.setTitle("Track2");
//        track2.setSequenceNr(2);
//        track2.setLocationUrl("url2");
//
//        Track track3 = new Track();
//        track3.setTitle("Track3");
//        track3.setSequenceNr(3);
//        track3.setLocationUrl("url3");
//
//        Track track4 = new Track();
//        track4.setTitle("Track4");
//        track4.setSequenceNr(4);
//        track4.setLocationUrl("url4");
//
//        Track track5 = new Track();
//        track5.setTitle("Track5");
//        track5.setSequenceNr(5);
//        track5.setLocationUrl("url5");
//
//        Track track6 = new Track();
//        track6.setTitle("Track6");
//        track6.setSequenceNr(6);
//        track6.setLocationUrl("url6");
//
//        concert1.setTrackList(new ArrayList<>(Arrays.asList(track1, track2)));
//        concert2.setTrackList(new ArrayList<>(Arrays.asList(track3, track4)));
//        concert6.setTrackList(new ArrayList<>(Arrays.asList(track5, track6)));
//
//        tour1.setConcertList(new ArrayList<>(Arrays.asList(concert1, concert2)));
//        tour2.setConcertList(new ArrayList<>(Arrays.asList(concert3, concert2)));
//        tour3.setConcertList(new ArrayList<>(Arrays.asList(concert2, concert6)));
//
//        List<Tour> tourList = new ArrayList<>(Arrays.asList(tour1, tour2, tour3));
//        tourRepository.saveAll(tourList);
//    }

    // crud methods
    @Override
    public Tour addOne(Tour tour) {
        Tour newTour = tourRepository.save(tour);
        logger.info("addedTour = [{}]", newTour);
        return newTour;
    }

    @Override
    public Iterable<Tour> findAll() {
        List<Tour> result = tourRepository.findAll();
        //logger.info("result findAll = " + result);
        return result;
    }

    @Override
    public Tour findOneById(Long id) {
        Optional<Tour> foundTour = tourRepository.findById(id);
        if (foundTour.isPresent()) {
            Tour result = foundTour.get();
            logger.info("foundTour = [{}]", foundTour.get());
            return result;
        } else {
            return null;
        }
    }

    @Override
    public Tour udpdateOneById(Long id, Tour tour) {
        Optional<Tour> foundTour = tourRepository.findById(id);
        if (foundTour.isPresent()) {
            Tour tourToUpdate = foundTour.get();
            tourToUpdate.setConcertList(tour.getConcertList());
            tourToUpdate.setContinent(tour.getContinent());
            tourToUpdate.setStartyear(tour.getStartyear());
            tourToUpdate.setEndyear(tour.getEndyear());
            tourToUpdate.setLeg(tour.getLeg());
            tourToUpdate.setTitle(tour.getTitle());
            Tour result = tourRepository.save(tourToUpdate);
            logger.info("updatedTour = [{}]", result);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public Tour deleteOneById(Long id) {
        Optional<Tour> foundTour = tourRepository.findById(id);
        if (foundTour.isPresent()) {
            tourRepository.deleteById(id);
            logger.info("deletedTour = [{}]", foundTour.get());
            return foundTour.get();
        } else {
            return null;
        }
    }

    // end crud methods ///////////////////////////////////////////////////////////////

    @Override
    public Iterable<Tour> findByTitleLikeIgnoreCase(String keyWord) {
        //logger.info("keyword = " + keyWord);
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

    @Override
    public Concert addConcertToTour(Long tour_Id, Concert newConcert) {
        Tour aTour = findOneById(tour_Id);
        if (aTour != null) {
            if (aTour.getConcertList().add(newConcert)) {
                logger.info("New Concert = " + newConcert + " added to Tour =" + aTour);
                return newConcert;
            } else {
                return null;
            }
            //addOne(aTour); is hier niet nodig omdat aTour gemanaged is door hibernate als gevolg van de findOneById method en door list.add(concert) al gesaved wordt. Nog eens saven zou een fout genereren :"entity already persisted". Omdat aTour gemanaged is (bevind zich in de PersistenceContext) worden wijzigingen aan dit object weggeschreven naar de databank zodra de transactie is voltooid = commit (@Transactional) !!
        } else {
            return null;
        }
    }

}
