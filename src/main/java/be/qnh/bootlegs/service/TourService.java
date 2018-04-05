package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;

public interface TourService {

    // crud methods
    Tour addOne(Tour tour);

    Iterable<Tour> findAll();

    Tour findOneById(Long id);

    Tour udpdateOneById(Long id, Tour tour);

    Tour deleteOneById(Long id);

    // end crud methods ///////////////////////////////////////////

    Iterable<Tour> findByTitleLikeIgnoreCase(String keyWord);

    Iterable<Tour> findByStartyearGreaterThanEqual(int startYear);

    Iterable<Tour> findByStartyearEquals(int startyear);

    Iterable<Tour> findByContinentEquals (Continent continent);
}
