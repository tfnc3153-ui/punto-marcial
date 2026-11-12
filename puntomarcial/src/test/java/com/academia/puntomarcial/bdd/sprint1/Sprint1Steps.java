package com.academia.puntomarcial.bdd.sprint1;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.academia.puntomarcial.bdd.comun.Navegador;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Steps del Sprint 1 (HU-01 y HU-02).
 * Cada método corresponde a una línea de features/sprint1/HU01_HU02_acceso_usuarios.feature
 */
public class Sprint1Steps {

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

    @Given("que estoy en la pantalla de inicio de sesión")
    public void queEstoyEnElLogin() throws InterruptedException {
        driver.get(Navegador.URL + "/login");
        Thread.sleep(2000);
    }

    @Given("que inicié sesión con el correo {string} y la contraseña {string}")
    public void queInicieSesion(String correo, String clave) throws InterruptedException {
        queEstoyEnElLogin();
        ingresoCorreoYContrasena(correo, clave);

        //Espera hasta que el sistema cambie de pantalla
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
        Thread.sleep(2000);
    }

    @When("ingreso el correo {string} y la contraseña {string}")
    public void ingresoCorreoYContrasena(String correo, String clave) throws InterruptedException {
        //Caja de texto del correo
        driver.findElement(By.id("correo")).sendKeys(correo);
        Thread.sleep(2000);

        //Caja de texto de la contraseña
        driver.findElement(By.id("password")).sendKeys(clave);
        Thread.sleep(2000);

        //Botón INGRESAR
        driver.findElement(By.cssSelector("button.login-button")).click();
        Thread.sleep(2000);
    }

    @When("registro un cliente nuevo llamado {string} {string}")
    public void registroUnClienteNuevo(String nombre, String apellido) throws InterruptedException {
        driver.get(Navegador.URL + "/registrar-usuario");
        Thread.sleep(2000);

        // El rol "Cliente" ya viene elegido. DNI, correo y teléfono cambian en cada ejecución para no repetirse.
        String numero = String.valueOf(System.currentTimeMillis());
        String ochoDigitos = numero.substring(numero.length() - 8);

        driver.findElement(By.id("campo-nombre")).sendKeys(nombre);
        Thread.sleep(2000);
        driver.findElement(By.id("campo-apellido")).sendKeys(apellido);
        Thread.sleep(2000);
        driver.findElement(By.id("campo-numDoc")).sendKeys(ochoDigitos);
        Thread.sleep(2000);
        driver.findElement(By.id("campo-correo")).sendKeys("registro" + numero + "@prueba.com");
        Thread.sleep(2000);
        driver.findElement(By.id("campo-telefono")).sendKeys("9" + ochoDigitos);
        Thread.sleep(2000);
        driver.findElement(By.id("campo-password")).sendKeys("Prueba123");
        Thread.sleep(2000);

        //Botón REGISTRAR USUARIO
        driver.findElement(By.cssSelector("button.register-button")).click();
        Thread.sleep(2000);
    }

    @Then("el sistema me lleva a la pantalla {string}")
    public void elSistemaMeLlevaA(String pantalla) {
        //Espera hasta que cambie la dirección
        wait.until(ExpectedConditions.urlContains(pantalla));

        String direccion = driver.getCurrentUrl();
        System.out.println("Pantalla obtenida: " + direccion);

        //Validación
        assertEquals(Navegador.URL + pantalla, direccion);
    }

    @Then("veo el mensaje de error {string}")
    public void veoElMensajeDeError(String esperado) {
        //Espera hasta que salga el mensaje
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".message-error span:last-child")));

        //Obtener el texto
        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertEquals(esperado, mensaje);
    }

    @Then("veo el mensaje de registro {string}")
    public void veoElMensajeDeRegistro(String esperado) {
        //Espera hasta que salga el mensaje
        WebElement elementoMensaje = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".feedback.success p")));

        //Obtener el texto
        String mensaje = elementoMensaje.getText();
        System.out.println("Mensaje obtenido: " + mensaje);

        //Validación
        assertEquals(esperado, mensaje);
    }
}