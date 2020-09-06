package datarequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

/**
 * This class serves as an entry point for the Spring Boot app.
 */
@SpringBootApplication
public class DataRequestApplication {

    private static final Logger logger = LoggerFactory.getLogger(DataRequestApplication.class);

    public static void main(final String[] args) {
        String port = System.getenv("PORT");
        if (port == null) {
            port = "8080";
            logger.warn("$PORT environment variable not set, defaulting to 8080");
        }
        SpringApplication app = new SpringApplication(DataRequestApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", port));

        // Start the Spring Boot application.
        app.run(args);
        logger.info("The container started successfully and is listening for HTTP requests on $PORT");

    }
}
