package be.qnh.bootlegs.controller.UnitTests;

import be.qnh.bootlegs.controller.TrackController;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.service.ConcertService;
import be.qnh.bootlegs.service.TrackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(value = SpringRunner.class)
@WebMvcTest(TrackController.class)
public class TrackControllerUnitTest {

    private static final String BASE_URI = "/api/track";

    private Track testTrack1, testTrack2, testTrack3;
    private List<Track> tracks;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrackService trackService;

    @Before
    public void setUp() {
        // create test-objects
        testTrack1 = new Track();
        testTrack1.setId(1L);
        testTrack1.setSequenceNr(1);
        testTrack1.setTitle("TestTrackTitle1");
        testTrack1.setLocationUrl("url1");

        testTrack2 = new Track();
        testTrack2.setId(2L);
        testTrack2.setSequenceNr(2);
        testTrack2.setTitle("TestTrackTitle2");
        testTrack2.setLocationUrl("url2");

        testTrack3 = new Track();
        testTrack3.setId(3L);
        testTrack3.setSequenceNr(3);
        testTrack3.setTitle("TestTrackTitle3");
        testTrack3.setLocationUrl("url3");

        tracks = Arrays.asList(testTrack1, testTrack2, testTrack3);
    }

    /*
     * converts a Java object into JSON representation
     */
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectToJsonMapper = new ObjectMapper();
            objectToJsonMapper.registerModule(new JavaTimeModule());
            objectToJsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectToJsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindAll() throws Exception {
        when(trackService.findAll()).thenReturn(tracks);

        mockMvc.perform(get(BASE_URI + "/findall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].sequenceNr", is(2)))
                .andExpect(jsonPath("$[2].locationUrl", is("url3")));

        verify(trackService, times(1)).findAll();
        verifyNoMoreInteractions(trackService);
    }

    @Test
    public void testFindOneById() {
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() {
    }

    @Test
    public void TestAddOne() {
    }

    @Test
    public void TestUpdateOne() {
    }

    @Test
    public void TestDeleteOne() {
    }
}