package be.qnh.bootlegs.controller;


import be.qnh.bootlegs.BootlegsApplication;
import be.qnh.bootlegs.domain.Track;
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
        Track testTrack1 = new Track();
        testTrack1.setTitle("TitleTestTrack1");
        testTrack1.setSequenceNr(8);
        testTrack1.setLocationUrl("URLTestTrack");
        // test create
        HttpEntity<Track> httpCreateEntity = new HttpEntity<>(testTrack1, httpHeaders);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<Track> responseEntityCreate = testRestTemplate.postForEntity(createURLWithPort(BASE_URI + "/"), httpCreateEntity, Track.class);
        assertThat(responseEntityCreate.getBody()).isNotNull();
        assertThat(responseEntityCreate.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntityCreate.getBody().getTitle()).isEqualToIgnoringCase("TitleTestTrack1");
        Long newId = responseEntityCreate.getBody().getId();

        // test read
        ResponseEntity<Track> responseEntityFindOneById = testRestTemplate.getForEntity(createURLWithPort(BASE_URI + "/findid/" + newId), Track.class);
        assertThat(responseEntityFindOneById.getBody()).isNotNull();
        assertThat(responseEntityFindOneById.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityFindOneById.getBody().getId()).isEqualTo(newId);

        // test update
        testTrack1.setTitle("Updated TrackTitle");
        HttpEntity<Track> httpEntityUpdateOne = new HttpEntity<>(testTrack1, httpHeaders);
        ResponseEntity<Track> responseEntityUpdateOne = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.PUT, httpEntityUpdateOne, Track.class);
        assertThat(responseEntityUpdateOne.getBody()).isNotNull();
        assertThat(responseEntityUpdateOne.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityUpdateOne.getBody().getId()).isEqualTo(newId);
        assertThat(responseEntityUpdateOne.getBody().getTitle()).isEqualToIgnoringCase("updated tracktitle");

        // test delete
        HttpEntity<Track> httpEntityDeleteOneById = new HttpEntity<>(httpHeaders);
        ResponseEntity<Track> responseEntityDeleteOneById = testRestTemplate.exchange(createURLWithPort(BASE_URI + "/" + newId), HttpMethod.DELETE, httpEntityDeleteOneById, Track.class);
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
