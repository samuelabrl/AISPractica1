package es.codeurjc.ais.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;


public class BookWebControllerTest {

    private WebDriver driver;
    private WebElement topic;

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();

    }

   @BeforeEach
    public void setupTest() {
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        topic = driver.findElement(By.name("topic"));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /*@AfterAll
    public void commentDeletion() {

    }*/

    @Test
    @DisplayName("When search drama and pick first option, the book has the drama target")
    public void DramaTarget() {
        topic.sendKeys("drama");
        WebElement boton = driver.findElement(By.id("search-button"));
        boton.submit();
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement book = wait1.until(
                presenceOfElementLocated(By.xpath("/html/body/div[2]/div/a[1]")));
        book.click();
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(5));

        WebElement text = wait2.until(
                presenceOfElementLocated(By.id("drama")));
        assertTrue(text.getText().contains("drama"));
    }

    @Test
    @DisplayName("When search epic fantasy and can add a review with content on the book 'The Way of Kings'")
    public void ReviewFullTest() {;
        topic.sendKeys("epic fantasy");
        WebElement boton = driver.findElement(By.id("search-button"));
        boton.submit();

        WebDriverWait waitList = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement book = waitList.until(presenceOfElementLocated(By.id("The Way of Kings")));
        book.click();

        WebDriverWait waitFormulario = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement nickname = waitFormulario.until(presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[17]/div/form/div[1]/input")));
        nickname.sendKeys("Juanito08");
        WebElement review = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div/form/div[2]/textarea"));
        review.sendKeys("Muy buen libro, pero le faltaron loot boxes");
        WebElement botonReview = driver.findElement(By.xpath("/html/body/div[2]/div/div[17]/div/form/button"));
        botonReview.submit();

        WebDriverWait waitReview = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement textReview = waitReview.until(presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[17]/div[1]/div/a")));

        List<WebElement> comments = driver.findElements(By.className("comment"));


        assertEquals("Muy buen libro, pero le faltaron loot boxes", comments.get(comments.size() - 1).findElement(By.className("text")).getText());
    }
    @Test
    @DisplayName("When search epic fantasy and can add a review without content on the book 'Words of Radiance'")
    public void ReviewEmptyTest() {
        topic.sendKeys("epic fantasy");
        WebElement boton = driver.findElement(By.id("search-button"));
        boton.submit();

        WebDriverWait waitList = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement book = waitList.until(presenceOfElementLocated(By.id("Words of Radiance")));
        book.click();

        WebDriverWait waitReview = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement botonReview = waitReview.until(presenceOfElementLocated(By.xpath("/html/body/div[2]/div/div[16]/div/form/button")));
        botonReview.submit();

       // assertThrows(org.openqa.selenium.NoSuchElementException.class, () -> driver.findElement(By.id("error-message")));
        assertTrue(driver.findElement(By.id("error-message")).isDisplayed());
    }

}