package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.service.TrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/track")
public class TrackController {

    private final TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    /* @GetMapping

        /api/track/findall                   : find all tracks
        /api/track/findid/{id}               : find one track by id
        /api/track/findtitle/{title}         : find tracks by %title% ignore case

       @PostMapping
        /api/track  : add one track

       @PutMapping
        /api/track/{id}  : update one track

       @DeleteMapping
        /api/track/{id} : delete one track

     */

    // GETMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/findall")
    public ResponseEntity<Iterable<Track>> findAll() {
        return createMultipleResultResponse(trackService.findAll());
    }

    @GetMapping("/findid/{id}")
    public ResponseEntity<Track> findOneById(@PathVariable Long id) {
        return createSingleResultResponse(trackService.findOneById(id));
    }

    @GetMapping("/findtitle/{title}")
    public ResponseEntity<Iterable<Track>> findByTitleLikeIgnoreCase(@PathVariable String title) {
        return createMultipleResultResponse(trackService.findByTitleLikeIgnoreCase(title));
    }

    // POSTTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PostMapping
    public ResponseEntity<Track> addOne(@RequestBody Track track) {
        Track newTrack = trackService.addOne(track);
        if (newTrack == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(newTrack, HttpStatus.CREATED);
        }
    }

    // PUTMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @PutMapping("/{id}")
    public ResponseEntity<Track> updateOne(@PathVariable Long id, @RequestBody Track track) {
        return createSingleResultResponse(trackService.udpdateOneById(id, track));
    }

    // DELETEMAPPINGS /////////////////////////////////////////////////////////////////////////////////////////////////
    @DeleteMapping("/{id}")
    public ResponseEntity<Track> deleteOne(@PathVariable Long id) {
        return createSingleResultResponse(trackService.deleteOneById(id));
    }

    // END MAPPINGS ///////////////////////////////////////////////////////////////////////////////////////////////////

    // helper methods /////////////////////////////////////////////////////////////////////////////////////////////////
    private ResponseEntity<Iterable<Track>> createMultipleResultResponse(Iterable<Track> resultOfFind) {
        if (resultOfFind.iterator().hasNext()) {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<Track> createSingleResultResponse(Track resultOfFind) {
        if (resultOfFind == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(resultOfFind, HttpStatus.OK);
        }
    }
    // end helper methods /////////////////////////////////////////////////////////////
}
