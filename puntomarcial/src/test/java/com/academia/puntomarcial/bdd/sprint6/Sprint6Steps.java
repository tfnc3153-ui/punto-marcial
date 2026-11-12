package com.academia.puntomarcial.bdd.sprint6;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.academia.puntomarcial.bdd.comun.ApiPrueba;
import com.academia.puntomarcial.bdd.comun.ApiPrueba.ClasePrueba;
import com.academia.puntomarcial.bdd.comun.Navegador;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps del Sprint 6 (HU-13 y HU-04).
 * Cada método corresponde a una línea de features/sprint6/HU13_HU04_instructor_clases.feature
 */
public class Sprint6Steps {

    WebDriver driver;
    WebDriverWait wait;
    ClasePrueba claseSinReservas;
    WebElement avisoClase;

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

    @Given("que existe una clase de Box sin reservas")
    public void queExisteUnaClaseSinReservas() {
        //Preparar datos: clase nueva en una fecha lejana, creada directamente en el backend
        claseSinReservas = ApiPrueba.crearClaseSinReservas();
        System.out.println("Clase creada: " + claseSinReservas.inicio() + " sala " + claseSinReservas.sala());
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

    @When("marco la asistencia del primer alumno que no la tenga marcada")
    public void marcoLaAsistencia() throws InterruptedException {
        // Se elige el botón (Asistió o No asistió) que todavía no está marcado,
        // así cada ejecución cambia el valor y el escenario se puede repetir
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn-asistencia[aria-pressed='false']"))).click();
        Thread.sleep(2000);
    }

    @When("elimino esa clase y acepto la confirmación")
    public void eliminoEsaClase() throws InterruptedException {
        String fecha = claseSinReservas.inicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        //Botón ELIMINAR de la clase creada
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[aria-label='Eliminar la clase de Box del " + fecha + "']"))).click();
        Thread.sleep(2000);

        //Cuadro "¿Eliminar la clase de ...?" -> Aceptar
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        Thread.sleep(2000);
    }

    @Then("veo el mensaje de asistencia guardada")
    public void veoElMensajeDeAsistencia() {
        //Espera hasta que salga el mensaje, por ejemplo "María Flores: asistió."
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".feedback.success")));

        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertTrue(mensaje.endsWith("asistió."));
    }

    @Then("la clase {int} muestra el aviso {string}")
    public void laClaseMuestraElAviso(int idClase, String esperado) {
        //Espera hasta que salga el aviso de la clase
        avisoClase = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nota-" + idClase)));

        String texto = avisoClase.getText();
        System.out.println("Aviso obtenido: " + texto);

        //Validación (el aviso empieza con un ícono de candado)
        assertTrue(texto.endsWith(esperado));
    }

    @Then("su botón ELIMINAR está deshabilitado")
    public void suBotonEliminarEstaDeshabilitado() {
        WebElement eliminar = avisoClase.findElement(
                By.xpath("./ancestor::article//button[contains(@class,'delete')]"));

        //Validación
        assertFalse(eliminar.isEnabled());
    }

    @Then("veo el mensaje de que la clase se eliminó")
    public void veoElMensajeDeClaseEliminada() {
        //Espera hasta que salga el mensaje
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".feedback.success")));

        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        String esperado = "Se eliminó la clase de Box del " + claseSinReservas.inicio().toLocalDate()
                + " en la sala " + claseSinReservas.sala() + ".";
        assertEquals(esperado, mensaje);
    }
}