package app.automs.recipe.lib;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.unmodifiableMap;

abstract public class StromRecipe implements StromWebdriver, StromPdfHandler {

    @Value("${automs.automation.resourceId}")
    public String resourceId;

    @Value("${automs.webdriver.url}")
    private String webdriverUri;

    @Autowired
    private Gson gson;

    protected abstract String process(WebDriver driver, String... args);

    protected abstract Boolean validate(@NotNull String... args);

    protected abstract String targetSite();

    public String run(String... args) {
        WebDriver driver = getDriver();
        driver.get(targetSite());
        String processResponse = process(driver, args);
        driver.quit();

        if (!validate(processResponse)) {
            throw new IllegalStateException("automation not successfully validated");
        }

        return transform(processResponse);
    }

    protected String transform(@NotNull String... capturedResponses) {
        return asJson(resourceId, capturedResponses[0]);
    }

    protected void withDriverConfig(@NotNull WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    protected WebDriver getDriver() {
        WebDriver driver = withRemoteWebdriver(webdriverUri, prepareHeadlessBrowser());
        withDriverConfig(driver);
        return driver;
    }

    protected String asJson(String queryId, String response) {
        return gson.toJson(unmodifiableMap(new LinkedHashMap<String, String>() {{
            put("uuid", UUID.randomUUID().toString());
            put("queryId", queryId);
            put("response", response);
            put("date", LocalDateTime.now().toString());
        }}));
    }
}

