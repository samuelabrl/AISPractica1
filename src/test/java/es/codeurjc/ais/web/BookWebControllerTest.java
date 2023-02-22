package es.codeurjc.ais.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;


public class BookWebControllerTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("When search drama and pick first option, the book has the drama target")
    public void DramaTarget() {
        driver.get("http://localhost:8080/");
        WebElement topic = driver.findElement(By.name("topic"));
        topic.sendKeys("drama");
        WebElement boton = driver.findElement(By.id("search-button"));
        boton.submit();

        WebElement book = driver.findElement(By.xpath("/html/body/div[2]/div/a[1]"));
        book.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        WebElement text = wait.until(
                presenceOfElementLocated(By.id("drama")));

        assertTrue(text.getText().contains("drama"));
    }
}