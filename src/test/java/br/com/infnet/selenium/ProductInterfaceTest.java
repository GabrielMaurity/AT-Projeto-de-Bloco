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

        // --- OBRIGATÓRIO PARA GITHUB ACTIONS ---
        // Se receber a propriedade de sistema "headless", ativa o modo sem tela
        String headless = System.getProperty("headless");
        if ("true".equals(headless)) {
            options.addArguments("--headless");
        }

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox"); // Necessário para Docker/Linux
        options.addArguments("--disable-dev-shm-usage"); // Evita crash de memória

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Configuração para teste pós-deploy (Rubrica 5)
        String targetUrl = System.getProperty("targetUrl");
        if (targetUrl != null && !targetUrl.isEmpty()) {
            baseUrl = targetUrl;
        } else {
            baseUrl = "http://localhost:" + port;
        }
    }

    @AfterEach void tearDown() { if (driver != null) driver.quit(); }

    // --- SEUS TESTES ---
    @Test
    @Order(1)
    void testCreateProduct() {
        driver.get(baseUrl);
        new ProductListPage(driver).clickNewProduct();
        ProductFormPage form = new ProductFormPage(driver);
        form.fillForm("CI Test", "50.00", "10", "ELECTRONICS");
        form.submit();
        Assertions.assertTrue(new ProductListPage(driver).hasProductWithName("CI Test"));
    }

    // Mantenha os outros testes (update, delete, parametrizado) aqui...
}