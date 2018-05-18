package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tour")
public class TourController {

    private final TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    /* @GetMapping
        /api/activeTour/findall                   : find all tourList
        /api/activeTour/findid/{id}               : find one activeTour by id
        /api/activeTour/findtitle/{title}         : find tourList by %title% ignore case
        /api/activeTour/findfromyear/{startYear}  : find tourList by startYear and later
        /api/activeTour/findyear/{startYear}      : find tourList by startYear
        /api/activeTour/findcontinent/{continent} : find tourList by continent

       @PostMapping
        /api/activeTour                           : add one activeTour
        /api/activeTour/addconcerttotour/{tourid} : add one concert to activeTour with tourid

       @PutMapping
        /api/activeTour/{id}                      : update one activeTour

       @DeleteMapping
        /api/activeTour/{id}                      : delete one activeTour
        /api/activeTour/delconcert/{id}           : del one concert from activeTour with id

     */

    // GETMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/findall")
    public ResponseEntity<Iterable<Tour>> findAll() {
        System.out.println("Entered TourController.findAll()");
        return createMultipleResultResponse(tourService.findAll());
    }

    @GetMapping("/findid/{id}")
    public ResponseEntity<Tour> findOneById(@PathVariable Long id) {
        return createSingleResultResponse(tourService.findOneById(id));
    }

    @GetMapping("/findtitle/{title}")
    public ResponseEntity<Iterable<Tour>> findByTitleLikeIgnoreCase(@PathVariable String title) {
        return createMultipleResultResponse(tourService.findByTitleLikeIgnoreCase(title));
    }

    @GetMapping("/findfromyear/{startYear}")
    public ResponseEntity<Iterable<Tour>> findByStartyearGreaterThanEqual(@PathVariable int startYear) {
        return createMultipleResultResponse(tourService.findByStartyearGreaterThanEqual(startYear));
    }

    @GetMapping("/findyear/{startYear}")
    public ResponseEntity<Iterable<Tour>> findByStartyearEquals(@PathVariable int startYear) {
        return createMultipleResultResponse(tourService.findByStartyearEquals(startYear));
    }

    @GetMapping("/findcontinent/{continent}")
    public ResponseEntity<Iterable<Tour>> findByContinentEquals(@PathVariable Continent continent) {
        return createMultipleResultResponse(tourService.findByContinentEquals(continent));
    }


    // POSTTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping
    public ResponseEntity<Tour> addOne(@RequestBody Tour tour) {
        System.out.println("Entered TourController.addOne()");
        Tour newTour = tourService.addOne(tour);
           if (newTour == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(newTour, HttpStatus.CREATED);
        }
    }

    @PostMapping("/addconcerttotour/{tourId}")
    public ResponseEntity<Concert> addConcertToTour(@PathVariable Long tourId, @RequestBody Concert concert) {
        Concert updatedConcert = tourService.addConcertToTour(tourId, concert);
        if (updatedConcert != null) {
            return new ResponseEntity<>(updatedConcert, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/{id}")
    public ResponseEntity<Tour> updateOne(@PathVariable Long id, @RequestBody Tour tour) {
        return createSingleResultResponse(tourService.udpdateOneById(id, tour));
    }

    // DELETEMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/{id}")
    public ResponseEntity<Tour> deleteOne(@PathVariable Long id) {
        return createSingleResultResponse(tourService.deleteOneById(id));
    }

    @DeleteMapping("/delconcert/{id}")
    public ResponseEntity<Boolean> delConcertFromTour(@PathVariable Long id, @RequestBody Concert concert) {
        if (tourService.delConcertFromTour(id, concert)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    // END MAPPINGS ///////////////////////////////////////////////////////////////////////////////////////////////////

    // helper methods /////////////////////////////////////////////////////////////////////////////////////////////////
    private ResponseEntity<Iterable<Tour>> createMultipleResultResponse(Iterable<Tour> resultOfFind) {
        if (resultOfFind.iterator().hasNext()) {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<Tour> createSingleResultResponse(Tour resultOfFind) {
        if (resultOfFind == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        }
    }
    // end helper methods /////////////////////////////////////////////////////////////
}
