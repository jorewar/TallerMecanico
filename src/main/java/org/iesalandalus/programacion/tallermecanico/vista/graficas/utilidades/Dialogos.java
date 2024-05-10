package org.iesalandalus.programacion.tallermecanico.vista.graficas.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class Dialogos {

	private static final String ID_BT_ACEPTAR = "btAceptar";
	private static final String ID_BT_CANCELAR = "btCancelar";

	private Dialogos() {
	}

	public static void mostrarDialogoError(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.ERROR);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static void mostrarDialogoInformacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.INFORMATION);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static void mostrarDialogoAdvertencia(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.WARNING);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		dialogo.showAndWait();
	}

	public static boolean mostrarDialogoConfirmacion(String titulo, String contenido, Stage propietario) {
		Alert dialogo = new Alert(AlertType.CONFIRMATION);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.getDialogPane().lookupButton(ButtonType.CANCEL).setId(ID_BT_CANCELAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);

		Optional<ButtonType> respuesta = dialogo.showAndWait();
		return (respuesta.isPresent() && respuesta.get() == ButtonType.OK);
	}
	
	public static String mostrarDialogoTexto(String titulo, String contenido, Stage propietario, String expresionRegular) {
		TextInputDialog dialogo = new TextInputDialog();
		dialogo.getEditor().textProperty().addListener((ob, ov, nv) -> Controles.validarCampoTexto(expresionRegular, dialogo.getEditor()));
		dialogo.setGraphic(null);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.getDialogPane().lookupButton(ButtonType.CANCEL).setId(ID_BT_CANCELAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		
		Optional<String> respuesta = dialogo.showAndWait();
		return (respuesta.orElse(null));
	}

	public static <T> T mostrarDialogoEleccion(List<T> opciones, String titulo, String contenido, Stage propietario) {
		ChoiceDialog<T> dialogo = new ChoiceDialog<>(opciones.get(1), opciones);
		dialogo.getDialogPane().lookupButton(ButtonType.OK).setId(ID_BT_ACEPTAR);
		dialogo.getDialogPane().lookupButton(ButtonType.CANCEL).setId(ID_BT_CANCELAR);
		dialogo.setTitle(titulo);
		dialogo.setHeaderText(null);
		dialogo.setContentText(contenido);
		dialogo.initModality(Modality.APPLICATION_MODAL);
		dialogo.initOwner(propietario);
		Optional<T> respuesta = dialogo.showAndWait();
		return respuesta.orElse(null);
	}

}
