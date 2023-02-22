package es.codeurjc.ais.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.*;
//import static org.junit.Assert.*;

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

    @Test
    @DisplayName("When search epic fantasy and can add a review with content on the book 'The Way of Kings'")
    public void ReviewTestFantasy() {
        driver.get("http://localhost:8080/");
        WebElement topic = driver.findElement(By.name("topic"));
        topic.sendKeys("epic fantasy");
        WebElement boton = driver.findElement(By.id("search-button"));
        boton.submit();

        WebElement book = driver.findElement(By.id("The Way of Kings"));
        book.click();

        WebElement nickname = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div/form/div[1]/input"));
        nickname.sendKeys("Juanito08");
        WebElement review = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div/form/div[2]/textarea"));
        review.sendKeys("Muy buen libro, pero le faltaron loot boxes");
        WebElement botonReview = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div/form/button"));
        botonReview.submit();

       // WebElement nombreReview = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div[1]/div/div[2]"));
        WebElement textReview = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div[1]/div/a"));

        List<WebElement> comments = driver.findElements(By.className("comment"));


        assertEquals("Muy buen libro, pero le faltaron loot boxes", comments.get(comments.size() - 1).findElement(By.className("text")).getText());
    }
}