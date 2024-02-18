package com.ylab.app.in.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ylab.app.aspect.LogExecution;
import com.ylab.app.exception.userException.UserValidationException;
import com.ylab.app.model.dto.UserDto;
import com.ylab.app.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * The type User servlet.
 */
@LogExecution
@WebServlet(name = "UserServlets", urlPatterns = "/users/*")
public class UserServlet extends HttpServlet {
    private final UserServiceImpl userService = new UserServiceImpl();
    private final ObjectMapper mapper = new ObjectMapper();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ObjectNode json = mapper.readValue(request.getReader(), ObjectNode.class);
            String name = json.get("name").asText();
            String password = json.get("password").asText();
            UserDto user = userService.registerUser(name, password);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_CREATED);
            mapper.writeValue(response.getWriter(), user);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        if ("/login".equals(pathInfo) && name != null && password != null) {
            loginUser(request, response, name, password);
        } else if (pathInfo == null || pathInfo.equals("/")) {
            getAllUsers(request, response);
        } else if (pathInfo.length() > 1) {
            try {
                long id = Long.parseLong(pathInfo.substring(1));
                getUserById(request, response, id);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный формат идентификатора пользователя");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Неверный запрос");
        }
    }

    /**
     * Gets all users.
     *
     * @param request  the request
     * @param response the response
     * @throws IOException the io exception
     */
    public void getAllUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<UserDto> users = userService.getAllUsers();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(response.getWriter(), users);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Login user.
     *
     * @param request  the request
     * @param response the response
     * @param name     the name
     * @param password the password
     * @throws IOException the io exception
     */
    public void loginUser(HttpServletRequest request, HttpServletResponse response, String name, String password) throws IOException {
        try {
            UserDto user = userService.loginUser(name, password);
            request.getSession().setAttribute("user", user);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(response.getWriter(), user);
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Gets user by id.
     *
     * @param request  the request
     * @param response the response
     * @param id       the id
     * @throws IOException the io exception
     */
    public void getUserById(HttpServletRequest request, HttpServletResponse response, long id) throws IOException {
        try {
            UserDto user = userService.getUserById(id);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Пользователь не найден");
            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                mapper.writeValue(response.getWriter(), user);
            }
        } catch (UserValidationException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
