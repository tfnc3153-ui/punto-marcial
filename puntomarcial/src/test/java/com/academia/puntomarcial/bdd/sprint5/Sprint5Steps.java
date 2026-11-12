package com.academia.puntomarcial.bdd.sprint5;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.academia.puntomarcial.bdd.comun.ApiPrueba;
import com.academia.puntomarcial.bdd.comun.ApiPrueba.ClientePrueba;
import com.academia.puntomarcial.bdd.comun.Navegador;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps del Sprint 5 (HU-10, HU-11 y HU-12).
 * Cada método corresponde a una línea de features/sprint5/HU10_HU12_pago_boleta.feature
 */
public class Sprint5Steps {

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

    @Given("que inicié sesión como un cliente nuevo")
    public void queInicieSesionComoUnClienteNuevo() throws InterruptedException {
        //Preparar datos: cliente nuevo para no llenar las compras de los clientes de ejemplo
        ClientePrueba cliente = ApiPrueba.crearCliente();

        driver.get(Navegador.URL + "/login");
        Thread.sleep(2000);

        //Caja de texto del correo y la contraseña
        driver.findElement(By.id("correo")).sendKeys(cliente.correo());
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(cliente.clave());
        Thread.sleep(2000);

        //Botón INGRESAR
        driver.findElement(By.cssSelector("button.login-button")).click();
        wait.until(ExpectedConditions.urlContains("/menu"));
        Thread.sleep(2000);
    }

    @Given("agregué {int} unidad de {string} al carrito")
    public void agregueAlCarrito(int cantidad, String producto) throws InterruptedException {
        driver.get(Navegador.URL + "/comprar-productos");
        Thread.sleep(2000);

        //Filtro de disciplina "General"
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filtro-disciplina"))))
                .selectByValue("GENERAL");
        Thread.sleep(2000);

        //Tarjeta del producto: cantidad y botón AGREGAR
        WebElement tarjeta = driver.findElement(By.xpath(
                "//li[contains(@class,'product-card')][.//h3[normalize-space()='" + producto + "']]"));
        WebElement cajaCantidad = tarjeta.findElement(By.cssSelector("input[type='number']"));
        cajaCantidad.clear();
        cajaCantidad.sendKeys(String.valueOf(cantidad));
        Thread.sleep(2000);

        tarjeta.findElement(By.cssSelector("button.add-button")).click();
        Thread.sleep(2000);
    }

    @Given("continué al pago")
    public void continueAlPago() throws InterruptedException {
        //Botón CONTINUAR AL PAGO
        driver.findElement(By.xpath("//button[contains(.,'CONTINUAR AL PAGO')]")).click();
        wait.until(ExpectedConditions.urlContains("/pago"));
        Thread.sleep(2000);
    }

    @When("pago con la tarjeta {string}, vencimiento {string} y CVV {string}")
    public void pagoConLaTarjeta(String numero, String vencimiento, String cvv) throws InterruptedException {
        //Cajas de texto de la tarjeta
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pago-titular"))).sendKeys("Cliente Selenium");
        Thread.sleep(2000);
        driver.findElement(By.id("pago-numeroTarjeta")).sendKeys(numero);
        Thread.sleep(2000);
        driver.findElement(By.id("pago-vencimiento")).sendKeys(vencimiento);   // la pantalla agrega la barra: 12/29
        Thread.sleep(2000);
        driver.findElement(By.id("pago-cvv")).sendKeys(cvv);
        Thread.sleep(2000);

        //Botón PAGAR
        driver.findElement(By.cssSelector("button.pay-button")).click();
        Thread.sleep(2000);
    }

    @Then("veo el mensaje del pago {string}")
    public void veoElMensajeDelPago(String esperado) {
        //Espera hasta que salga el resultado
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".result-message")));

        //Obtener el texto
        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertEquals(esperado, mensaje);
    }

    @Then("veo el botón {string}")
    public void veoElBoton(String esperado) {
        WebElement boton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(.,'BOLETA')]")));

        String texto = boton.getText();
        System.out.println("Botón obtenido: " + texto);

        //Validación (el botón empieza con un ícono de descarga)
        assertTrue(texto.endsWith(esperado));
    }

    @Then("veo el error de la tarjeta {string}")
    public void veoElErrorDeLaTarjeta(String esperado) {
        //Espera hasta que salga el error debajo del número de tarjeta
        WebElement elementoError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("error-numeroTarjeta")));

        String mensaje = elementoError.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación: se muestra el error y la compra no se registró
        assertEquals(esperado, mensaje);
        assertTrue(driver.findElements(By.id("titulo-resultado")).isEmpty());
    }
}