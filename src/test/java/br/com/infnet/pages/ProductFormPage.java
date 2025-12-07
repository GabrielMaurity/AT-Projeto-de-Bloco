package br.com.infnet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProductFormPage {
    private final WebDriver driver;
    private final WebDriverWait wait; // Adicionamos o Wait

    private final By nameInput = By.id("input-nome");
    private final By priceInput = By.id("input-preco");
    private final By stockInput = By.id("input-estoque");
    private final By categorySelect = By.id("select-categoria");
    private final By supplierSelect = By.name("supplierId"); // Caso tenha adicionado a integração
    private final By saveBtn = By.xpath("//button[@type='submit']"); // Melhor usar XPath ou seletor robusto
    private final By errorMsg = By.id("msg-erro");

    public ProductFormPage(WebDriver driver) {
        this.driver = driver;
        // Configura uma espera de até 10 segundos
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillForm(String name, String price, String stock, String category) {
        // CORREÇÃO: Espera o campo Nome aparecer antes de interagir
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        nameField.clear();
        nameField.sendKeys(name);

        driver.findElement(priceInput).sendKeys(price);
        driver.findElement(stockInput).sendKeys(stock);

        new Select(driver.findElement(categorySelect)).selectByVisibleText(category);

        // Se estiver testando a integração com fornecedor e o campo existir:
        try {
            new Select(driver.findElement(supplierSelect)).selectByIndex(1); // Seleciona o primeiro da lista
        } catch (Exception e) {
            // Ignora se o campo não estiver na tela neste teste específico
        }
    }

    public void submit() {
        // As vezes o botão demora para ficar clicável
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
        btn.click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMsg)).getText();
    }
}