package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Tour;

import java.util.Optional;

public interface TourService {

    // crud methods
    Tour addOne(Tour tour);

    Iterable<Tour> findAll();

    Optional<Tour> findOneById(Long id);

    Tour udpdateOneById(Long id, Tour tour);

    Tour deleteOneById(Long id);

    // end crud methods ///////////////////////////////////////////
}
