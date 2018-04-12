package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.service.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        /api/concert/findall            : find all tours
        /api/tour/findid/{id}           : find one tour by id
        /api/tour/findtitle/{title}     : find tours by %title% ignore case

        @PostMapping
        /api/concert                    : add one concert
        /api/concert/addtrack/{id}      : add one track to concert with id

        @PutMapping
        /api/concert/{id}               : update one concert

       @DeleteMapping
        /api/concert/{id}               : delete one concert
        /api/concert/deltrack/{id}      : delete one track from concert with id
     */

    // GETMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
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
    public ResponseEntity<Iterable<Concert>> findByDateEquals(@PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date) {
        return createMultipleResultResponse(concertService.findByDateEquals(date));
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

    // POSTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping
    public ResponseEntity<Concert> addOne(@RequestBody Concert concert) {
        Concert newConcert = concertService.addOne(concert);
        if (newConcert == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(newConcert, HttpStatus.CREATED);
        }
    }

    @PostMapping("/addtrack/{id}")
    public ResponseEntity<Boolean> addTrackToConcert(@PathVariable Long id, @RequestBody Track track) {
        if (concertService.addTrackToConcert(id, track)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // PUTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/{id}")
    public ResponseEntity<Concert> updateOne(@PathVariable Long id, @RequestBody Concert concert) {
        return createSingleResultResponse(concertService.udpdateOneById(id, concert));
    }

    // DELETEMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/{id}")
    public ResponseEntity<Concert> deleteOne(@PathVariable Long id) {
        return createSingleResultResponse(concertService.deleteOneById(id));
    }

    @DeleteMapping("/deltrack/{id}")
    public ResponseEntity<Boolean> delTrackFromConcert(@PathVariable Long id, @RequestBody Track track) {
        if (concertService.delTrackFromConcert(id, track)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
