package be.qnh.bootlegs.controller.IntegrationTests;


import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootlegsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TrackControllerIT {

    private static final String BASE_URI = "/api/track";
    private TestRestTemplate testRestTemplate;
    private HttpHeaders httpHeaders;

    // test objects
    private Tour testTour;
    private Concert testConcert;
    private Track testTrack1, testTrack2, testTrack3;
    private Long tourId, concertId, trackId1, trackId2, trackId3;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // een track kan alleen worden toegevoegd aan een bestaand concert en een concert kan alleen worden toegevoegd aan een bestaande tour
        testTour = new Tour();
        testTour.setTitle("TitleTestTour1");
        testTour.setLeg(1);
        testTour.setStartyear(2018);
        testTour.setEndyear(2018);
        testTour.setContinent(Continent.NORTHAMERICA);

        testConcert = new Concert();
        testConcert.setTitle("TitleTestConcert1");
        testConcert.setCity("CityTestConcert1");
        testConcert.setCountry("USA");
        testConcert.setDate(LocalDate.of(2018, 4, 11));
        testConcert.setQuality(RecordingQuality.FAIR);

        // create tour = testTour in database
        HttpEntity<Tour> httpCreateTourEntity = new HttpEntity<>(testTour, httpHeaders);
        ResponseEntity<Tour> responseEntityCreateTour = testRestTemplate.postForEntity(createURLWithPort("/api/tour/"), httpCreateTourEntity, Tour.class);
        assertThat(responseEntityCreateTour.getBody()).isNotNull();
        assertThat(responseEntityCreateTour.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateTour.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTour1");
        tourId = responseEntityCreateTour.getBody().getId();

        // add testConcert to testTour in database
        HttpEntity<Concert> httpCreateConcertEntity = new HttpEntity<>(testConcert, httpHeaders);
        ResponseEntity<Concert> responseEntityCreateConcert = testRestTemplate.postForEntity(createURLWithPort("/api/tour/addconcerttotour/" + tourId), httpCreateConcertEntity, Concert.class);
        assertThat(responseEntityCreateConcert.getBody()).isNotNull();
        assertThat(responseEntityCreateConcert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateConcert.getBody().getTitle()).isEqualToIgnoringCase("TitleTestConcert1");
        concertId = responseEntityCreateConcert.getBody().getId();
    }

    @After
    public void deleteTestTourAndConcertInDatabase() {
        // delete testConcert + testTour
        HttpEntity<Concert> httpEntityDeleteOneConcertById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Concert> responseEntityDeleteOneConcertById = testRestTemplate.exchange(createURLWithPort("/api/concert/" + concertId), HttpMethod.DELETE, httpEntityDeleteOneConcertById, Concert.class);
        assertThat(responseEntityDeleteOneConcertById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneConcertById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneConcertById.getBody().getId()).isEqualTo(concertId);

        HttpEntity<Tour> httpEntityDeleteOneTourById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Tour> responseEntityDeleteOneTourById = testRestTemplate.exchange(createURLWithPort("/api/tour/" + tourId), HttpMethod.DELETE, httpEntityDeleteOneTourById, Tour.class);
        assertThat(responseEntityDeleteOneTourById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneTourById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneTourById.getBody().getId()).isEqualTo(tourId);

        // delete test-objects
        testConcert = null;
        testTour = null;
    }

    @Test
    public void crudTests() {
        // create test object
        Track testTrack1 = new Track();
        testTrack1.setTitle("TitleTestTrack1");
        testTrack1.setSequenceNr(8);
        testTrack1.setLocationUrl("URLTestTrack1");

        // create track = testTrack1 and add to new concert = testConcert
        HttpEntity<Track> httpCreateTrackEntity = new HttpEntity<>(testTrack1, httpHeaders);
        ResponseEntity<Track> responseEntityCreateTrack = testRestTemplate.postForEntity(createURLWithPort("/api/concert/addtracktoconcert/" + concertId), httpCreateTrackEntity, Track.class);
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

        // test delete testTrack
        HttpEntity<Track> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Track> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newTrackId), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(newTrackId);

        // delete test object
        testTrack1 = null;
    }

    @Test
    public void additionalFindTests() {
        // creating test objects
        testTrack1 = new Track();
        testTrack1.setSequenceNr(10);
        testTrack1.setTitle("TestTrackTitle1");
        testTrack1.setLocationUrl("TestTrackUrl1");

        testTrack2 = new Track();
        testTrack2.setSequenceNr(11);
        testTrack2.setTitle("TestTrackTitle2");
        testTrack2.setLocationUrl("TestTrackUrl2");

        testTrack3 = new Track();
        testTrack3.setSequenceNr(12);
        testTrack3.setTitle("TestTrackTitle3");
        testTrack3.setLocationUrl("TestTrackUrl3");

        // adding testTracks to testConcert in database

        HttpEntity<Track> httpCreateEntity = new HttpEntity<>(testTrack1, httpHeaders);
        ResponseEntity<Track> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/concert/addtracktoconcert/" + concertId), httpCreateEntity, Track.class);
        if (responseEntityCreate.getBody() != null) {
            trackId1 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testTrack2, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/concert/addtracktoconcert/" + concertId), httpCreateEntity, Track.class);
        if (responseEntityCreate.getBody() != null) {
            trackId2 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testTrack3, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/concert/addtracktoconcert/" + concertId), httpCreateEntity, Track.class);
        if (responseEntityCreate.getBody() != null) {
            trackId3 = responseEntityCreate.getBody().getId();
        }

        // test findall
        ResponseEntity<Iterable> iterableResponseEntityFindAll = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        assertResponse(iterableResponseEntityFindAll, HttpStatus.OK, 3);

        // test findByTitleLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/title3"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 1);

        // deleting test-objects(testTrack1, testTrack2, testTrack3) from database
        HttpEntity<Track> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);

        ResponseEntity<Track> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + trackId1), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + trackId2), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + trackId3), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        // deleting test-objects
        testTrack1 = null;
        testTrack2 = null;
        testTrack3 = null;
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
