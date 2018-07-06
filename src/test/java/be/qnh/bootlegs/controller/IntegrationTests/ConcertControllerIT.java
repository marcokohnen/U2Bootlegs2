package be.qnh.bootlegs.controller.IntegrationTests;

import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Tour;
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
public class ConcertControllerIT {

    private static final String BASE_URI = "/api/concert";
    private TestRestTemplate testRestTemplate;
    private HttpHeaders httpHeaders;

    // test objects
    private Tour testTour;
    private Concert testConcert1, testConcert2, testConcert3;
    private Long tourId, concertId1, concertId2, concertId3;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        System.out.println("Entered @Before : init");
        //om integratie-testen te kunnen doen met spring security geven we geldige credentials van een test-user met 'ROLE_ADMIN' mee met de testRestTemplate, die bij elke test worden gebruikt. Deze test-user moet wel al bestaan in de database !!!
        testRestTemplate = new TestRestTemplate().withBasicAuth("testadmin@test.com", "AdminWachtwoord");
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        // een concert kan alleen worden toegevoegd aan een bestaande tour
        testTour = new Tour();
        testTour.setTitle("TitleTestTour1");
        testTour.setLeg(1);
        testTour.setStartyear(2018);
        testTour.setEndyear(2018);
        testTour.setContinent(Continent.NORTHAMERICA);

        // create tour = testTour in database
        HttpEntity<Tour> httpCreateTourEntity = new HttpEntity<>(testTour, httpHeaders);
        ResponseEntity<Tour> responseEntityCreateTour = testRestTemplate.postForEntity(createURLWithPort("/api/tour/"), httpCreateTourEntity, Tour.class);
        assertThat(responseEntityCreateTour.getBody()).isNotNull();
        assertThat(responseEntityCreateTour.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateTour.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTour1");
        tourId = responseEntityCreateTour.getBody().getId();
    }

    @After
    public void deleteTestTourInDatabase() {
        System.out.println("Entered @After : deleteTestTourInDatabase");
        // delete testTour from database
        HttpEntity<Tour> httpEntityDeleteOneTourById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Tour> responseEntityDeleteOneTourById = testRestTemplate.exchange(createURLWithPort("/api/tour/" + tourId), HttpMethod.DELETE, httpEntityDeleteOneTourById, Tour.class);
        assertThat(responseEntityDeleteOneTourById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneTourById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneTourById.getBody().getId()).isEqualTo(tourId);

        // delete testTour-object
        testTour = null;
    }

    @Test
    public void crudTests() {
        // create test object
        testConcert1 = new Concert();
        testConcert1.setTitle("TitleTestConcert1");
        testConcert1.setCity("CityTestConcert1");
        testConcert1.setCountry("USA");
        testConcert1.setDate(LocalDate.of(2018, 4, 11));
        testConcert1.setQuality(RecordingQuality.FAIR);

        // create concert = testConcert1 and add to new tour = testTour
        HttpEntity<Concert> httpCreateConcertEntity = new HttpEntity<>(testConcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityCreateConcert = testRestTemplate.postForEntity(createURLWithPort("api/tour/addconcerttotour/" + tourId), httpCreateConcertEntity, Concert.class);
        assertThat(responseEntityCreateConcert.getBody()).isNotNull();
        assertThat(responseEntityCreateConcert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateConcert.getBody().getTitle()).isEqualToIgnoringCase("TitleTestConcert1");
        concertId1 = responseEntityCreateConcert.getBody().getId();

        // test read
        ResponseEntity<Concert> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + concertId1), Concert.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(concertId1);

        // test update
        testConcert1.setTitle("Updated ConcertTitle");
        HttpEntity<Concert> httpEntityUpdateOne = new HttpEntity<>(testConcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + concertId1), HttpMethod.PUT, httpEntityUpdateOne, Concert.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(concertId1);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated concerttitle");

        // test delete
        HttpEntity<Concert> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Concert> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + concertId1), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(concertId1);

        // delete test object
        testConcert1 = null;
    }

    @Test
    public void additionalFindTests() {
        // creating test objects
        testConcert1 = new Concert();
        testConcert1.setTitle("TitleTestConcert1");
        testConcert1.setCity("New York");
        testConcert1.setCountry("USA");
        testConcert1.setDate(LocalDate.of(2018, 4, 1));
        testConcert1.setQuality(RecordingQuality.FAIR);

        testConcert2 = new Concert();
        testConcert2.setTitle("TitleTestConcert2");
        testConcert2.setCity("Antwerp");
        testConcert2.setCountry("Belgium");
        testConcert2.setDate(LocalDate.of(2018, 4, 2));
        testConcert2.setQuality(RecordingQuality.GOOD);

        testConcert3 = new Concert();
        testConcert3.setTitle("TitleTestConcert3");
        testConcert3.setCity("Cologne");
        testConcert3.setCountry("Germany");
        testConcert3.setDate(LocalDate.of(2018, 4, 3));
        testConcert3.setQuality(RecordingQuality.EXCELLENT);

        // adding testConcerts to testTour in database

        HttpEntity<Concert> httpCreateEntity = new HttpEntity<>(testConcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/tour/addconcerttotour/" + tourId), httpCreateEntity, Concert.class);
        if (responseEntityCreate.getBody() != null) {
            concertId1 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testConcert2, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/tour/addconcerttotour/" + tourId), httpCreateEntity, Concert.class);
        if (responseEntityCreate.getBody() != null) {
            concertId2 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testConcert3, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort("/api/tour/addconcerttotour/" + tourId), httpCreateEntity, Concert.class);
        if (responseEntityCreate.getBody() != null) {
            concertId3 = responseEntityCreate.getBody().getId();
        }

        // test findall
        ResponseEntity<Iterable> iterableResponseEntityFindAll = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        assertResponse(iterableResponseEntityFindAll, HttpStatus.OK, 1);

        // test findByTitleLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/concert2"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 1);

        // test findByDateEquals
        ResponseEntity<Iterable> iterableResponseEntityfindByDateEquals = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/finddate/03-04-2018"), Iterable.class);
        assertResponse(iterableResponseEntityfindByDateEquals, HttpStatus.OK, 1);


        // test findByCountryLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByCountryLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcountry/usa"), Iterable.class);
        assertResponse(iterableResponseEntityfindByCountryLikeIgnoreCase, HttpStatus.OK, 1);


        // test findByCityLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByCityLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcity/new york"), Iterable.class);
        assertResponse(iterableResponseEntityfindByCityLikeIgnoreCase, HttpStatus.OK, 1);

        // test findByRecordingQuality
        ResponseEntity<Iterable> iterableResponseEntityfindByRecordingQuality = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findquality/FAIR"), Iterable.class);
        assertResponse(iterableResponseEntityfindByRecordingQuality, HttpStatus.OK, 1);

        // deleting test-objects(testConcert1, testConcert2, testConcert3) from database

        HttpEntity<Concert> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);

        ResponseEntity<Concert> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + concertId1), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + concertId2), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + concertId3), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        //deleting test-objects
        testConcert1 = null;
        testConcert2 = null;
        testConcert3 = null;
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
