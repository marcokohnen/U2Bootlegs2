package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.TrackRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TrackServiceImplUnitTest {

    private Track testTrack1, testTrack2, testTrack3;
    private List<Track> tracks;

    @InjectMocks
    private TrackServiceImpl trackService;

    @Mock
    private TrackRepository trackRepository;

    public void init() {
        testTrack1 = new Track();
        testTrack1.setSequenceNr(1);
        testTrack1.setTitle("TestTrackTitle1");
        testTrack1.setLocationUrl("urlTrack1");

        testTrack2 = new Track();
        testTrack2.setSequenceNr(1);
        testTrack2.setTitle("TestTrackTitle2");
        testTrack2.setLocationUrl("urlTrack2");

        testTrack3 = new Track();
        testTrack3.setSequenceNr(1);
        testTrack3.setTitle("TestTrackTitle3");
        testTrack3.setLocationUrl("urlTrack3");

        tracks = new ArrayList<>();
        tracks.addAll(Arrays.asList(testTrack1, testTrack2, testTrack3));
    }

    @Test
    public void testAddOne() {
    }

    @Test
    public void testFindAll() {
    }

    @Test
    public void testFindOneById() {
    }

    @Test
    public void testUpdateOneById() {
    }

    @Test
    public void testDeleteOneById() {
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() {
    }
}