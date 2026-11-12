package com.academia.puntomarcial.bdd.comun;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Inicio de sesión compartido por los Steps de los Sprints 2 a 6.
 * Antes este código estaba copiado en cada clase de Steps; si cambia la pantalla de login,
 * ahora se corrige solo aquí (refactorización de la PC 12).
 */
public final class Login {

    private Login() {
    }

    public static void iniciarSesion(WebDriver driver, WebDriverWait wait, String correo, String clave)
            throws InterruptedException {
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
}
