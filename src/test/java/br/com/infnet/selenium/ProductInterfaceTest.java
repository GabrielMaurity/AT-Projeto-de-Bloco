package br.com.infnet.selenium;

import br.com.infnet.App;
import br.com.infnet.pages.ProductFormPage;
import br.com.infnet.pages.ProductListPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.time.Duration;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductInterfaceTest {

    @LocalServerPort private int port;
    private static WebDriver driver;
    private String baseUrl;

    @BeforeAll static void setupClass() { WebDriverManager.chromedriver().setup(); }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();

        String headless = System.getProperty("headless");
        if ("true".equals(headless)) {
            options.addArguments("--headless");
        }
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // --- MUDANÇA PARA O TRABALHO 5 ---
        // Se passarmos uma URL específica (ex: ambiente de produção), usamos ela.
        // Se não, usamos o localhost padrão.
        String targetUrl = System.getProperty("targetUrl");
        if (targetUrl != null && !targetUrl.isEmpty()) {
            baseUrl = targetUrl;
        } else {
            baseUrl = "http://localhost:" + port; // Fallback para teste local (@SpringBootTest)
        }
    }

    @AfterEach void tearDown() { if (driver != null) driver.quit(); }

    @Test
    @Order(1)
    void testCreateProduct() {
        driver.get(baseUrl);
        ProductListPage listPage = new ProductListPage(driver);
        listPage.clickNewProduct();
        ProductFormPage formPage = new ProductFormPage(driver);

        // Fluxo normal (o fornecedor é opcional ou pegamos o default)
        formPage.fillForm("Selenium Phone", "1500.00", "10", "ELECTRONICS");
        formPage.submit();
        Assertions.assertTrue(listPage.hasProductWithName("Selenium Phone"));
    }
}