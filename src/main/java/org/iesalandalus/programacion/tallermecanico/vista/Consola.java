package org.iesalandalus.programacion.tallermecanico.vista;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Revision;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.utilidades.Entrada;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class Consola {

    private static final String CADENA_FORMATO_FECHA ="^(?:(?:1[6-9]|[2-9]\\d)?\\d{2})-(?:(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8])|(?:0?[13-9]|1[0-2])-(?:29|30)|(?:0?[13578]|1[02])-31)$|^(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))-02-29$";
    private Consola() {
    }

    public static void mostrarCabecera(String mensaje) {
        System.out.printf("%s%n%s%n%s%n", "-".repeat(mensaje.length()), mensaje, "-".repeat(mensaje.length()));
    }

    public static void mostrarMenu() {
        mostrarCabecera("Aplicación del taller mecánico. Uso para la gestión de clientes, vehículos y revisiones");
        for (Opcion opcion : Opcion.values()) {
            System.out.println(opcion.toString());
        }
    }

    private static float leerReal(String mensaje) {
        System.out.print(mensaje);
        return Entrada.real();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return Entrada.entero();
    }

    private static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return Entrada.cadena();
    }

    public static String leerFecha(String mensaje) {
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


    public static Opcion elegirOpcion() {
        return Opcion.get(leerEntero("Ingrese el número correspondiente a su opción elegida del menú: "));
    }

    public static Cliente leerCliente() {
        System.out.println("Introduzca los datos del cliente.");
        return new Cliente(leerCadena("Ingrese el nombre: "), leerCadena("Ingrese el DNI: "), leerCadena("Ingrese el teléfono: "));
    }

    public static Cliente leerClienteDni() {
        return new Cliente(Cliente.get(leerCadena("Ingrese el DNI: ")));
    }

    public static String leerClienteNombre() {
        return leerCadena("Ingrese el nombre: ");
    }

    public static String leerClienteTelefono() {
        return leerCadena("Ingrese el teléfono: ");
    }

    public static Vehiculo leerVehiculo() {
        System.out.println("Introduzca los datos del vehículo.");
        return new Vehiculo(leerCadena("Ingrese la marca: "), leerCadena("Ingrese el modelo: "), leerCadena("Ingrese la matricula: "));
    }

    public static Vehiculo leerVehiculoMatricula() {
        return Vehiculo.get(leerCadena("Ingrese la matrícula: "));
    }

    public static Revision leerRevision() {
        System.out.println("Introduzca los datos de la revisión.");
        return new Revision(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(leerFecha("Ingrese la fecha de inicio de la revisión: ")));
    }

    public static int leerHoras() {
        return leerEntero("Introduzca las horas que desee añadir a la revisión: ");
    }

    public static float leerPrecioMaterial() {
        return leerReal("Introduzca el precio que desee añadir al material de la revisión: ");
    }

    public static LocalDate leerFechaCierre() {
        return LocalDate.parse(leerFecha("Introduzca la fecha de cierre de la revisión: "));
    }
}