package org.iesalandalus.programacion.tallermecanico.vista.texto;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.utilidades.Entrada;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Consola {

    private static final String CADENA_FORMATO_FECHA = "^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})-(?:(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8])|(?:0?[13-9]|1[0-2])-(?:29|30)|(?:0?[13578]|1[02])-31)$|^(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))-02-29$";

    private Consola() {
    }

    static void mostrarCabecera(String mensaje) {
        System.out.printf("%s%n%s%n%s%n", "-".repeat(mensaje.length()), mensaje, "-".repeat(mensaje.length()));
    }

    static void mostrarMenu() {
        mostrarCabecera("Aplicación del taller mecánico. Uso para la gestión de clientes, vehículos y trabajos");
        for (Evento evento : Evento.values()) {
            System.out.println(evento.toString());
        }
    }

    static Evento elegirOpcion() {
        return Evento.get(leerEntero("Ingrese el número correspondiente a su opción elegida del menú: "));
    }

    static float leerReal(String mensaje) {
        System.out.print(mensaje);
        return Entrada.real();
    }

    static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Entrada.entero();
    }

    static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return Entrada.cadena();
    }

    static String leerFecha(String mensaje) {
        Pattern patron = Pattern.compile(CADENA_FORMATO_FECHA);
        String entradaFecha;
        do {
            System.out.print(mensaje);
            entradaFecha = Entrada.cadena();
            if (!patron.matcher(entradaFecha).matches()) {
                System.out.println("ERROR: Formato de fecha(YYYY-MM-DD) no valido.");
            }
        } while (!patron.matcher(entradaFecha).matches());
        return entradaFecha;
    }
}