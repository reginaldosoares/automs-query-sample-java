package datarequest.recipe;


import datarequest.lib.StromRecipe;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;


/**
 * Defines a automs query recipe
 */
@Component
public class QuerySampleRecipe extends StromRecipe {
    private static final Logger logger = LoggerFactory.getLogger(QuerySampleRecipe.class);

    @Override
    @NotNull
    public String query(String... args) {
        WebDriver driver = getDriver();
        String givenCnpj = args[0];

        driver.get(targetSite());

        driver.findElement(By.id("txtId")).click();
        driver.findElement(By.id("txtId")).sendKeys(givenCnpj);
        driver.findElement(By.id("txtCaptchaDigitado")).click();

        final String captchaValue = driver.findElement(By.cssSelector("label.btn")).getText();
        driver.findElement(By.id("txtCaptchaDigitado")).sendKeys(captchaValue);

        driver.findElement(By.cssSelector("#consultar-pendencias-relatorio > .fa")).click();

        final String capturedResponse = driver.findElement(By.xpath("//form/div[2]/i")).getText();

        if (capturedResponse.contains("Não existem pendências para")) {

            driver.findElement(By.id("button-gerar")).click();

            Set<String> windows = driver.getWindowHandles();
            driver.switchTo().window(
                    windows.stream().filter(s -> !s.contains(driver.getWindowHandle())).findFirst().get()
            );

        }

        driver.quit();

        logger.info("givenCnpj: " + givenCnpj);
        logger.info("capturedResponse: " + capturedResponse);

        return asJson("equipo/sefaz-geral/pendencias", capturedResponse);
    }

    @Override
    protected String targetSite() {
        return "https://sistemas.sefaz.go.gov.br/cdn-consultas/pendencia";
    }
}
