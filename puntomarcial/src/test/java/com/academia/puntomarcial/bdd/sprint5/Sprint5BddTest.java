package com.academia.puntomarcial.bdd.sprint5;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Runner del Sprint 5: Cucumber lee los escenarios de features/sprint5 (Gherkin)
 * y los ejecuta con los Steps de este paquete (Selenium).
 * Para ejecutarlo: botón ▶ de esta clase en VS Code (backend, frontend y XAMPP encendidos).
 * Reporte: target/cucumber/sprint5.html
 */
@Suite(failIfNoTests = false)
@IncludeEngines("cucumber")
@SelectPackages("features.sprint5")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.academia.puntomarcial.bdd.sprint5")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber/sprint5.html")
public class Sprint5BddTest {
}