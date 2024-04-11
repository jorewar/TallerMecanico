package org.iesalandalus.programacion.tallermecanico;

import org.iesalandalus.programacion.tallermecanico.controlador.Controlador;
import org.iesalandalus.programacion.tallermecanico.modelo.cascada.ModeloCascada;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.FabricaFuenteDatos;
import org.iesalandalus.programacion.tallermecanico.vista.Vista;
import org.iesalandalus.programacion.tallermecanico.vista.texto.VistaTexto;

public class Main {
    public static void main(String[] args) {
        ModeloCascada modeloCascada = new ModeloCascada(FabricaFuenteDatos.MEMORIA);
        Vista vista = new VistaTexto();
        Controlador controlador = new Controlador(modeloCascada, vista);
        controlador.comenzar();
    }
}
