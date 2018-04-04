package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    /* @GetMapping
        /api/tour/findall                   : find all tours
        /api/tour/findid/{id}               : find one tour by id
        /api/tour/findtitle/{title}         : find tours by %title%
        /api/tour/findfromyear/{startYear}  : find tours by startYear and later
        /api/tour/findyear/{startYear}      : find tours by startYear
        /api/tour/findcontinent/{continent} : find tours by continent
     */

    @GetMapping("/findall")
    public ResponseEntity<Iterable<Tour>> findAll() {
        return createMultipleFindResponse(tourService.findAll());
    }

    @GetMapping("/findid/{id}")
    public ResponseEntity<Tour> findOneById(@PathVariable Long id) {
        return createSingleFindResponse(tourService.findOneById(id));
    }

    @GetMapping("/findtitle/{title}")
    public ResponseEntity<Iterable<Tour>> findByTitleLike(@PathVariable String title) {
        return createMultipleFindResponse(tourService.findByTitleLike(title));
    }

    @GetMapping("/findfromyear/{startYear}")
    public ResponseEntity<Iterable<Tour>> findByStartyearGreaterThanEqual(@PathVariable int startYear) {
        return createMultipleFindResponse(tourService.findByStartyearGreaterThanEqual(startYear));
    }

    @GetMapping("/findyear/{startYear}")
    public ResponseEntity<Iterable<Tour>> findByStartyearEquals(@PathVariable int startYear) {
        return createMultipleFindResponse(tourService.findByStartyearEquals(startYear));
    }

    @GetMapping("/findcontinent/{continent}")
    public ResponseEntity<Iterable<Tour>> findByContinentEquals(@PathVariable Continent continent) {
        return createMultipleFindResponse(tourService.findByContinentEquals(continent));
    }
    // end find mappings //////////////////////////////////////////////////////////////

    // helper methods
    private ResponseEntity<Iterable<Tour>> createMultipleFindResponse(Iterable<Tour> resultOfFind) {
        if (resultOfFind.iterator().hasNext()) {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND );
        }
    }

    private ResponseEntity<Tour> createSingleFindResponse(Tour resultOfFind) {
        if (resultOfFind == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        }
    }
    // end helper methods /////////////////////////////////////////////////////////////
}
