package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.repository.ConcertRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConcertServiceImplUnitTest {

    private Concert testConcert1, testConcert2, testConcert3;
    private List<Concert> concerts;

    @InjectMocks
    private ConcertServiceImpl concertService;

    @Mock
    private ConcertRepository concertRepository;

    @Before
    public void init() {
        testConcert1 = new Concert();
        testConcert1.setTitle("TestConcertTitle1");
        testConcert1.setDate(LocalDate.of(2018, 4, 30));
        testConcert1.setCountry("Belgium");
        testConcert1.setCity("Hasselt");
        testConcert1.setQuality(RecordingQuality.FAIR);

        testConcert2 = new Concert();
        testConcert2.setTitle("TestConcertTitle2");
        testConcert2.setDate(LocalDate.of(2018, 5, 15));
        testConcert2.setCountry("Netherlands");
        testConcert2.setCity("Amsterdam");
        testConcert1.setQuality(RecordingQuality.FAIR);

        testConcert3 = new Concert();
        testConcert3.setTitle("TestConcertTitle3");
        testConcert3.setDate(LocalDate.of(2018, 5, 30));
        testConcert3.setCountry("Germany");
        testConcert3.setCity("Cologne");
        testConcert1.setQuality(RecordingQuality.GOOD);

        concerts = new ArrayList<>();
        concerts.addAll(Arrays.asList(testConcert1, testConcert2, testConcert3));

    }

    @After
    public void deleteTestObjects() {
        testConcert1 = null;
        testConcert2 = null;
        testConcert3 = null;
        concerts = null;
    }

    @Test
    public void testAddOne() {
        when(concertRepository.save(testConcert1)).thenReturn(testConcert1);

        Concert newConcert = concertService.addOne(testConcert1);

        assertThat(newConcert).isEqualTo(testConcert1);
        assertThat(newConcert.getTitle()).isEqualToIgnoringCase("testconcertTITLE1");

        verify(concertRepository, times(1)).save(testConcert1);
    }

    @Test
    public void testFindAll() {
        when(concertRepository.findAll()).thenReturn(concerts);

        List<Concert> concertsFromService = (List<Concert>) concertService.findAll();

        assertThat(concertsFromService).contains(testConcert1, testConcert2, testConcert3);

        verify(concertRepository, times(1)).findAll();
    }

    @Test
    public void testFindOneById() {
        when(concertRepository.findById(2L)).thenReturn(Optional.of(testConcert2));

        Concert concertFoundById = concertService.findOneById(2L);

        assertThat(concertFoundById).isEqualTo(testConcert2);
        assertThat(concertFoundById.getTitle()).isEqualToIgnoringCase("testcOnceRtTITle2");

        verify(concertRepository, times(1)).findById(2L);
    }

    @Test
    public void testUdpdateOneById() {
        when(concertRepository.findById(3L)).thenReturn(Optional.of(testConcert3));
        when(concertRepository.save(testConcert3)).thenReturn(testConcert2);

        Concert updatedConcertFromService = concertService.udpdateOneById(3L, testConcert3);

        assertThat(updatedConcertFromService).isEqualTo(testConcert2);

        verify(concertRepository, times(1)).findById(3L);
        verify(concertRepository, times(1)).save(testConcert3);
    }

    @Test
    public void testDeleteOneById() {
        when(concertRepository.findById(1L)).thenReturn(Optional.of(testConcert1));

        Concert deletedConcertFromService = concertService.deleteOneById(1L);

        assertThat(deletedConcertFromService).isEqualTo(testConcert1);

        verify(concertRepository, times(1)).findById(1L);
        verify(concertRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByDateEquals() {
        when(concertRepository.findByDateEquals(LocalDate.of(2018, 5, 15))).thenReturn(Collections.singletonList(testConcert2));

        List<Concert> foundConcerts = (List<Concert>) concertService.findByDateEquals(LocalDate.of(2018, 5, 15));

        assertThat(foundConcerts.size()).isEqualTo(1);
        assertThat(foundConcerts).contains(testConcert2);

        verify(concertRepository, times(1)).findByDateEquals(LocalDate.of(2018, 5, 15));
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() {
        when(concertRepository.findByTitleLikeIgnoreCase("%TiTlE2%")).thenReturn(Collections.singletonList(testConcert2));

        List<Concert> foundConcerts = (List<Concert>) concertService.findByTitleLikeIgnoreCase("TiTlE2");

        assertThat(foundConcerts.size()).isEqualTo(1);
        assertThat(foundConcerts).contains(testConcert2);
    }

    @Test
    public void testFindByCountryLikeIgnoreCase() {
        when(concertRepository.findByCountryLikeIgnoreCase("%gerMany%")).thenReturn(Collections.singletonList(testConcert3));

        List<Concert> foundConcerts = (List<Concert>) concertService.findByCountryLikeIgnoreCase("gerMany");

        assertThat(foundConcerts.size()).isEqualTo(1);
        assertThat(foundConcerts).contains(testConcert3);
    }

    @Test
    public void testFindByCityLikeIgnoreCase() {
        when(concertRepository.findByCityLikeIgnoreCase("%Hassel%")).thenReturn(Collections.singletonList(testConcert1));

        List<Concert> foundConcerts = (List<Concert>) concertService.findByCityLikeIgnoreCase("Hassel");

        assertThat(foundConcerts.size()).isEqualTo(1);
        assertThat(foundConcerts).contains(testConcert1);
    }

    @Test
    public void testFindByRecordingQuality() {
        when(concertRepository.findByQuality(RecordingQuality.FAIR)).thenReturn(Arrays.asList(testConcert1, testConcert2));

        List<Concert> foundConcerts = (List<Concert>) concertService.findByRecordingQuality(RecordingQuality.FAIR);

        assertThat(foundConcerts.size()).isEqualTo(2);
        assertThat(foundConcerts).contains(testConcert1, testConcert2);
    }

    @Test
    public void tetsFindConcertIdByConcertId() {
        when(concertRepository.findTourIdByConcertId(2L)).thenReturn(1L);

        Long foundTourId = concertService.findTourIdByConcertId(2L);

        assertThat(foundTourId).isEqualTo(1L);

        verify(concertRepository, times(1)).findTourIdByConcertId(2L);
    }

    @Test
    public void testAddTrackToConcert() {
        Track newTrack = new Track();
        newTrack.setTitle("newTrack");
        newTrack.setSequenceNr(1);
        newTrack.setLocationUrl("newTrackUrl");

        when(concertRepository.findById(3L)).thenReturn(Optional.of(testConcert3));

        Track addedTrack = concertService.addTrackToConcert(3L, newTrack);

        assertThat(addedTrack).isEqualTo(newTrack);
        assertThat(testConcert3.getTrackList()).contains(newTrack);

        verify(concertRepository, times(1)).findById(3L);
    }
}