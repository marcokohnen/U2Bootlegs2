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

    private Tour testTour1;
    private Tour testTour2;
    private Tour testTour3;

    @LocalServerPort
    private int port;

    @Before
    public void init() {
        testRestTemplate = new TestRestTemplate();
        httpHeaders = new HttpHeaders();

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
        HttpEntity<Tour> httpCreateEntity = new HttpEntity<>(testTour1, httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Tour> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Tour.class );
        assertThat(responseEntityCreate.getBody()).isNotNull();
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreate.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTour1");
        Long newId = responseEntityCreate.getBody().getId();

        // test read
        ResponseEntity<Tour> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newId), Tour.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newId);

        // test update
        testTour2.setTitle("Updated TourTitle");
        HttpEntity<Tour> httpEntityUpdateOne = new HttpEntity<>(testTour2, httpHeaders);
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
    }

    // helper methods
    private String createURLWithPort(String uri) {
        String uriString = "http://localhost:" + port + uri;
        System.out.println(uriString);
        return uriString;
    }
}
