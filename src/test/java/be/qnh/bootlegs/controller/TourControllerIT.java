package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.BootlegsApplication;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = BootlegsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class TourControllerIT {

    private static final String BASE_URI = "/api/tour";
    private TestRestTemplate testRestTemplate;
    private HttpHeaders httpHeaders;

    // test objects
    private Tour testTour1, testTour2, testTour3;
    private Long tourId1, tourId2, tourId3;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @Test
    public void crudTests() {
        // creating test object
        testTour1 = new Tour();
        testTour1.setTitle("New Boy");
        testTour1.setStartyear(1983);
        testTour1.setEndyear(1984);
        testTour1.setLeg(3);
        testTour1.setContinent(Continent.EUROPE);

        // test create
        HttpEntity<Tour> httpCreateEntity = new HttpEntity<>(testTour1, httpHeaders);
        ResponseEntity<Tour> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Tour.class);
        assertThat(responseEntityCreate.getBody()).isNotNull();
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreate.getBody().getTitle()).isEqualToIgnoringCase("new boy");
        Long newId = responseEntityCreate.getBody().getId();

        // test read
        ResponseEntity<Tour> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newId), Tour.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newId);

        // test update
        testTour1.setTitle("Updated TourTitle");
        HttpEntity<Tour> httpEntityUpdateOne = new HttpEntity<>(testTour1, httpHeaders);
        ResponseEntity<Tour> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.PUT, httpEntityUpdateOne, Tour.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(newId);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated tourtitle");

        // test delete
        HttpEntity<Tour> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Tour> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.DELETE, httpEntityDeleteOneById, Tour.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(newId);

        // delete test object
        testTour1 = null;
    }

    @Test
    public void additionalFindTests() {
        // creating test objects
        testTour1 = new Tour();
        testTour1.setTitle("New Boy");
        testTour1.setStartyear(1983);
        testTour1.setEndyear(1984);
        testTour1.setLeg(3);
        testTour1.setContinent(Continent.EUROPE);

        testTour2 = new Tour();
        testTour2.setTitle("October");
        testTour2.setStartyear(1985);
        testTour2.setEndyear(1985);
        testTour2.setLeg(2);
        testTour2.setContinent(Continent.NORTHAMERICA);

        testTour3 = new Tour();
        testTour3.setTitle("Joshua Tree");
        testTour3.setStartyear(1987);
        testTour3.setEndyear(1988);
        testTour3.setLeg(1);
        testTour3.setContinent(Continent.AUSTRALIA);

        HttpEntity<Tour> httpCreateEntity = new HttpEntity<>(testTour1, httpHeaders);
        ResponseEntity<Tour> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Tour.class);
        if (responseEntityCreate.getBody() != null) {
            tourId1 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testTour2, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Tour.class);
        if (responseEntityCreate.getBody() != null) {
            tourId2 = responseEntityCreate.getBody().getId();
        }

        httpCreateEntity = new HttpEntity<>(testTour3, httpHeaders);
        responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Tour.class);
        if (responseEntityCreate.getBody() != null) {
            tourId3 = responseEntityCreate.getBody().getId();
        }

        // test findall
        ResponseEntity<Iterable> iterableResponseEntityFindAll = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        assertResponse(iterableResponseEntityFindAll, HttpStatus.OK, 1);

        // test findByTitleLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/tree"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 1);

        // findByStartyearGreaterThanEqual
        ResponseEntity<Iterable> iterableResponseEntityStartYearGreaterThanEqual = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findfromyear/" + 1985), Iterable.class);
        assertResponse(iterableResponseEntityStartYearGreaterThanEqual, HttpStatus.OK, 2);

        // test findByStartyearEquals
        ResponseEntity<Iterable> iterableResponseEntityfindByStartyearEquals = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findyear/1987"), Iterable.class);
        assertResponse(iterableResponseEntityfindByStartyearEquals, HttpStatus.OK, 1);

        // test findByContinentEquals
        ResponseEntity<Iterable> iterableResponseEntityfindByContinentEquals = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcontinent/EUROPE"), Iterable.class);
        assertResponse(iterableResponseEntityfindByContinentEquals, HttpStatus.OK, 1);

        // deleting test objects
        HttpEntity<Tour> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Tour> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + tourId1), HttpMethod.DELETE, httpEntityDeleteOneById, Tour.class);
        assertThat(responseEntityDeleteOneById).isNotNull();
        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + tourId2), HttpMethod.DELETE, httpEntityDeleteOneById, Tour.class);
        assertThat(responseEntityDeleteOneById).isNotNull();
        responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + tourId3), HttpMethod.DELETE, httpEntityDeleteOneById, Tour.class);
        assertThat(responseEntityDeleteOneById).isNotNull();

        testTour1 = null;
        testTour2 = null;
        testTour3 = null;
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
