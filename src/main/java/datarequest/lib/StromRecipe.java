package datarequest.lib;

import com.google.gson.Gson;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.unmodifiableMap;


abstract public class StromRecipe implements StromWebdriver {

    @Value("${dataquery.webdriver.url}")
    private String webdriverUri;

    @Autowired
    private Gson gson;

    public abstract String query(String... args);

    protected abstract String targetSite();

    protected void withDriverConfig(WebDriver driver) {
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

    public String readPDFContent(String appUrl) throws Exception {

        URL url = new URL(appUrl);
        InputStream input = url.openStream();
        BufferedInputStream fileToParse = new BufferedInputStream(input);
        PDDocument document = null;
        String output = null;

        try {
            document = PDDocument.load(fileToParse);
            output = new PDFTextStripper().getText(document);
            System.out.println(output);

        } finally {
            if (document != null) {
                document.close();
            }
            fileToParse.close();
        }
        return output;
    }
}

