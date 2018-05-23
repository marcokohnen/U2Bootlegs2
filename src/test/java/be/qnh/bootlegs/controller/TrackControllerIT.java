package be.qnh.bootlegs.controller;


import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.domain.Track;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootlegsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TrackControllerIT {

    private static final String BASE_URI = "/api/track";
    private TestRestTemplate testRestTemplate;
    private HttpHeaders httpHeaders;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @Test
    public void crudTests() {
        // een track kan alleen worden toegevoegd aan een bestaand concert en een concert kan alleen worden toegevoegd aan een bestaande tour
        Tour testTour1 = new Tour();
        testTour1.setTitle("TitleTestTour1");
        testTour1.setLeg(1);
        testTour1.setStartyear(2018);
        testTour1.setEndyear(2018);
        testTour1.setContinent(Continent.NORTHAMERICA);

        Concert testConcert1 = new Concert();
        testConcert1.setTitle("TitleTestConcert1");
        testConcert1.setCity("CityTestConcert1");
        testConcert1.setCountry("USA");
        testConcert1.setDate(LocalDate.of(2018, 4, 11));

        Track testTrack1 = new Track();
        testTrack1.setTitle("TitleTestTrack1");
        testTrack1.setSequenceNr(8);
        testTrack1.setLocationUrl("URLTestTrack");

        // test create
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // create tour = testTour1
        HttpEntity<Tour> httpCreateTourEntity = new HttpEntity<>(testTour1, httpHeaders);
        ResponseEntity<Tour> responseEntityCreateTour = testRestTemplate.postForEntity(createURLWithPort("api/tour/"), httpCreateTourEntity, Tour.class);
        assertThat(responseEntityCreateTour.getBody()).isNotNull();
        assertThat(responseEntityCreateTour.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateTour.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTour1");
        Long newTourId = responseEntityCreateTour.getBody().getId();

        // create concert = testConcert1 and add to new tour = testTour1
        HttpEntity<Concert> httpCreateConcertEntity = new HttpEntity<>(testConcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityCreateConcert = testRestTemplate.postForEntity(createURLWithPort("api/tour/addconcerttotour/" + newTourId), httpCreateConcertEntity, Concert.class);
        assertThat(responseEntityCreateConcert.getBody()).isNotNull();
        assertThat(responseEntityCreateConcert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateConcert.getBody().getTitle()).isEqualToIgnoringCase("TitleTestConcert1");
        Long newConcertId = responseEntityCreateConcert.getBody().getId();

        // create track = testTrack1 and aad to new concert = testConcert1
        HttpEntity<Track> httpCreateTrackEntity = new HttpEntity<>(testTrack1, httpHeaders);
        ResponseEntity<Track> responseEntityCreateTrack = testRestTemplate.postForEntity(createURLWithPort("api/concert/addtrack/" + newConcertId), httpCreateTrackEntity, Track.class);
        assertThat(responseEntityCreateTrack.getBody()).isNotNull();
        assertThat(responseEntityCreateTrack.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateTrack.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTrack1");
        Long newTrackId = responseEntityCreateTrack.getBody().getId();

        // test read
        ResponseEntity<Track> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newTrackId), Track.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newTrackId);

        // test update
        testTrack1.setTitle("Updated TrackTitle");
        HttpEntity<Track> httpEntityUpdateOne = new HttpEntity<>(testTrack1, httpHeaders);
        ResponseEntity<Track> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newTrackId), HttpMethod.PUT, httpEntityUpdateOne, Track.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(newTrackId);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated tracktitle");

        // test delete testTrack and delete testConcert + testTour
        HttpEntity<Track> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Track> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newTrackId), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(newTrackId);

            // delete testConcert + testTour
        HttpEntity<Concert> httpEntityDeleteOneConcertById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Concert> responseEntityDeleteOneConcertById = testRestTemplate.exchange(createURLWithPort("api/concert/" + newConcertId), HttpMethod.DELETE, httpEntityDeleteOneConcertById, Concert.class);
        assertThat(responseEntityDeleteOneConcertById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneConcertById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneConcertById.getBody().getId()).isEqualTo(newConcertId);

        HttpEntity<Tour> httpEntityDeleteOneTourById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Tour> responseEntityDeleteOneTourById = testRestTemplate.exchange(createURLWithPort("api/tour/" + newTourId), HttpMethod.DELETE, httpEntityDeleteOneTourById, Tour.class);
        assertThat(responseEntityDeleteOneTourById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneTourById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneTourById.getBody().getId()).isEqualTo(newTourId);
    }

    @Test
    public void additionalFindTests() {
        // test findall
        ResponseEntity<Iterable> iterableResponseEntityFindAll = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        assertResponse(iterableResponseEntityFindAll, HttpStatus.OK, 1);

        // test findByTitleLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/ack4"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 1);
    }

    // helper methods
    private String createURLWithPort(String uri) {
        String uriString = "http://localhost:" + port + uri;
        System.out.println(uriString);
        return uriString;
    }

    private void assertResponse(ResponseEntity<Iterable> iterableResponseEntity, HttpStatus httpStatus, int returnResult) {
        assertThat(iterableResponseEntity.getStatusCode()).isEqualTo(httpStatus);
        assertThat(iterableResponseEntity.getBody()).asList().size().isGreaterThanOrEqualTo(returnResult);
    }
}
