package org.iesalandalus.programacion.tallermecanico;

import javafx.util.Pair;
import org.iesalandalus.programacion.tallermecanico.controlador.Controlador;
import org.iesalandalus.programacion.tallermecanico.controlador.IControlador;
import org.iesalandalus.programacion.tallermecanico.modelo.FabricaModelo;
import org.iesalandalus.programacion.tallermecanico.modelo.Modelo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.FabricaFuenteDatos;
import org.iesalandalus.programacion.tallermecanico.vista.FabricaVista;
import org.iesalandalus.programacion.tallermecanico.vista.Vista;

public class Main {
    public static void main(String[] args, FabricaVista FabricaVista) {
        FabricaFuenteDatos FabricaFuenteDatos = null;
        Pair<FabricaVista, FabricaFuenteDatos> fabricas = procesarArgumentos(args, FabricaVista, FabricaFuenteDatos);
        Modelo modelo = null;
        Vista vista = null;
        IControlador controlador = new Controlador(FabricaModelo.CASCADA, (FabricaFuenteDatos.MEMORIA), FabricaVista.GRAFICAS.crear(), modelo, vista);
        controlador.comenzar();
    }


    private static Pair<FabricaVista, FabricaFuenteDatos> procesarArgumentos(String[] args, FabricaVista FabricaVista, FabricaFuenteDatos FabricaFuenteDatos){
        FabricaVista fabricaVista = FabricaVista.GRAFICA;
        FabricaFuenteDatos fabricaFuenteDatos = FabricaFuenteDatos.MARIADB;
        for (String argumento : args){
            if (argumento.equalsIgnoreCase("-vventanas")){
                fabricaVista = FabricaVista.GRAFICA;
            } else if (argumento.equalsIgnoreCase("-vtexto")) {
                fabricaVista = FabricaVista.TEXTO;
            } else if (argumento.equalsIgnoreCase("-fdficheros")) {
                fabricaFuenteDatos = FabricaFuenteDatos.FICHEROS;
            } else if (argumento.equalsIgnoreCase("-fdmariadb")) {
                fabricaFuenteDatos = FabricaFuenteDatos.MARIADB;
            }
        }
        return new Pair<>(FabricaVista, FabricaFuenteDatos);
}



}
