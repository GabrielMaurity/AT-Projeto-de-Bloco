package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class ProductListPage {
    private final WebDriver driver;

    // Locators (Mapeamento dos elementos)
    private final By newProductBtn = By.id("btn-novo");
    private final By productTableRows = By.cssSelector("#tabela-produtos tbody tr");

    public ProductListPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickNewProduct() {
        driver.findElement(newProductBtn).click();
    }

    public boolean hasProductWithName(String name) {
        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) return true;
        }
        return false;
    }

    public void deleteProductByName(String name) {
        List<WebElement> rows = driver.findElements(productTableRows);
        for (WebElement row : rows) {
            if (row.getText().contains(name)) {
                row.findElement(By.className("btn-delete")).click();
                driver.switchTo().alert().accept(); // Confirma o alerta JS
                break;
            }
        }
    }
}