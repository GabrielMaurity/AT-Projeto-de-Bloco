package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor; // <--- Importante
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductFormPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By nameInput = By.id("input-nome");
    private final By priceInput = By.id("input-preco");
    private final By stockInput = By.id("input-estoque");
    private final By categorySelect = By.id("select-categoria");
    private final By supplierSelect = By.name("supplierId");
    private final By saveBtn = By.xpath("//button[@type='submit']");
    private final By errorMsg = By.id("msg-erro");

    public ProductFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // 30s de tolerância
    }

    public void fillForm(String name, String price, String stock, String category) {
        // Garante que o campo está visível antes de digitar
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameField.clear();
        nameField.sendKeys(name);

        driver.findElement(priceInput).clear();
        driver.findElement(priceInput).sendKeys(price);

        driver.findElement(stockInput).clear();
        driver.findElement(stockInput).sendKeys(stock);

        new Select(driver.findElement(categorySelect)).selectByVisibleText(category);

        // Tenta selecionar fornecedor se o campo existir (integração)
        try {
            WebElement supplier = driver.findElement(supplierSelect);
            if (supplier.isDisplayed()) {
                new Select(supplier).selectByIndex(1);
            }
        } catch (Exception e) {
            // Ignora se não houver campo de fornecedor ou lista vazia
        }
    }

    public void submit() {
        // Espera o botão existir
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(saveBtn));

        // --- SOLUÇÃO NUCLEAR: CLIQUE VIA JAVASCRIPT ---
        // Resolve o erro "ElementClickInterceptedException"
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).getText();
    }
}