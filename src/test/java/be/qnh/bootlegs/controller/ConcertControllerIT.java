package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
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

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @Test
    public void crudTests() {
        // een concert kan alleen worden toegevoegd aan een bestaande tour
        Tour testTour1 = new Tour();
        testTour1.setTitle("TitleTestTour1");
        testTour1.setLeg(1);
        testTour1.setStartyear(2018);
        testTour1.setEndyear(2018);
        testTour1.setContinent(Continent.NORTHAMERICA);

        Concert testconcert1 = new Concert();
        testconcert1.setTitle("TitleTestConcert1");
        testconcert1.setCity("CityTestConcert1");
        testconcert1.setCountry("USA");
        testconcert1.setDate(LocalDate.of(2018, 4, 11));

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
        HttpEntity<Concert> httpCreateConcertEntity = new HttpEntity<>(testconcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityCreateConcert = testRestTemplate.postForEntity(createURLWithPort("api/tour/addconcerttotour/" + newTourId), httpCreateConcertEntity, Concert.class);
        assertThat(responseEntityCreateConcert.getBody()).isNotNull();
        assertThat(responseEntityCreateConcert.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreateConcert.getBody().getTitle()).isEqualToIgnoringCase("TitleTestConcert1");
        Long newConcertId = responseEntityCreateConcert.getBody().getId();

        // test read
        ResponseEntity<Concert> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newConcertId), Concert.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newConcertId);

        // test update
        testconcert1.setTitle("Updated ConcertTitle");
        HttpEntity<Concert> httpEntityUpdateOne = new HttpEntity<>(testconcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newConcertId), HttpMethod.PUT, httpEntityUpdateOne, Concert.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(newConcertId);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated concerttitle");

        // test delete + tour delete
        HttpEntity<Concert> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Concert> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newConcertId), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(newConcertId);

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
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/title 1"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 1);


        // test findByDateEquals
        ResponseEntity<Iterable> iterableResponseEntityfindByDateEquals = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/finddate/30-10-2004"), Iterable.class);
        assertResponse(iterableResponseEntityfindByDateEquals, HttpStatus.OK, 1);


        // test findByCountryLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByCountryLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcountry/netherl"), Iterable.class);
        assertResponse(iterableResponseEntityfindByCountryLikeIgnoreCase, HttpStatus.OK, 1);


        // test findByCityLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByCityLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcity/vegas"), Iterable.class);
        assertResponse(iterableResponseEntityfindByCityLikeIgnoreCase, HttpStatus.OK, 2);

        // test findByRecordingQuality
        ResponseEntity<Iterable> iterableResponseEntityfindByRecordingQuality = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findquality/FAIR"), Iterable.class);
        assertResponse(iterableResponseEntityfindByRecordingQuality, HttpStatus.OK, 2);
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
