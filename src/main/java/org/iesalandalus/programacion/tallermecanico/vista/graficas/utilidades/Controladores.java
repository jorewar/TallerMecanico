package org.iesalandalus.programacion.tallermecanico.vista.graficas.utilidades;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Controladores {

	private static final Map<String, Controlador> controladoresCreados = new HashMap<>();

	private Controladores() {
	}

	public static Controlador get(String vistaFxml, String titulo, Stage propietario) {
		return controladoresCreados.containsKey(vistaFxml) ? controladoresCreados.get(vistaFxml) : crear(vistaFxml, titulo, propietario);
	}

	private static <FXMLLoader, Parent, Stage> Controlador crear(String vistaFxml, String titulo, Stage propietario) {
		Controlador controlador = null;
		try {
			FXMLLoader cargador = new FXMLLoader(Controladores.class.getResource(vistaFxml));
			Parent raiz = cargador.load();
			controlador = cargador.getController();
			Stage escenario = new Stage();
			controlador.setEscenario(escenario);
			escenario.initModality(Modality.APPLICATION_MODAL);
			escenario.initOwner(propietario);
			escenario.setTitle(titulo);
			escenario.setScene(new Scene(raiz));
			controladoresCreados.put(vistaFxml, controlador);
		} catch (IOException e) {
			System.out.printf("Error al cargar: %s%n", vistaFxml);
		}
		return controlador;
	}
}