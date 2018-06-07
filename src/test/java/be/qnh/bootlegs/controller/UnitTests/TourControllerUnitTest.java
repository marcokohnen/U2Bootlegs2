package be.qnh.bootlegs.controller.UnitTests;

import be.qnh.bootlegs.controller.TourController;
import be.qnh.bootlegs.domain.Concert;
import be.qnh.bootlegs.domain.Continent;
import be.qnh.bootlegs.domain.RecordingQuality;
import be.qnh.bootlegs.domain.Tour;
import be.qnh.bootlegs.service.TourService;
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
@WebMvcTest(TourController.class)
public class TourControllerUnitTest {

    private static final String BASE_URI = "/api/tour";

    private Tour testTour1, testTour2, testTour3;
    private Concert testConcert;
    private List<Tour> tours;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourService tourService;

    @Before
    public void init() {
        // create test-objects
        testTour1 = new Tour();
        testTour1.setId(101L);
        testTour1.setTitle("TestTourTitle1");
        testTour1.setLeg(2);
        testTour1.setStartyear(1987);
        testTour1.setEndyear(1987);
        testTour1.setContinent(Continent.EUROPE);

        testTour2 = new Tour();
        testTour2.setId(102L);
        testTour2.setTitle("TestTourTitle2");
        testTour2.setLeg(4);
        testTour2.setStartyear(2002);
        testTour2.setEndyear(2003);
        testTour2.setContinent(Continent.NEWZEALAND);

        testTour3 = new Tour();
        testTour3.setId(103L);
        testTour3.setTitle("TestTourTitle3");
        testTour3.setLeg(5);
        testTour3.setStartyear(2005);
        testTour3.setEndyear(2006);
        testTour3.setContinent(Continent.NORTHAMERICA);

        tours = new ArrayList<>();
        tours.addAll(Arrays.asList(testTour1, testTour2, testTour3));

        testConcert = new Concert();
        testConcert.setId(10L);
        testConcert.setDate(LocalDate.of(2018, 6, 5));
        testConcert.setTitle("testConcert");
        testConcert.setCity("Hasselt");
        testConcert.setCountry("Belgium");
        testConcert.setQuality(RecordingQuality.GOOD);
    }

    /*
     * converts a Java object into JSON representation
     */
    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindAll() throws Exception {
        when(tourService.findAll()).thenReturn(tours);

        mockMvc.perform(get(BASE_URI + "/findall"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title", is("TestTourTitle1")))
                .andExpect(jsonPath("$[1].startyear", is(2002)))
                .andExpect(jsonPath("$[2].leg", is(5)))
                .andDo(print());

        verify(tourService, times(1)).findAll();
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testFindOneById() throws Exception {
        when(tourService.findOneById(3L)).thenReturn(testTour3);
        mockMvc.perform(get(BASE_URI + "/findid/{id}", 3))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(103)))
                .andExpect(jsonPath("$.endyear", is(2006)))
                .andDo(print());

        verify(tourService, times(1)).findOneById(3L);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testFindByTitleLikeIgnoreCase() throws Exception {
        when(tourService.findByTitleLikeIgnoreCase("title2")).thenReturn(Collections.singletonList(testTour2));

        mockMvc.perform(get(BASE_URI + "/findtitle/{title}", "title2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$[0].id", is(102)))
                .andExpect(jsonPath("$[0].endyear", is(2003)));

        verify(tourService, times(1)).findByTitleLikeIgnoreCase("title2");
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testFindByStartyearGreaterThanEqual() throws Exception {
        when(tourService.findByStartyearGreaterThanEqual(2002)).thenReturn(Arrays.asList(testTour2, testTour3));

        mockMvc.perform(get(BASE_URI + "/findfromyear/{startYear}", 2002))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(102)))
                .andExpect(jsonPath("$[1].id", is(103)));

        verify(tourService, times(1)).findByStartyearGreaterThanEqual(2002);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testFindByStartyearEquals() throws Exception {
        when(tourService.findByStartyearEquals(1987)).thenReturn(Collections.singletonList(testTour1));

        mockMvc.perform(get(BASE_URI + "/findyear/{startYear}", 1987))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(101)));

        verify(tourService, times(1)).findByStartyearEquals(1987);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testFindByContinentEquals() throws Exception {
        when(tourService.findByContinentEquals(Continent.NEWZEALAND)).thenReturn(Collections.singletonList(testTour2));

        mockMvc.perform(get(BASE_URI + "/findcontinent/{continent}", "NEWZEALAND"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(102)));

        verify(tourService, times(1)).findByContinentEquals(Continent.NEWZEALAND);
        verifyNoMoreInteractions(tourService);

    }

    @Test
    public void testAddOne() throws Exception {
        when(tourService.addOne(testTour1)).thenReturn(testTour1);

        mockMvc.perform(
                post(BASE_URI)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(testTour1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("TestTourTitle1")))
                .andDo(print());

        verify(tourService, times(1)).addOne(testTour1);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testAddConcertToTour() throws Exception {
        when(tourService.addConcertToTour(1L, testConcert)).thenReturn(testConcert);

        mockMvc.perform(post(BASE_URI + "/addconcerttotour/{tourId}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(testConcert))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andDo(print());

        verify(tourService, times(1)).addConcertToTour(1L, testConcert);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testUpdateOne() throws Exception {
        when(tourService.udpdateOneById(101L, testTour1)).thenReturn(testTour2);

        mockMvc.perform(
                put(BASE_URI + "/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(asJsonString(testTour1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("TestTourTitle2")))
                .andDo(print());

        verify(tourService, times(1)).udpdateOneById(101L, testTour1);
        verifyNoMoreInteractions(tourService);
    }

    @Test
    public void testDeleteOne() throws Exception {
        when(tourService.deleteOneById(103L)).thenReturn(testTour3);

        mockMvc.perform(
                delete(BASE_URI + "/{id}", 103L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(103)));

        verify(tourService, times(1)).deleteOneById(103L);
        verifyNoMoreInteractions(tourService);
    }
}