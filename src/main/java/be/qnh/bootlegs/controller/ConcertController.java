package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @Autowired
    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }

    /* @GetMapping
        /api/concert/findall                : find all tours
        /api/tour/findid/{id}               : find one tour by id
        /api/tour/findtitle/{title}         : find tours by %title% ignore case
     */

    @GetMapping("/findall")
    public ResponseEntity<Iterable<Concert>> findAll() {
        return createMultipleResultResponse(concertService.findAll());
    }

    @GetMapping("/findid/{id}")
    public ResponseEntity<Concert> findOneById(@PathVariable Long id) {
        return createSingleResultResponse(concertService.findOneById(id));
    }

    @GetMapping("/findtitle/{title}")
    public ResponseEntity<Iterable<Concert>> findByTitleLikeIgnoreCase(@PathVariable String title) {
        return createMultipleResultResponse(concertService.findByTitleLikeIgnoreCase(title));
    }

    @GetMapping("/finddate/{date}")
    public ResponseEntity<Concert> findByDateEquals(@PathVariable LocalDate date) {
        return createSingleResultResponse(concertService.findByDateEquals(date));
    }

    @GetMapping("/findcountry/{country}")
    public ResponseEntity<Iterable<Concert>> findByCountryLikeIgnoreCase(@PathVariable String country) {
        return createMultipleResultResponse(concertService.findByCountryLikeIgnoreCase(country));
    }

    @GetMapping("/findcity/{city}")
    public ResponseEntity<Iterable<Concert>> findByCityLikeIgnoreCase(@PathVariable String city) {
        return createMultipleResultResponse(concertService.findByCityLikeIgnoreCase(city));
    }

    @GetMapping("/findquality/{recordingQuality}")
    public ResponseEntity<Iterable<Concert>> findByRecordingQuality(@PathVariable RecordingQuality recordingQuality) {
        return createMultipleResultResponse(concertService.findByRecordingQuality(recordingQuality));
    }

    // helper methods
    private ResponseEntity<Iterable<Concert>> createMultipleResultResponse(Iterable<Concert> resultOfFind) {
        if (resultOfFind.iterator().hasNext()) {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<Concert> createSingleResultResponse(Concert resultOfFind) {
        if (resultOfFind == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        }
    }
    // end helper methods /////////////////////////////////////////////////////////////

}
