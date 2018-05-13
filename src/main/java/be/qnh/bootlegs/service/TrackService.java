package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Track;

import javax.validation.constraints.NotNull;

public interface TrackService {

    // crud methods
    Track addOne(Track track);

    Iterable<Track> findAll();

    Track findOneById(Long id);

    Track updateOneById(Long id, Track concert);

    Track deleteOneById(Long id);

    // end crud methods ///////////////////////////////////////////

    Iterable<Track> findByTitleLikeIgnoreCase(@NotNull String title);

}
