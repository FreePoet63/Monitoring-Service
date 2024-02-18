package com.ylab.app.test.controllers;

import com.ylab.app.in.controllers.MeterController;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.MeterServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.test.util.TestSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * MeterControllerTest class
 *
 * @author HP
 * @since 18.02.2024
 */
@ContextConfiguration(classes = { TestSecurityConfig.class })
@WebAppConfiguration
public class MeterControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private MeterServiceImpl meterService;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private MeterController meterController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get current meter readings")
    public void testGetCurrentMeterReadings() throws Exception {
        Mockito.when(userService.getUserByLogin("testUser")).thenReturn(new UserDto());
        Mockito.when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(new User());
        Mockito.when(meterService.getCurrentReadings(any(User.class))).thenReturn(Arrays.asList(new MeterReadingDto()));

        mockMvc.perform(get("/meter-readings/current")
                        .with(request -> {
                            request.setRemoteUser("testUser");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Submit meter reading")
    public void testSubmitMeterReading() throws Exception {
        Mockito.when(userService.getUserByLogin("testUser")).thenReturn(new UserDto());
        Mockito.when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(new User());
        Mockito.when(meterService.submitReading(any(User.class), anyString(), anyList())).thenReturn(new MeterReadingDto());

        mockMvc.perform(post("/meter-readings/submit")
                        .with(request -> {
                            request.setRemoteUser("testUser");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("your_meter_reading_dto_as_json_string"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Get meter reading history")
    public void testGetMeterReadingHistory() throws Exception {
        Mockito.when(userService.getUserByLogin("testUser")).thenReturn(new UserDto());
        Mockito.when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(new User());
        Mockito.when(meterService.getReadingsHistory(any(User.class))).thenReturn(Arrays.asList(new MeterReadingDto()));

        mockMvc.perform(get("/meter-readings/history")
                        .with(request -> {
                            request.setRemoteUser("testUser");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Get meter reading by month")
    public void testGetMeterReadingByMonth() throws Exception {
        Mockito.when(userService.getUserByLogin("testUser")).thenReturn(new UserDto());
        Mockito.when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(new User());
        Mockito.when(meterService.getReadingsByMonth(any(User.class), anyInt())).thenReturn(Arrays.asList(new MeterReadingDto()));

        mockMvc.perform(get("/meter-readings/month/3")
                        .with(request -> {
                            request.setRemoteUser("testUser");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Get all meter reading history")
    public void testGetMeterReadingAllHistory() throws Exception {
        Mockito.when(userService.getUserByLogin("testUser")).thenReturn(new UserDto());
        Mockito.when(userMapper.userDtoToUser(any(UserDto.class))).thenReturn(new User());
        Mockito.when(meterService.getAllReadingsHistory(any(User.class))).thenReturn(Arrays.asList(new MeterReadingDto()));

        mockMvc.perform(get("/meter-readings/history/all")
                        .with(request -> {
                            request.setRemoteUser("testUser");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}

