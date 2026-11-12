package com.academia.puntomarcial.bdd.comun;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Opciones para abrir Chrome en los Steps: new ChromeDriver(Navegador.opciones()).
 *
 * En una laptop con Chrome instalado no hace falta configurar nada: funciona igual que
 * new ChromeDriver() de clase. Las opciones solo sirven para casos especiales, por ejemplo:
 *   -Dselenium.headless=true      ejecuta sin mostrar la ventana de Chrome
 */
public class Navegador {

    // Dirección del frontend (Angular) y del backend (Spring Boot)
    public static final String URL = "http://localhost:4200";
    public static final String API = "http://localhost:8081";

    public static ChromeOptions opciones() {
        ChromeOptions opciones = new ChromeOptions();

        if (Boolean.getBoolean("selenium.headless")) {
            opciones.addArguments("--headless=new", "--window-size=1366,900");
        }
        // Ruta de otro Chrome y argumentos extra (separados por coma). Normalmente no se usan.
        String chrome = System.getProperty("selenium.chrome");
        if (chrome != null) {
            opciones.setBinary(chrome);
        }
        for (String argumento : System.getProperty("selenium.args", "").split(",")) {
            if (!argumento.isBlank()) {
                opciones.addArguments(argumento.trim());
            }
        }
        return opciones;
    }
}