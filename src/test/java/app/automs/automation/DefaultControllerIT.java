package app.automs.automation;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Integration test for local or remote service based on the env var
 * "SERVICE_URL". See java/CONTRIBUTING.MD for more information.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class DefaultControllerIT {

    @Test
    public void respondsToHttpRequest() throws IOException {
        String port = System.getenv("PORT");
        if (port == null || port == "") {
            port = "8080";
        }

        String url = System.getenv("SERVICE_URL");
        if (url == null || url == "") {
            url = "http://localhost:" + port;
        }

        OkHttpClient ok =
                new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .build();

        Request request = new Request.Builder().url(url + "/").get().build();

        String expected = "Not Found";
        Response response = ok.newCall(request).execute();
        assertThat(response.body().string(), containsString(expected));
        assertThat(response.code(), equalTo(404));
    }
}
