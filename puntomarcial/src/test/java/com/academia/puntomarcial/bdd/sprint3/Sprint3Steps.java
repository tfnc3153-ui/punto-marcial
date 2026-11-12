package com.academia.puntomarcial.bdd.sprint3;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

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
 * Steps del Sprint 3 (HU-06 y HU-07).
 * Cada método corresponde a una línea de features/sprint3/HU06_HU07_cancelacion_aforo.feature
 */
public class Sprint3Steps {

    WebDriver driver;
    WebDriverWait wait;
    ClientePrueba clienteNuevo;
    int idReserva;

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

    @Given("que soy un cliente nuevo con una reserva en la clase {int}")
    public void queSoyUnClienteNuevoConUnaReserva(int idClase) {
        //Preparar datos: cliente nuevo con una sola reserva, hecha directamente en el backend
        clienteNuevo = ApiPrueba.crearCliente();
        idReserva = ApiPrueba.reservar(clienteNuevo.id(), idClase);
        System.out.println("Reserva creada: #" + idReserva);
    }

    @Given("que inicié sesión con ese cliente")
    public void queInicieSesionConEseCliente() throws InterruptedException {
        queInicieSesion(clienteNuevo.correo(), clienteNuevo.clave());
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

    @When("cancelo la reserva y acepto la confirmación")
    public void canceloLaReserva() throws InterruptedException {
        //Botón CANCELAR RESERVA
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.cancel-button"))).click();
        Thread.sleep(2000);

        //Cuadro "¿Deseas cancelar la reserva #...?" -> Aceptar
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        Thread.sleep(2000);
    }

    @Then("veo el mensaje de que esa reserva fue cancelada")
    public void veoElMensajeDeCancelacion() {
        //Espera hasta que salga el mensaje
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".notice.success")));

        //Obtener el texto
        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertEquals("La reserva #" + idReserva + " fue cancelada correctamente.", mensaje);
    }

    @Then("ya no veo reservas para cancelar")
    public void yaNoVeoReservasParaCancelar() {
        // La reserva sigue en la base de datos como CANCELADA, pero ya no se muestra al cliente
        int botones = driver.findElements(By.cssSelector("button.cancel-button")).size();
        System.out.println("Botones de cancelar en pantalla: " + botones);

        //Validación
        assertEquals(0, botones);
    }

    @Then("veo el título {string}")
    public void veoElTitulo(String esperado) {
        //Espera hasta que salga el título
        WebElement titulo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("titulo-aforo")));

        String texto = titulo.getText();
        System.out.println("Título obtenido: " + texto);

        //Validación
        assertEquals(esperado, texto);
    }

    @Then("cada clase muestra su barra de ocupación")
    public void cadaClaseMuestraSuBarra() {
        //Espera hasta que carguen las clases
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='progressbar']")));

        int clases = driver.findElements(By.cssSelector(".cards article")).size();
        int barras = driver.findElements(By.cssSelector("[role='progressbar']")).size();
        System.out.println("Clases: " + clases + " - Barras de ocupación: " + barras);

        //Validación: una barra por cada clase
        assertTrue(clases > 0);
        assertEquals(clases, barras);
    }
}
