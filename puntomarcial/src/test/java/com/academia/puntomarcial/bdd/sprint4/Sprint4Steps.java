package com.academia.puntomarcial.bdd.sprint4;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.academia.puntomarcial.bdd.comun.Navegador;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps del Sprint 4 (HU-08 y HU-09).
 * Cada método corresponde a una línea de features/sprint4/HU08_HU09_productos_compra.feature
 */
public class Sprint4Steps {

    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void abrirChrome() throws InterruptedException {
        //Abrir Chrome
        driver = new ChromeDriver(Navegador.opciones());

        //Maximizar Chrome
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        //Pausa de 2 seg
        Thread.sleep(2000);
    }

    @After
    public void cerrarChrome() throws InterruptedException {
        Thread.sleep(2000);

        //Cerrar el navegador
        driver.quit();
    }

    @Given("que inicié sesión con el correo {string} y la contraseña {string}")
    public void queInicieSesion(String correo, String clave) throws InterruptedException {
        driver.get(Navegador.URL + "/login");
        Thread.sleep(2000);

        //Caja de texto del correo y la contraseña
        driver.findElement(By.id("correo")).sendKeys(correo);
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(clave);
        Thread.sleep(2000);

        //Botón INGRESAR
        driver.findElement(By.cssSelector("button.login-button")).click();

        //Espera hasta que el sistema cambie de pantalla
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        Thread.sleep(2000);
    }

    @When("abro la pantalla {string}")
    public void abroLaPantalla(String pantalla) throws InterruptedException {
        driver.get(Navegador.URL + pantalla);
        Thread.sleep(2000);
    }

    @When("agrego {int} unidades de {string} al carrito")
    public void agregoAlCarrito(int cantidad, String producto) throws InterruptedException {
        //Filtro de disciplina "General" para que el producto aparezca en la primera página
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtro-disciplina"))))
                .selectByValue("GENERAL");
        Thread.sleep(2000);

        //Tarjeta del producto
        WebElement tarjeta = driver.findElement(By.xpath(
                "//li[contains(@class,'product-card')][.//h3[normalize-space()='" + producto + "']]"));

        //Caja de texto de la cantidad
        WebElement cajaCantidad = tarjeta.findElement(By.cssSelector("input[type='number']"));
        cajaCantidad.clear();
        cajaCantidad.sendKeys(String.valueOf(cantidad));
        Thread.sleep(2000);

        //Botón AGREGAR
        tarjeta.findElement(By.cssSelector("button.add-button")).click();
        Thread.sleep(2000);
    }

    @Then("veo la alerta {string}")
    public void veoLaAlerta(String esperado) {
        //Espera hasta que salga la alerta
        WebElement alerta = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alerta-stock")));

        String texto = alerta.getText();
        System.out.println("Alerta obtenida: " + texto);

        //Validación: el número de productos cambia con los datos, por eso se compara el final del texto
        assertTrue(texto.endsWith(esperado));
    }

    @Then("al marcar {string} todos los productos de la lista tienen alerta")
    public void alMarcarSoloStockBajo(String filtro) throws InterruptedException {
        //Casilla "solo stock bajo"
        driver.findElement(By.id("filtro-stock-bajo")).click();
        Thread.sleep(2000);

        List<WebElement> filas = driver.findElements(By.cssSelector(".table-wrapper tbody tr"));
        System.out.println("Productos con " + filtro + ": " + filas.size());

        //Validación: cada fila tiene su etiqueta de alerta (Stock bajo o Agotado)
        assertFalse(filas.isEmpty());
        for (WebElement fila : filas) {
            assertFalse(fila.findElements(By.cssSelector(".badge")).isEmpty());
        }
    }

    @Then("el total del carrito es {string}")
    public void elTotalDelCarritoEs(String esperado) {
        //Espera hasta que salga el total
        WebElement total = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".total-row strong")));

        String texto = total.getText();
        System.out.println("Total obtenido: " + texto);

        //Validación
        assertEquals(esperado, texto);
    }

    @Then("el producto {string} muestra {string} y no se puede agregar")
    public void elProductoNoSePuedeAgregar(String producto, String esperado) throws InterruptedException {
        //Filtro de disciplina "General"
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtro-disciplina"))))
                .selectByValue("GENERAL");
        Thread.sleep(2000);

        //Botón del producto agotado
        WebElement boton = driver.findElement(By.xpath(
                "//li[contains(@class,'product-card')][.//h3[normalize-space()='" + producto + "']]//button"));

        String texto = boton.getText();
        System.out.println("Botón obtenido: " + texto);

        //Validación
        assertEquals(esperado, texto);
        assertFalse(boton.isEnabled());
    }
}