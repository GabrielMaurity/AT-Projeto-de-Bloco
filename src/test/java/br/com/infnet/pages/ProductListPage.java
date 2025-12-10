package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ProductListPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By productTableRows = By.cssSelector("table tbody tr");

    public ProductListPage(WebDriver driver) {
        this.driver = driver;
        // AUMENTADO PARA 30s (O seu log mostrava timeout de 10s, que é pouco para Azure/CI)
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void clickNewProduct() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btn-novo")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public boolean hasProductWithName(String name) {
        try {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), name));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void clickEditProduct(String name) {
        // Espera a tabela estar visível
        wait.until(ExpectedConditions.presenceOfElementLocated(productTableRows));

        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                // Encontra o botão
                WebElement editBtn = row.findElement(By.className("btn-edit"));

                // CLIQUE VIA JAVASCRIPT (Resolve o problema de timeout/click intercepted)
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);
                return;
            }
        }
        throw new RuntimeException("Produto não encontrado na tabela: " + name);
    }

    public void deleteProductByName(String name) {
        wait.until(ExpectedConditions.presenceOfElementLocated(productTableRows));
        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                WebElement delBtn = row.findElement(By.className("btn-delete"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", delBtn);

                try { driver.switchTo().alert().accept(); } catch (Exception e) {}
                return;
            }
        }
    }
}