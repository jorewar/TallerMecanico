package org.iesalandalus.programacion.tallermecanico.vista.graficas.utilidades;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Controlador {

	private Stage escenario;
	
	public Stage getEscenario() {
		return escenario;
	}
	
	public void setEscenario(Stage escenario) {
		Objects.requireNonNull(escenario, "ERROR: El escenario no puede ser nulo.");
		this.escenario = escenario;
	}

	public void addHojaEstilos(String hojaEstilos) {
		getEscenario().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource(hojaEstilos)).toExternalForm());
	}

	public void addIcono(String icono) {
		getEscenario().getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(icono))));
	}
	
}
