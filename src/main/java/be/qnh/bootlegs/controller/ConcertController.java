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
        /api/concert/findall            : find all concerts
        /api/concert/findid/{id}        : find one concert by id
        /api/concert/findtitle/{title}  : find concerts by %title% ignore case
        /api/concert/findcountry/{country}  : find concerts by %country% ignore case
        /api/concert/findcity/{city}    : find concerts by %city% ignore case
        /api/concert/findcity/{city}    : find concerts by %city% ignore case
        /api/concert/findtour/{concertId} : find tourId by concertId


        @PostMapping
        /api/concert                    : add one concert
        /api/concert/addtracktoconcert/{concertid}   : add one track to concert with id

        @PutMapping
        /api/concert/{id}               : update one concert

       @DeleteMapping
        /api/concert/{id}               : delete one concert
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

    @GetMapping("/findtour/{concertId}")
    public ResponseEntity<Long> findTourIdByConcertId(@PathVariable Long concertId) {
        Long tourId = concertService.findTourIdByConcertId(concertId);
        if (tourId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(tourId, HttpStatus.OK);
        }
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

    @PostMapping("/addtracktoconcert/{concertid}")
    public ResponseEntity<Track> addTrackToConcert(@PathVariable Long concertid, @RequestBody Track track) {
        Track newTrack = concertService.addTrackToConcert(concertid, track);
        if (newTrack != null) {
            return new ResponseEntity<>(newTrack, HttpStatus.CREATED);
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
