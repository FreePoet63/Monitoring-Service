package com.ylab.app.in;

import com.ylab.app.model.*;

import com.ylab.app.service.impl.*;
import com.ylab.app.util.*;

import java.util.*;

/**
 * StartApplication class to initialize and run the meter reading service application.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class StartApplication {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        MeterServiceImpl meterService = new MeterServiceImpl(userService);
        WebServiceImpl webService = new WebServiceImpl(userService, meterService);
        Audition audition = new Audition();
        Scanner sc = new Scanner(System.in);
        User user = null;
        boolean running = true;
        System.out.println("Welcome to the meter reading service!");
        while (running) {
            System.out.println("Main menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter name, password and role for registration:");
                    String name = sc.next();
                    String password = sc.next();
                    String role = sc.next();
                    webService.handleRegisterRequest(name, password, role);
                    break;
                case 2:
                    System.out.println("Enter name and password for login:");
                    name = sc.next();
                    password = sc.next();
                    user = webService.handleLoginRequest(name, password);
                    audition.auditAction(user, "Login");
                    if (user != null) {
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("User menu:");
                            System.out.println("1. Submit reading");
                            System.out.println("2. View current readings");
                            System.out.println("3. View readings by month");
                            System.out.println("4. View readings history");
                            System.out.println("5. Logout");
                            if (user.getRole().equals("admin")) {
                                System.out.println("6. View all readings");
                            }
                            choice = sc.nextInt();
                            switch (choice) {
                                case 1:
                                    System.out.println("Enter the number of meter types and reading values for current month:");
                                    int n = sc.nextInt();
                                    String s = sc.next();
                                    Map<String, Double> readings = new HashMap<>();
                                    for (int i = 0; i < n; i++) {
                                        String type = sc.next();
                                        double value = sc.nextDouble();
                                        readings.put(type, value);
                                    }
                                    for (Map.Entry<String, Double> entry : readings.entrySet()) {
                                        webService.handleSubmitReadingRequest(user, s, entry.getKey(), entry.getValue());
                                    }
                                    audition.auditAction(user, "Submit Reading");
                                    break;
                                case 2:
                                    webService.handleGetCurrentReadingsRequest(user);
                                    audition.auditAction(user, "View Current Readings");
                                    break;
                                case 3:
                                    System.out.println("Enter month number for viewing readings by month:");
                                    int month = sc.nextInt();
                                    webService.handleGetReadingsByMonthRequest(user, month);
                                    audition.auditAction(user, "View Readings by Month");
                                    break;
                                case 4:
                                    webService.handleGetReadingsHistoryRequest(user);
                                    audition.auditAction(user, "View Readings History");
                                    break;
                                case 5:
                                    System.out.println("User logged out successfully");
                                    loggedIn = false;
                                    audition.auditAction(user, "Logout");
                                    break;
                                case 6:
                                    if (user.getRole().equals("admin")) {
                                        webService.handleGetAllReadingsRequest(user);
                                        break;
                                    }
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }
                        }
                    }
                    break;
                case 3:
                    System.out.println("Exiting the application");
                    running = false;
                    audition.auditAction(user, "Session End");
                    break;
                default:
                    System.out.println("Invalid  choice");
                    break;
            }
        }
        System.out.println(audition.getAuditLogs());
    }
}