package com.ylab.app.test.controllers;

import com.ylab.app.in.controllers.MeterController;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDetailsDto;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.service.impl.MeterServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MeterControllerTest class
 *
 * @author razlivinsky
 * @since 18.02.2024
 */
@SpringBootTest
public class MeterControllerTest {
    @InjectMocks
    private MeterController meterController;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MeterServiceImpl meterService;

    private MeterReadingDto meterReadingDto1;
    private MeterReadingDto meterReadingDto2;
    private List<MeterReadingDto> meterReadingDtoList;
    private MeterReadingDetailsDto meterReadingDetailsDto1;
    private MeterReadingDetailsDto meterReadingDetailsDto2;
    private List<MeterReadingDetailsDto> meterReadingDetailsDtoList;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(meterController).build();

        meterReadingDetailsDto1 = new MeterReadingDetailsDto();
        meterReadingDetailsDto1.setType("Electricity");
        meterReadingDetailsDto1.setValue(100.0);
        meterReadingDetailsDto2 = new MeterReadingDetailsDto();
        meterReadingDetailsDto2.setType("Gas");
        meterReadingDetailsDto2.setValue(200.7);
        meterReadingDetailsDtoList = Arrays.asList(meterReadingDetailsDto1, meterReadingDetailsDto2);

        meterReadingDto1 = new MeterReadingDto();
        meterReadingDto1.setNumberMeter("123456789");
        meterReadingDto1.setDetailsList(meterReadingDetailsDtoList);
        meterReadingDto2 = new MeterReadingDto();
        meterReadingDto2.setNumberMeter("987654321");
        meterReadingDto2.setDetailsList(meterReadingDetailsDtoList);
        meterReadingDtoList = Arrays.asList(meterReadingDto1, meterReadingDto2);
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    @DisplayName("getCurrentMeterReadings returns a list of current meter readings for the authenticated user when successful")
    public void testGetCurrentMeterReadings() throws Exception {
        when(meterService.getCurrentReadings(any(User.class))).thenReturn(meterReadingDtoList);

        mockMvc.perform(get("/meter-readings/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    @DisplayName("submitMeterReading creates a new meter reading for the authenticated user when successful")
    public void testSubmitMeterReading() throws Exception {
        when(meterService.submitReading(any(User.class), anyString(),
                any(List.class))).thenReturn(meterReadingDto1);

        mockMvc.perform(post("/meter-readings/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numberMeter\":\"123456789\",\"detailsList\":" +
                                "[{\"meterType\":\"Electricity\",\"reading\":100},{\"meterType\":\"Gas\",\"reading\":200}]}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    @DisplayName("getMeterReadingHistory returns a list of meter reading history for the authenticated user when successful")
    public void testGetMeterReadingHistory() throws Exception {
        when(meterService.getReadingsHistory(any(User.class))).thenReturn(meterReadingDtoList);

        mockMvc.perform(get("/meter-readings/history"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    @DisplayName("testGetMeterReadingByMonth returns a list of meter reading by month for the authenticated user when successful")
    public void testGetMeterReadingByMonth() throws Exception {
        Mockito.when(meterService.getReadingsByMonth(any(User.class), anyInt())).thenReturn(meterReadingDtoList);

        mockMvc.perform(get("/meter-readings/month/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "ADMIN")
    @DisplayName("getMeterReadingHistory returns a list of meter reading all history for the authenticated user when successful")
    public void testGetMeterReadingAllHistory() throws Exception {
        Mockito.when(meterService.getAllReadingsHistory(any(User.class))).thenReturn(meterReadingDtoList);

        mockMvc.perform(get("/meter-readings/history/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}


