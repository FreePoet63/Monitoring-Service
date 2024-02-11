package com.ylab.app.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ylab.app.aspect.LogExecution;
import com.ylab.app.exception.meterException.MeterReadingException;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.mapper.UserMapper;
import com.ylab.app.model.User;
import com.ylab.app.model.dto.MeterReadingDto;
import com.ylab.app.model.dto.MeterReadingRequestDto;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.impl.MeterServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * MeterServlet class
 *
 * @author HP
 * @since 09.02.2024
 */
@LogExecution
@WebServlet(name = "MeterServlet", urlPatterns = "/meter/*")
public class MeterServlet extends HttpServlet {
    private MeterService meterService = new MeterServiceImpl();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDto user = (UserDto) request.getSession().getAttribute("user");
        String action = request.getParameter("action");
        objectMapper.registerModule(new JavaTimeModule());
        UserMapper mapper = UserMapper.INSTANCE;
        User user1 = mapper.userDtoToUser(user);
        switch (action) {
            case "current":
                getCurrentReadings(request, response, user1);
                break;
            case "month":
                getReadingsByMonth(request, response, user1);
                break;
            case "history":
                getReadingsHistory(request, response, user1);
                break;
            case "all":
                getAllReadingsHistory(request, response, user1);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    /**
     * Gets current readings.
     *
     * @param request  the request
     * @param response the response
     * @param user     the user
     * @throws IOException the io exception
     */
    public void getCurrentReadings(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            List<MeterReadingDto> currentReadings = meterService.getCurrentReadings(user);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), currentReadings);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Gets readings by month.
     *
     * @param request  the request
     * @param response the response
     * @param user     the user
     * @throws IOException the io exception
     */
    public void getReadingsByMonth(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            int month = Integer.parseInt(request.getParameter("month"));
            List<MeterReadingDto> readingsByMonth = meterService.getReadingsByMonth(user, month);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), readingsByMonth);
        } catch (UserValidationException | MeterReadingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Gets readings history.
     *
     * @param request  the request
     * @param response the response
     * @param user     the user
     * @throws IOException the io exception
     */
    public void getReadingsHistory(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            List<MeterReadingDto> readingsHistory = meterService.getReadingsHistory(user);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), readingsHistory);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Gets all readings history.
     *
     * @param request  the request
     * @param response the response
     * @param user     the user
     * @throws IOException the io exception
     */
    public void getAllReadingsHistory(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        try {
            List<MeterReadingDto> allReadingsHistory = meterService.getAllReadingsHistory(user);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), allReadingsHistory);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (MeterReadingException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            UserDto userDto = (UserDto) request.getSession().getAttribute("user");
            UserMapper mapper = UserMapper.INSTANCE;
            User user = mapper.userDtoToUser(userDto);
            objectMapper.registerModule(new JavaTimeModule());

            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String readingsJson = buffer.toString();

            MeterReadingRequestDto requestDto = objectMapper.readValue(readingsJson, MeterReadingRequestDto.class);

            if (requestDto.getDetailsList() == null || requestDto.getDetailsList().isEmpty()) {
                throw new MeterReadingException("Empty details list");
            }

            MeterReadingDto submittedReading = meterService.submitReading(user, requestDto.getNumberMeter(), requestDto.getDetailsList());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), submittedReading);
        } catch (MeterReadingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }
}