package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductListPage {
    private final WebDriver driver;
    // Seletor que pega todas as linhas da tabela
    private final By productTableRows = By.cssSelector("table tbody tr");

    public ProductListPage(WebDriver driver) { this.driver = driver; }

    public void clickNewProduct() { driver.findElement(By.id("btn-novo")).click(); }

    public boolean hasProductWithName(String name) {
        return driver.getPageSource().contains(name);
    }

    // --- NOVO: Método para clicar em EDITAR em um produto específico ---
    // Adicione um wait no clique de editar para garantir
    public void clickEditProduct(String name) {
        // Pequena espera para garantir que a tabela carregou
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(productTableRows).size() > 0);

        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                // Rola a página até o elemento (ajuda se estiver escondido)
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", row);
                row.findElement(By.className("btn-edit")).click();
                return;
            }
        }
        throw new RuntimeException("Produto não encontrado para edição: " + name);
    }

    // --- NOVO: Método para clicar em EXCLUIR ---
    public void deleteProductByName(String name) {
        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                row.findElement(By.className("btn-delete")).click();
                // Lidar com o "alert" de confirmação do navegador
                try { driver.switchTo().alert().accept(); } catch (Exception e) {}
                return;
            }
        }
    }
}