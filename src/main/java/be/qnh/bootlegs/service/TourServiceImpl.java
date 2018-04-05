package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;

    @Autowired
    public TourServiceImpl(TourRepository tourTourRepository) {
        this.tourRepository = tourTourRepository;
    }

    @PostConstruct
    private void init() {
        Tour tour1 = new Tour();
        tour1.setTitle("Boy");
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
            tourToUpdate.setConcerts(tour.getConcerts());
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
