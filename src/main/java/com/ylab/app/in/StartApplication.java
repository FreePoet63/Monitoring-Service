package com.ylab.app.in;

import com.ylab.app.dbService.migration.LiquibaseMigration;
import com.ylab.app.model.Audit;
import com.ylab.app.model.MeterReadingDetails;
import com.ylab.app.model.User;
import com.ylab.app.model.UserRole;
import com.ylab.app.service.MeterService;
import com.ylab.app.service.UserService;
import com.ylab.app.service.WebService;
import com.ylab.app.service.impl.MeterServiceImpl;
import com.ylab.app.service.impl.UserServiceImpl;
import com.ylab.app.service.impl.WebServiceImpl;
import com.ylab.app.service.Audition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * StartApplication class to initialize and run the meter reading service application.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class StartApplication {
    private LiquibaseMigration liquibaseMigration = new LiquibaseMigration();
    private UserService userService = new UserServiceImpl();
    private MeterService meterService = new MeterServiceImpl(userService);
    private WebService webService = new WebServiceImpl(userService, meterService);
    private Audition audition = new Audition();
    private Scanner sc = new Scanner(System.in);
    private User user;
    private boolean running = true;
    private boolean loggedIn = true;

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new StartApplication().start();
    }

    private void start() {
        liquibaseMigration.performLiquibaseMigration();
        System.out.println("Welcome to the meter reading service!");
        while (running) {
            System.out.println("Main menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = sc.nextInt();
            mainMenu(choice);
        }
        System.out.println(audition.getAuditLogs());
    }

    private void mainMenu(int choice) {
        switch (choice) {
            case 1:
                registration();
                break;
            case 2:
                login();
                if (user != null) {
                    userMenu();
                }
                break;
            case 3:
                System.out.println("Exiting the application");
                running = false;
                break;
            default:
                System.out.println("Invalid  choice");
                break;
        }
    }

    private void userMenu() {
        while (loggedIn) {
            System.out.println("User menu:");
            System.out.println("1. Submit reading");
            System.out.println("2. View current readings");
            System.out.println("3. View readings by month");
            System.out.println("4. View readings history");
            System.out.println("5. Logout");
            if (user.getRole().equals(UserRole.ADMIN)) {
                System.out.println("6. View all readings");
            }
            int choice = sc.nextInt();
            meterReadingMenu(choice);
            if (!loggedIn) {
                return;
            }
        }
    }

    private void meterReadingMenu(int choice) {
        switch (choice) {
            case 1:
                submitReading();
                break;
            case 2:
                getCurrentReading();
                break;
            case 3:
                getMonthReading();
                break;
            case 4:
                historyReading();
                break;
            case 5:
                logout();
                break;
            case 6:
                if (user.getRole().equals(UserRole.ADMIN)) {
                    webService.handleGetAllReadingsRequest(user);
                    break;
                }
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void registration() {
        System.out.println("Enter name, password and role for registration:");
        String name = sc.next();
        String password = sc.next();
        webService.handleRegisterRequest(name, password, UserRole.USER);
        return;
    }

    private void login() {
        System.out.println("Enter name and password for login:");
        String name = sc.next();
        String password = sc.next();
        User loginUser = webService.handleLoginRequest(name, password);
        if (loginUser != null) {
            user = loginUser;
            loggedIn = true;
            audition.auditAction(user, Audit.LOGIN);
            userMenu();
        } else {
            System.out.println("Login failed.");
        }
    }

    private void submitReading() {
        System.out.println("Enter the number of meter types and reading values for current month:");
        int n = sc.nextInt();
        String numberMeter = sc.next();
        List<MeterReadingDetails> readings = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String type = sc.next();
            double value = sc.nextDouble();
            readings.add(new MeterReadingDetails(type, value));
        }

        webService.handleSubmitReadingRequest(user, numberMeter, readings);
        audition.auditAction(user, Audit.SUBMIT_READING);
        return;
    }

    private void getCurrentReading() {
        webService.handleGetCurrentReadingsRequest(user);
        audition.auditAction(user, Audit.VIEW_CURRENT_READINGS);
        return;
    }

    private void getMonthReading() {
        System.out.println("Enter month number for viewing readings by month:");
        int month = sc.nextInt();
        webService.handleGetReadingsByMonthRequest(user, month);
        audition.auditAction(user, Audit.VIEW_READINGS_BY_MONTH);
        return;
    }

    private void historyReading() {
        webService.handleGetReadingsHistoryRequest(user);
        audition.auditAction(user, Audit.VIEW_READING_HISTORY);
        return;
    }

    private void logout() {
        System.out.println("User logged out successfully");
        loggedIn = false;
        audition.auditAction(user, Audit.LOGOUT);
        audition.auditAction(user, Audit.SESSION_END);
        user = null;
    }
}
    