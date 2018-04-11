package be.qnh.bootlegs.controller;

import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.Tour;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

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
        Concert testconcert1 = new Concert();
        testconcert1.setTitle("TitleTestConcert1");
        testconcert1.setCity("CityTestConcert1");
        testconcert1.setCountry("USA");
        testconcert1.setDate(LocalDate.of(2018, 4, 11));
        // test create
        HttpEntity<Concert> httpCreateEntity = new HttpEntity<>(testconcert1, httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Concert> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Concert.class);
        assertThat(responseEntityCreate.getBody()).isNotNull();
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreate.getBody().getTitle()).isEqualToIgnoringCase("TitleTestConcert1");
        Long newId = responseEntityCreate.getBody().getId();

        // test read
        ResponseEntity<Concert> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newId), Concert.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newId);

        // test update
        testconcert1.setTitle("Updated ConcertTitle");
        HttpEntity<Concert> httpEntityUpdateOne = new HttpEntity<>(testconcert1, httpHeaders);
        ResponseEntity<Concert> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.PUT, httpEntityUpdateOne, Concert.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(newId);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated concerttitle");

        // test delete
        HttpEntity<Concert> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Concert> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.DELETE, httpEntityDeleteOneById, Concert.class);
        assertThat(responseEntityDeleteOneById.getBody()).isNotNull();
        assertThat(responseEntityDeleteOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityDeleteOneById.getBody().getId()).isEqualTo(newId);
    }

    @Test
    public void additionalFindTests() {
        // test findall
        ResponseEntity<Iterable> iterableResponseEntityFindAll = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findall"), Iterable.class);
        assertResponse(iterableResponseEntityFindAll, HttpStatus.OK, 1);

        // test findByTitleLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByTitleLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findtitle/night"), Iterable.class);
        assertResponse(iterableResponseEntityfindByTitleLikeIgnoreCase, HttpStatus.OK, 2);


        // test findByDateEquals
        ResponseEntity<Iterable> iterableResponseEntityfindByDateEquals = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/finddate/30-10-1984"), Iterable.class);
        assertResponse(iterableResponseEntityfindByDateEquals, HttpStatus.OK, 2);


        // test findByCountryLikeIgnoreCase
        ResponseEntity<Iterable> iterableResponseEntityfindByCountryLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcountry/usa"), Iterable.class);
        assertResponse(iterableResponseEntityfindByCountryLikeIgnoreCase, HttpStatus.OK, 2);


    // test findByCityLikeIgnoreCase
    ResponseEntity<Iterable> iterableResponseEntityfindByCityLikeIgnoreCase = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findcity/rotterd"), Iterable.class);
    assertResponse(iterableResponseEntityfindByCityLikeIgnoreCase, HttpStatus.OK, 2);

        // test findByRecordingQuality
        ResponseEntity<Iterable> iterableResponseEntityfindByRecordingQuality = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findquality/FAIR"), Iterable.class);
        assertResponse(iterableResponseEntityfindByRecordingQuality, HttpStatus.OK, 1);
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
