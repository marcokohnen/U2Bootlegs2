package be.qnh.bootlegs.service;

import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TourServiceIT {

    @Autowired
    private TourService tourService;

    private Tour testTour1;
    private Tour testTour2;
    private Tour testTour3;

    @Before
    public void init() {
        testTour1 = new Tour();
        testTour1.setTitle("TitleTestTour1");
        testTour1.setStartyear(1979);
        testTour1.setContinent(Continent.EUROPE);

        testTour2 = new Tour();
        testTour2.setTitle("TitleTestTour2");
        testTour2.setStartyear(1981);
        testTour2.setContinent(Continent.AUSTRALIA);

        testTour3 = new Tour();
        testTour3.setTitle("TitleTestTour3");
        testTour3.setStartyear(1992);
        testTour3.setContinent(Continent.SOUTHAMERICA);
    }

    @Test
    public void crudTests() {
        // test create
        Tour newTour = tourService.addOne(testTour1);
        Long newId = newTour.getId();
        assertThat(newId).isNotEqualTo(0);

        // test read
        Tour foundTour = tourService.findOneById(newId);
        assertThat(foundTour.getId()).isEqualTo(newId);
        assertThat(foundTour.getTitle()).isEqualToIgnoringCase("titletesttour1");

        // test update
        foundTour.setTitle("TitleUpdated");
        Tour updatedTour = tourService.udpdateOneById(newId, foundTour);
        assertThat(updatedTour.getId()).isEqualTo(newId);
        assertThat(updatedTour.getTitle()).isEqualToIgnoringCase("titleupdated");

        // test delete
        Tour deletedTour = tourService.deleteOneById(newId);
        assertThat(deletedTour).isNotNull();
        assertThat(tourService.findOneById(newId)).isNull();
    }

    @Test
    public void additionalFindTests() {
        tourService.addOne(testTour1);
        tourService.addOne(testTour2);
        tourService.addOne(testTour3);
        Iterable<Tour> testTourIterable;

        // test findByTitleLikeIgnoreCase
        testTourIterable = tourService.findByTitleLikeIgnoreCase("tour2");
        assertThat(testTourIterable.iterator().hasNext()).isTrue();
        assertThat(testTourIterable).contains(testTour2);
        testTourIterable = tourService.findByTitleLikeIgnoreCase("Titletesttour");
        assertThat(testTourIterable).contains(testTour1, testTour2, testTour3);

        // findByStartyearGreaterThanEqual
        testTourIterable = tourService.findByStartyearGreaterThanEqual(1981);
        assertThat(testTourIterable).isNotEmpty();
        assertThat(testTourIterable).asList().hasOnlyElementsOfTypes(Tour.class);
        assertThat(testTourIterable).asList().size().isGreaterThanOrEqualTo(2);

        // findByStartyearEquals
        testTourIterable = tourService.findByStartyearEquals(1981);
        assertThat(testTourIterable).isNotEmpty();
        assertThat(testTourIterable).asList().hasOnlyElementsOfTypes(Tour.class);
        assertThat(testTourIterable).asList().size().isGreaterThanOrEqualTo(1);

        // findByContinentEquals
        testTourIterable = tourService.findByContinentEquals(Continent.EUROPE);
        assertThat(testTourIterable).isNotEmpty();
        assertThat(testTourIterable).asList().hasOnlyElementsOfTypes(Tour.class);
        assertThat(testTourIterable).contains(testTour1);
        assertThat(testTourIterable).asList().size().isGreaterThanOrEqualTo(1);
    }
}
