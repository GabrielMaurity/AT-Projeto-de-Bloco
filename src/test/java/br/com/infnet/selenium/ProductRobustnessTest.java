package br.com.infnet.selenium;

import br.com.infnet.App;
import br.com.infnet.pages.ProductFormPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRobustnessTest {

    @LocalServerPort
    private int port;
    private WebDriver driver;

    @BeforeAll
    static void setupClass() { WebDriverManager.chromedriver().setup(); }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() { if (driver != null) driver.quit(); }

    @Test
    @DisplayName("Deve exibir página de erro amigável (Fail Gracefully) em caso de falha crítica")
    void testFailGracefully() {
        // Navega para a URL base
        String baseUrl = "http://localhost:" + port;

        // Vamos forçar um erro tentando criar um produto com nome "ERROR"
        // (Lógica implementada no Service para simular falha crítica)
        driver.get(baseUrl + "/new");
        ProductFormPage form = new ProductFormPage(driver);
        form.fillForm("ERROR", "100.00", "10", "FOOD"); // "ERROR" dispara a BusinessException
        form.submit();

        // Verifica se fomos redirecionados para a tela do form com a mensagem de erro
        // Ao invés de uma tela branca ou stacktrace
        Assertions.assertTrue(driver.getPageSource().contains("Simulação de erro crítico acionada"));
    }

    @Test
    @DisplayName("Teste de Fuzzing na Interface (Input Malicioso)")
    void testInterfaceFuzzing() {
        driver.get("http://localhost:" + port + "/new");
        ProductFormPage form = new ProductFormPage(driver);

        // Injeta Script (XSS Simples) para ver se o sistema sanitiza ou rejeita
        String xssAttack = "<script>alert('hacked')</script>";
        form.fillForm(xssAttack, "50.00", "5", "BOOKS");
        form.submit();

        // O sistema deve ter aceitado o cadastro (pois salvamos string pura)
        // OU rejeitado, mas a página não deve ter executado o alert.
        // Aqui verificamos apenas se não quebrou a aplicação
        Assertions.assertDoesNotThrow(() -> driver.getTitle());
    }
}