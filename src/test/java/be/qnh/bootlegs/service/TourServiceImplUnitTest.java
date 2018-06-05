package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.repository.TourRepository;
import com.google.common.collect.Lists;
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

@RunWith(value = MockitoJUnitRunner.class)
public class TourServiceImplUnitTest {

    private Tour testTour1, testTour2, testTour3;
    private List<Tour> tours;

    @InjectMocks
    private TourServiceImpl tourService;

    @Mock
    private TourRepository tourRepository;

    @Before
    public void init() {
        // create test-objects
        testTour1 = new Tour();
        testTour1.setTitle("TestTourTitle1");
        testTour1.setLeg(1);
        testTour1.setStartyear(2001);
        testTour1.setEndyear(2002);
        testTour1.setContinent(Continent.EUROPE);

        testTour2 = new Tour();
        testTour2.setTitle("TestTourTitle2");
        testTour2.setLeg(3);
        testTour2.setStartyear(2002);
        testTour2.setEndyear(2003);
        testTour2.setContinent(Continent.NEWZEALAND);

        testTour3 = new Tour();
        testTour3.setTitle("TestTourTitle3");
        testTour3.setLeg(3);
        testTour3.setStartyear(2003);
        testTour3.setEndyear(2004);
        testTour3.setContinent(Continent.NORTHAMERICA);

        tours = new ArrayList<>();
        tours.addAll(Arrays.asList(testTour1, testTour2, testTour3));
    }

    @After
    public void deleteTestObjects() {
        testTour1 = null;
        testTour2 = null;
        testTour3 = null;
        tours = null;
    }

    @Test
    public void testAddOne() {
        when(tourRepository.save(testTour1)).thenReturn(testTour1);

        Tour tourFromService = tourService.addOne(testTour1);

        assertThat(tourFromService).isNotNull();
        assertThat(tourFromService).isEqualTo(testTour1);
        assertThat(tourFromService).hasSameClassAs(testTour1);
        assertThat(tourFromService.getTitle()).isEqualToIgnoringCase("testtourtitle1");

        verify(tourRepository, times(1)).save(testTour1);
    }

    @Test
    public void testFindAll() {
        when(tourRepository.findAll()).thenReturn(tours);

        List<Tour> toursFromService = (List<Tour>) tourService.findAll();

        assertThat(toursFromService.iterator().hasNext()).isTrue();
        assertThat(toursFromService).isNotNull();
        assertThat(toursFromService.size()).isEqualTo(3);

        verify(tourRepository, times(1)).findAll();
    }

    @Test
    public void findOneById() {
        when(tourRepository.findById(2L)).thenReturn(Optional.of(testTour2));

        Tour tourFromService = tourService.findOneById(2L);

        assertThat(tourFromService).isNotNull();
        assertThat(tourFromService.getTitle()).isEqualToIgnoringCase("testtourtitle2");

        verify(tourRepository, times(1)).findById(2L);
    }

    @Test
    public void udpdateOneById() {
        when(tourRepository.findById(3L)).thenReturn(Optional.of(testTour3));
        when(tourRepository.save(testTour3)).thenReturn(testTour2);

        Tour updatedTourFromService = tourService.udpdateOneById(3L, testTour3);

        assertThat(updatedTourFromService).isEqualTo(testTour2);

        verify(tourRepository, times(1)).findById(3L);
        verify(tourRepository, times(1)).save(testTour3);
    }

    @Test
    public void findByTitleLikeIgnoreCase() {
        when(tourRepository.findByTitleLikeIgnoreCase("%TITLE3%")).thenReturn(Lists.newArrayList(testTour3));

        List<Tour> resultFromService = (List<Tour>) tourService.findByTitleLikeIgnoreCase("TITLE3");

        assertThat(resultFromService.size()).isEqualTo(1);
        assertThat(resultFromService).contains(testTour3);

        verify(tourRepository, times(1)).findByTitleLikeIgnoreCase("%TITLE3%");
    }

    @Test
    public void findByStartyearGreaterThanEqual() {
        when(tourRepository.findByStartyearGreaterThanEqual(2002)).thenReturn(Collections.unmodifiableList(Arrays.asList(testTour2, testTour3)));

        List<Tour> resultFromService = (List<Tour>) tourService.findByStartyearGreaterThanEqual(2002);

        assertThat(resultFromService.size()).isEqualTo(2);
        assertThat(resultFromService).contains(testTour2, testTour3);

        verify(tourRepository, times(1)).findByStartyearGreaterThanEqual(2002);
    }

    @Test
    public void findByStartyearEquals() {
        when(tourRepository.findByStartyearEquals(2002)).thenReturn(Collections.singletonList(testTour2));

        List<Tour> resultFromService = (List<Tour>) tourService.findByStartyearEquals(2002);

        assertThat(resultFromService.size()).isEqualTo(1);
        assertThat(resultFromService).contains(testTour2);

        verify(tourRepository, times(1)).findByStartyearEquals(2002);
    }

    @Test
    public void findByContinentEquals() {
        when(tourRepository.findByContinentEquals(Continent.NORTHAMERICA)).thenReturn(Collections.singletonList(testTour3));

        List<Tour> resultFromService = (List<Tour>) tourService.findByContinentEquals(Continent.NORTHAMERICA);

        assertThat(resultFromService.size()).isEqualTo(1);
        assertThat(resultFromService).contains(testTour3);

        verify(tourRepository, times(1)).findByContinentEquals(Continent.NORTHAMERICA);
    }

    @Test
    public void addConcertToTour() {
        Concert newConcert = new Concert();
        newConcert.setTitle("NewConcert");
        newConcert.setDate(LocalDate.of(2018, 5, 30));
        newConcert.setCountry("Belgium");
        newConcert.setCity("Hasselt");

        when(tourRepository.findById(8L)).thenReturn(Optional.of(testTour2));

        Concert addedConcert = tourService.addConcertToTour(8L, newConcert);

        assertThat(addedConcert).isEqualTo(newConcert);
        assertThat(addedConcert.getTitle()).isEqualToIgnoringCase("newconcert");
        assertThat(testTour2.getConcertList()).contains(addedConcert);

        verify(tourRepository, times(1)).findById(8L);
    }

    @Test
    public void deleteOneByIdTest() {
        when(tourRepository.findById(10L)).thenReturn(Optional.of(testTour1));

        Tour deletedTour = tourService.deleteOneById(10L);

        assertThat(deletedTour).isEqualTo(testTour1);

        verify(tourRepository, times(1)).findById(10L);
        verify(tourRepository, times(1)).deleteById(10L);
    }
}