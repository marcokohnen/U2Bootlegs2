package be.qnh.bootlegs.controller.UnitTests;

import be.qnh.bootlegs.controller.ConcertController;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Track;
import be.qnh.bootlegs.service.ConcertService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(value = SpringRunner.class)
@WebMvcTest(ConcertController.class)
public class ConcertControllerUnitTest {

    private static final String BASE_URI = "/api/concert";

    private Concert testConcert1, testConcert2, testConcert3;
    private Track testTrack;
    private List<Concert> concerts;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConcertService concertService;

    @Before
    public void init() {
        // create test-objects
        testConcert1 = new Concert();
        testConcert1.setId(1L);
        testConcert1.setTitle("TestConcertTitle1");
        testConcert1.setDate(LocalDate.of(2018, 6, 1));
        testConcert1.setCountry("USA");
        testConcert1.setCity("Boston");
        testConcert1.setQuality(RecordingQuality.FAIR);

        testConcert2 = new Concert();
        testConcert2.setId(2L);
        testConcert2.setTitle("TestConcertTitle2");
        testConcert2.setDate(LocalDate.of(2018, 6, 2));
        testConcert2.setCountry("Netherlands");
        testConcert2.setCity("Amsterdam");
        testConcert2.setQuality(RecordingQuality.FAIR);

        testConcert3 = new Concert();
        testConcert3.setId(3L);
        testConcert3.setTitle("TestConcertTitle3");
        testConcert3.setDate(LocalDate.of(2018, 6, 3));
        testConcert3.setCountry("Germany");
        testConcert3.setCity("Cologne");
        testConcert3.setQuality(RecordingQuality.GOOD);

        concerts = new ArrayList<>();
        concerts.addAll(Arrays.asList(testConcert1, testConcert2, testConcert3));

        testTrack = new Track();
        testTrack.setId(10L);
        testTrack.setSequenceNr(1);
        testTrack.setTitle("TrackTitle");
        testTrack.setLocationUrl("TrackUrl");
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
    public void testFfindAll() throws Exception {
        when(concertService.findAll()).thenReturn(concerts);

        mockMvc.perform(get(BASE_URI + "/findall"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].country", is("USA")))
                .andExpect(jsonPath("$[1].city", is("Amsterdam")))
                .andExpect(jsonPath("$[2].quality", is("GOOD")))
                .andDo(print());

        verify(concertService, times(1)).findAll();
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindOneById() throws Exception {
        when(concertService.findOneById(1L)).thenReturn(testConcert1);

        mockMvc.perform(get(BASE_URI + "/findid/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.city", is("Boston")));

        verify(concertService, times(1)).findOneById(1L);
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() throws Exception {
        when(concertService.findByTitleLikeIgnoreCase("title2")).thenReturn(Collections.singletonList(testConcert2));

        mockMvc.perform(get(BASE_URI + "/findtitle/{title}", "title2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].title", is("TestConcertTitle2")));

        verify(concertService, times(1)).findByTitleLikeIgnoreCase("title2");
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindByDateEquals() throws Exception {
        when(concertService.findByDateEquals(LocalDate.of(2018, 6, 1))).thenReturn(Collections.singletonList(testConcert1));

        mockMvc.perform(get(BASE_URI + "/finddate/{date}", "01-06-2018"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("TestConcertTitle1")))
                .andExpect(jsonPath("$[0].date", is(testConcert1.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))));

        verify(concertService, times(1)).findByDateEquals(LocalDate.of(2018, 6, 1));
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindByCountryLikeIgnoreCase() throws Exception {
        when(concertService.findByCountryLikeIgnoreCase("ger")).thenReturn(Collections.singletonList(testConcert3));

        mockMvc.perform(get(BASE_URI + "/findcountry/{country}", "ger"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(3)))
                .andExpect(jsonPath("$[0].title", is("TestConcertTitle3")));

        verify(concertService, times(1)).findByCountryLikeIgnoreCase("ger");
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindByCityLikeIgnoreCase() throws Exception {
        when(concertService.findByCityLikeIgnoreCase("sterdam")).thenReturn(Collections.singletonList(testConcert2));

        mockMvc.perform(get(BASE_URI + "/findcity/{city}", "sterdam"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].city", is("Amsterdam")));

        verify(concertService, times(1)).findByCityLikeIgnoreCase("sterdam");
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindByRecordingQuality() throws Exception {
        when(concertService.findByRecordingQuality(RecordingQuality.FAIR)).thenReturn(Arrays.asList(testConcert1, testConcert2));

        mockMvc.perform(get(BASE_URI + "/findquality/{recordingQuality}", "FAIR"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].quality", is("FAIR")));

        verify(concertService, times(1)).findByRecordingQuality(RecordingQuality.FAIR);
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testFindTourIdByConcertId() throws Exception {
        when(concertService.findTourIdByConcertId(2L)).thenReturn(25L);

        mockMvc.perform(get(BASE_URI + "/findtour/{concertId}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", is(25)));

        verify(concertService, times(1)).findTourIdByConcertId(2L);
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testAddOne() throws Exception {
        when(concertService.addOne(testConcert1)).thenReturn(testConcert1);

        mockMvc.perform
                (post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(testConcert1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("TestConcertTitle1")));

        verify(concertService, times(1)).addOne(testConcert1);
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testAddTrackToConcert() throws Exception {
        when(concertService.addTrackToConcert(3L, testTrack)).thenReturn(testTrack);

        mockMvc.perform(
                post(BASE_URI + "/addtracktoconcert/{concertid}", 3)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(testTrack)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.title", is("TrackTitle")));

        verify(concertService, times(1)).addTrackToConcert(3L, testTrack);
        verifyNoMoreInteractions(concertService);

    }

    @Test
    public void testUpdateOne() throws Exception {
        when(concertService.udpdateOneById(2L, testConcert2)).thenReturn(testConcert3);

        mockMvc.perform(
                put(BASE_URI + "/{id}", 2)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(testConcert2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("TestConcertTitle3")));

        verify(concertService, times(1)).udpdateOneById(2L, testConcert2);
        verifyNoMoreInteractions(concertService);
    }

    @Test
    public void testDeleteOne() throws Exception {
        when(concertService.deleteOneById(1L)).thenReturn(testConcert1);

        mockMvc.perform(
                delete(BASE_URI + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));


        verify(concertService, times(1)).deleteOneById(1L);
        verifyNoMoreInteractions(concertService);
    }
}