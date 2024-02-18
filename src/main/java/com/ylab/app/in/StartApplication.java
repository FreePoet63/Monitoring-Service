package com.ylab.app.in;

import com.ylab.app.dbService.migration.LiquibaseMigration;
import com.ylab.app.in.servlets.MeterServlet;
import com.ylab.app.in.servlets.UserServlet;
import com.ylab.app.service.UserService;
import com.ylab.app.service.impl.UserServiceImpl;
import jakarta.servlet.Servlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * StartApplication class to initialize and run the meter reading service application.
 *
 * @author razlivinsky
 * @since 24.01.2024
 */
public class StartApplication {
    private LiquibaseMigration liquibaseMigration = new LiquibaseMigration();
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws Exception {
        new StartApplication().superMain();
        Server server = new Server(8866);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new UserServlet()), "/users/*");
        context.addServlet(new ServletHolder(new MeterServlet()), "/meter/*");
        server.start();
        server.join();
    }

    private void superMain() {
        liquibaseMigration.performLiquibaseMigration();
    }
}
    