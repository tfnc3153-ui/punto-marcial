package com.academia.puntomarcial.bdd.sprint2;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
 * Steps del Sprint 2 (HU-05).
 * Cada método corresponde a una línea de features/sprint2/HU04_HU05_clases_reservas.feature
 */
public class Sprint2Steps {

    WebDriver driver;
    WebDriverWait wait;
    ClientePrueba clienteNuevo;

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

        // Limpieza: se cancela la reserva del cliente de prueba para devolver el cupo
        if (clienteNuevo != null) {
            ApiPrueba.cancelarReservas(clienteNuevo.id());
        }
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

    @Given("que inicié sesión como un cliente nuevo")
    public void queInicieSesionComoUnClienteNuevo() throws InterruptedException {
        //Preparar datos: cliente nuevo para que nunca tenga ya una reserva en esa clase
        clienteNuevo = ApiPrueba.crearCliente();
        queInicieSesion(clienteNuevo.correo(), clienteNuevo.clave());
    }

    @When("abro la pantalla {string}")
    public void abroLaPantalla(String pantalla) throws InterruptedException {
        driver.get(Navegador.URL + pantalla);
        Thread.sleep(2000);
    }

    @When("selecciono la primera clase con cupos disponibles")
    public void seleccionoLaPrimeraClaseConCupos() throws InterruptedException {
        //Botón SELECCIONAR de la primera clase que tenga cupos
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.select-button:enabled"))).click();
        wait.until(ExpectedConditions.urlContains("/reservar-clase"));
        Thread.sleep(2000);
    }

    @When("confirmo la reserva")
    public void confirmoLaReserva() throws InterruptedException {
        //Botón CONFIRMAR RESERVA
        driver.findElement(By.cssSelector("button.confirm-button")).click();
        Thread.sleep(2000);
    }

    @Then("veo el mensaje de reserva {string}")
    public void veoElMensajeDeReserva(String esperado) {
        //Espera hasta que salga el mensaje
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".reservation-message.success p")));

        //Obtener el texto
        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertEquals(esperado, mensaje);
    }

    @Then("las clases llenas muestran {string} y no se pueden elegir")
    public void lasClasesLlenasNoSePuedenElegir(String texto) {
        //Espera hasta que carguen las clases
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("article.class-row")));

        //Botones de las clases sin cupos
        List<WebElement> botonesLlenos = driver.findElements(By.xpath(
                "//button[contains(@class,'select-button')][contains(normalize-space(.),'" + texto + "')]"));
        System.out.println("Clases llenas encontradas: " + botonesLlenos.size());

        //Validación: hay al menos una (la clase 1004) y ninguna se puede elegir
        assertFalse(botonesLlenos.isEmpty());
        for (WebElement boton : botonesLlenos) {
            assertFalse(boton.isEnabled());
        }
    }
}