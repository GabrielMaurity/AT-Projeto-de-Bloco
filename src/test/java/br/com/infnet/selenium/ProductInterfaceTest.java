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

        // Configuração Robustez para CI/CD
        String headless = System.getProperty("headless");
        if ("true".equals(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Lógica de URL Dinâmica (Local vs Produção)
        String targetUrl = System.getProperty("targetUrl");
        if (targetUrl != null && !targetUrl.isEmpty()) {
            baseUrl = targetUrl;
        } else {
            baseUrl = "http://localhost:" + port;
        }
    }

    @AfterEach void tearDown() { if (driver != null) driver.quit(); }

    // --- RUBRICA: Todas as Interações (CREATE) ---
    @Test
    @Order(1)
    @DisplayName("1. Fluxo: Criar Produto")
    void testCreate() {
        driver.get(baseUrl);
        new ProductListPage(driver).clickNewProduct();
        ProductFormPage form = new ProductFormPage(driver);
        form.fillForm("Produto Base", "100.00", "10", "ELECTRONICS");
        form.submit();
        Assertions.assertTrue(new ProductListPage(driver).hasProductWithName("Produto Base"));
    }

    @Test
    @Order(2)
    @DisplayName("2. Fluxo: Editar Produto")
    void testUpdate() {
        driver.get(baseUrl);
        String uniqueName = "UpdateTest-" + System.currentTimeMillis();
        String editedName = uniqueName + "-EDITED";


        new ProductListPage(driver).clickNewProduct();
        ProductFormPage formCreate = new ProductFormPage(driver);
        formCreate.fillForm(uniqueName, "10.00", "5", "FOOD");
        formCreate.submit();


        ProductListPage list = new ProductListPage(driver);
        list.clickEditProduct(uniqueName); // Agora usa Javascript Click

        ProductFormPage formEdit = new ProductFormPage(driver);
        formEdit.fillForm(editedName, "150.00", "20", "BOOKS");
        formEdit.submit();


        Assertions.assertTrue(list.hasProductWithName(editedName));
    }


    @Test
    @Order(3)
    @DisplayName("3. Fluxo: Excluir Produto")
    void testDelete() {
        driver.get(baseUrl);
        ProductListPage list = new ProductListPage(driver);
        list.deleteProductByName("Produto Editado");
        Assertions.assertFalse(list.hasProductWithName("Produto Editado"));
    }

    // --- RUBRICA: Testes Parametrizados ---
    @ParameterizedTest
    @CsvSource({
            "Celular Param, 1000.00, 5, ELECTRONICS",
            "Livro Param, 50.00, 100, BOOKS"
    })
    @Order(4)
    @DisplayName("4. Cenários Variados")
    void testParameterized(String name, String price, String stock, String category) {
        driver.get(baseUrl);
        new ProductListPage(driver).clickNewProduct();
        new ProductFormPage(driver).fillForm(name, price, stock, category);
        new ProductFormPage(driver).submit();
        Assertions.assertTrue(new ProductListPage(driver).hasProductWithName(name));
    }

    // --- RUBRICA: Mensagens de Erro ---
    @Test
    @Order(5)
    @DisplayName("5. Validação de Erro")
    void testError() {
        driver.get(baseUrl + "/new");
        ProductFormPage form = new ProductFormPage(driver);
        form.fillForm("Erro", "-10.00", "5", "ELECTRONICS");
        form.submit();
        // Verifica se a mensagem de erro apareceu
        Assertions.assertTrue(form.getErrorMessage().contains("Preço deve ser maior"));
    }
}