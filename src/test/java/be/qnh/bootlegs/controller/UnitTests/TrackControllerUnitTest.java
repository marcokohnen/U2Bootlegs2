package be.qnh.bootlegs.controller.UnitTests;

import be.qnh.bootlegs.controller.TrackController;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.service.TrackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// see --> https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testcontext-framework (3.6.1. Server-Side Tests)
public class TrackControllerUnitTest {

    private static final String BASE_URI = "/api/track";

    private Track testTrack1, testTrack2, testTrack3;
    private List<Track> tracks;

    private MockMvc mockMvc;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private TrackController trackController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(trackController) // for testing only one controller
                .build();


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
    public void testFindOneById() throws Exception {
        when(trackService.findOneById(3L)).thenReturn(testTrack3);

        mockMvc.perform(get(BASE_URI + "/findid/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.sequenceNr", is(3)));

        verify(trackService, times(1)).findOneById(3L);
        verifyNoMoreInteractions(trackService);
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() throws Exception {
        when(trackService.findByTitleLikeIgnoreCase("title1")).thenReturn(Collections.singletonList(testTrack1));

        mockMvc.perform(get(BASE_URI + "/findtitle/{title}", "title1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("TestTrackTitle1")));

        verify(trackService, times(1)).findByTitleLikeIgnoreCase("title1");
        verifyNoMoreInteractions(trackService);
    }

    @Test
    public void TestAddOne() throws Exception {
        when(trackService.addOne(testTrack2)).thenReturn(testTrack2);

        mockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(testTrack2)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.locationUrl", is("url2")));

        verify(trackService, times(1)).addOne(testTrack2);
        verifyNoMoreInteractions(trackService);
    }

    @Test
    public void TestUpdateOne() throws Exception {
        when(trackService.updateOneById(2L, testTrack2)).thenReturn(testTrack3);

        mockMvc.perform(put(BASE_URI + "/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(testTrack2))) // = RequestBody in controller
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)));

        verify(trackService, times(1)).updateOneById(2L, testTrack2);
        verifyNoMoreInteractions(trackService);

    }

    @Test
    public void TestDeleteOne() throws Exception {
        when(trackService.deleteOneById(3L)).thenReturn(testTrack3);

        mockMvc.perform(delete(BASE_URI + "/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(3)));

        verify(trackService, times(1)).deleteOneById(3L);
        verifyNoMoreInteractions(trackService);
    }
}