package br.com.infnet.selenium;

import br.com.infnet.App;
import br.com.infnet.pages.ProductFormPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRobustnessTest {

    @LocalServerPort private int port;
    private WebDriver driver;

    @BeforeAll static void setupClass() { WebDriverManager.chromedriver().setup(); }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        String headless = System.getProperty("headless");
        if ("true".equals(headless)) options.addArguments("--headless=new");

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach void tearDown() { if (driver != null) driver.quit(); }

    @Test
    @DisplayName("Deve exibir mensagem de erro amigável (Fail Gracefully)")
    void testFailGracefully() {
        String baseUrl = "http://localhost:" + port;

        driver.get(baseUrl + "/new");
        ProductFormPage form = new ProductFormPage(driver);

        form.fillForm("ERROR", "100.00", "10", "FOOD");
        form.submit();

        String textoErro = form.getErrorMessage();

        Assertions.assertNotNull(textoErro, "A mensagem de erro não apareceu!");
        Assertions.assertTrue(
                textoErro.toLowerCase().contains("erro") || textoErro.toLowerCase().contains("crítico"),
                "O texto da mensagem não era o esperado. Texto recebido: " + textoErro
        );
    }

    @Test
    void testInterfaceFuzzing() {
        driver.get("http://localhost:" + port + "/new");
        ProductFormPage form = new ProductFormPage(driver);
        String xssAttack = "<script>alert('hacked')</script>";
        form.fillForm(xssAttack, "50.00", "5", "BOOKS");
        form.submit();
        // Apenas garante que não quebrou (Exception)
        Assertions.assertDoesNotThrow(() -> driver.getTitle());
    }
}