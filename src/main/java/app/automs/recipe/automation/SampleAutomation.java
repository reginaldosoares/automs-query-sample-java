package app.automs.recipe.automation;


import app.automs.recipe.lib.StromAutomation;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * Defines a automs automation recipe
 */
@Component
@Scope("prototype")
public class SampleAutomation extends StromAutomation {
    public static final String EXPECTED_TEXT_CONTENT = "Não existem pendências para";
    public static final String ENTRY_POINT_URL = "https://sistemas.sefaz.go.gov.br/cdn-consultas/pendencia";
    private static final Logger logger = LoggerFactory.getLogger(SampleAutomation.class);

    @Override
    @NotNull
    protected String recipe(String... args) {
        String givenCnpj = args[0];

        driver.findElement(By.id("txtId")).click();
        driver.findElement(By.id("txtId")).sendKeys(givenCnpj);
        driver.findElement(By.id("txtCaptchaDigitado")).click();

        final String captchaValue = driver.findElement(By.cssSelector("label.btn")).getText();
        driver.findElement(By.id("txtCaptchaDigitado")).sendKeys(captchaValue);

        driver.findElement(By.cssSelector("#consultar-pendencias-relatorio > .fa")).click();

        final String capturedResponse = driver.findElement(By.xpath("//form/div[2]/i")).getText();

        if (capturedResponse.contains(EXPECTED_TEXT_CONTENT)) {

            driver.findElement(By.id("button-gerar")).click();

            String parentWindow = driver.getWindowHandle();
            Set<String> windows = driver.getWindowHandles();
            driver.switchTo().window(
                    windows
                            .stream().filter(s -> !s.contains(parentWindow))
                            .findFirst()
                            .orElse(parentWindow)
            );
        }

        logger.info("givenCnpj: " + givenCnpj);
        logger.info("capturedResponse: " + capturedResponse);

        return capturedResponse;
    }

    @Override
    @NotNull
    protected Boolean validate(@NotNull String... args) {
        String response = args[0];
        return response.contains(EXPECTED_TEXT_CONTENT);
    }

    @Override
    protected String entryPointUrl() {
        return ENTRY_POINT_URL;
    }
}
