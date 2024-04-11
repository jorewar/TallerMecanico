package org.iesalandalus.programacion.tallermecanico.vista.texto;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.vista.Vista;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.GestorEventos;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class VistaTexto implements Vista {

    private GestorEventos gestorEventos = new GestorEventos(Evento.values());

    @Override
    public GestorEventos getGestorEventos() {
        return gestorEventos;
    }

    @Override
    public void comenzar() {
        Evento evento;
        do {
            Consola.mostrarMenu();
            evento = Consola.elegirOpcion();
            ejecutar(evento);
        } while (evento != Evento.SALIR);
    }

    @Override
    public void terminar() {
        Consola.mostrarCabecera("¡Gracias por utilizar nuestra aplicación! Esperamos haber hecho tu experiencia más fácil y agradable. ¡Hasta pronto!");
    }

    private void ejecutar(Evento evento) {
        switch (evento) {
            case INSERTAR_CLIENTE -> Consola.mostrarCabecera("INSERCIÓN DE CLIENTE ACTIVADA");
            case BUSCAR_CLIENTE -> Consola.mostrarCabecera("BÚSQUEDA DE CLIENTE ACTIVADA");
            case BORRAR_CLIENTE -> Consola.mostrarCabecera("ELIMINACIÓN DE CLIENTE ACTIVADA");
            case MODIFICAR_CLIENTE -> Consola.mostrarCabecera("MODIFICACIÓN DE CLIENTE ACTIVADA");
            case LISTAR_CLIENTES -> Consola.mostrarCabecera("LISTADO DE CLIENTES ACTIVADA");
            case INSERTAR_VEHICULO -> Consola.mostrarCabecera("INSERCIÓN DE VEHÍCULO ACTIVADA");
            case BUSCAR_VEHICULO -> Consola.mostrarCabecera("BÚSQUEDA DE VEHÍCULO ACTIVADA");
            case BORRAR_VEHICULO -> Consola.mostrarCabecera("ELIMINACIÓN DE VEHÍCULO ACTIVADA");
            case LISTAR_VEHICULOS -> Consola.mostrarCabecera("LISTADO DE VEHÍCULOS ACTIVADA");
            case INSERTAR_REVISION -> Consola.mostrarCabecera("INSERCIÓN DE REVISIÓN ACTIVADA");
            case INSERTAR_MECANICO -> Consola.mostrarCabecera("INSERCIÓN DE TRABAJO MECÁNICO ACTIVADA");
            case BUSCAR_TRABAJO -> Consola.mostrarCabecera("BÚSQUEDA DE TRABAJO ACTIVADA");
            case BORRAR_TRABAJO -> Consola.mostrarCabecera("ELIMINACIÓN DE TRABAJO ACTIVADA");
            case LISTAR_TRABAJOS -> Consola.mostrarCabecera("LISTADO DE TRABAJOS ACTIVADA");
            case LISTAR_TRABAJOS_CLIENTE -> Consola.mostrarCabecera("LISTADO DE TRABAJOS POR CLIENTE ACTIVADA");
            case LISTAR_TRABAJOS_VEHICULO -> Consola.mostrarCabecera("LISTADO DE TRABAJOS POR VEHÍCULO ACTIVADA");
            case ANADIR_HORAS_TRABAJO -> Consola.mostrarCabecera("AÑADIR HORAS A TRABAJO ACTIVADA");
            case ANADIR_PRECIO_MATERIAL_TRABAJO -> Consola.mostrarCabecera("AÑADIR PRECIO MATERIAL A TRABAJO ACTIVADA");
            case CERRAR_TRABAJO -> Consola.mostrarCabecera("CIERRE DE TRABAJO ACTIVADA");
            case SALIR -> Consola.mostrarCabecera("SALIENDO DEL SISTEMA ACTIVADA");
        }
        gestorEventos.notificar(evento);
    }

    @Override
    public Cliente leerCliente() {
        System.out.println("Introduzca los datos del cliente.");
        return new Cliente(Consola.leerCadena("Ingrese el nombre: "), Consola.leerCadena("Ingrese el DNI: "), Consola.leerCadena("Ingrese el teléfono: "));
    }

    @Override
    public Cliente leerClienteDni() {
        return new Cliente(Cliente.get(Consola.leerCadena("Ingrese el DNI: ")));
    }

    @Override
    public String leerNuevoNombre() {
        return Consola.leerCadena("Ingrese el nombre: ");
    }

    @Override
    public String leerNuevoTelefono() {
        return Consola.leerCadena("Ingrese el teléfono: ");
    }

    @Override
    public Vehiculo leerVehiculo() {
        System.out.println("Introduzca los datos del vehículo.");
        return new Vehiculo(Consola.leerCadena("Ingrese la marca: "), Consola.leerCadena("Ingrese el modelo: "), Consola.leerCadena("Ingrese la matricula: "));
    }

    @Override
    public Vehiculo leerVehiculoMatricula() {
        return Vehiculo.get(Consola.leerCadena("Ingrese la matrícula: "));
    }

    @Override
    public Trabajo leerRevision() {
        System.out.println("Introduzca los datos de la revisión.");
        return new Revision(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio de la revisión: ")));
    }

    @Override
    public Trabajo leerMecanico() {
        System.out.println("Introduzca los datos del trabajo mecánico.");
        return new Mecanico(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio del trabajo mecánico: ")));
    }

    @Override
    public Trabajo leerTrabajoVehiculo() {
        System.out.println("Introduzca los datos del vehiculo al cual pertenece el trabajo.");
        return new Revision((Revision) Trabajo.get(leerVehiculoMatricula()));
    }

    @Override
    public int leerHoras() {
        return Consola.leerEntero("Introduzca las horas que desee añadir a el trabajo: ");
    }

    @Override
    public float leerPrecioMaterial() {
        return Consola.leerReal("Introduzca el precio que desee añadir al material del trabajo: ");
    }

    @Override
    public LocalDate leerFechaCierre() {
        return LocalDate.parse(Consola.leerFecha("Introduzca la fecha de cierre del trabajo: "));
    }

    @Override
    public void notificarResultado(Evento evento, String texto, boolean exito) {
        Objects.requireNonNull(evento, "No se puede obtener un resultado de un evento nulo.");
        Objects.requireNonNull(texto, "No se puede obtener ningún resultado con un textowwww nulo.");
        // Consola.mostrarCabezera(evento.name())
        if (exito) {
            System.out.println(texto);
        } else {
            System.out.printf("ERROR: %s%n", texto);
        }
    }

    @Override
    public void mostrarCliente(Cliente cliente) {
        if (cliente == null) {
            System.out.println("No se ha encontrado ningún cliente con esa información. Por favor, inténtalo de nuevo.");
        } else {
            String.format("¡Cliente encontrado! A continuación se muestran los detalles: %s%n", cliente);
        }
    }

    @Override
    public void mostrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            System.out.println("No se ha encontrado ningún vehículo con esa información. Por favor, inténtalo de nuevo.");
        } else {
            String.format("¡Vehículo encontrado! A continuación se muestran los detalles: %s%n", vehiculo);
        }
    }

    @Override
    public void mostrarTrabajo(Trabajo trabajo) {
        if (trabajo == null) {
            System.out.println("No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo.");
        } else {
            String.format("¡Trabajo encontrado! A continuación se muestran los detalles: %s%n", trabajo);
        }
    }

    @Override
    public void mostrarClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No se encontraron clientes en la lista. ¡Registra un nuevo cliente para comenzar!");
        } else {
            System.out.println("Mostrando lista de clientes disponibles: ");
            int incrementadorNumeroLista = 1;
            for (Cliente cliente : clientes) {
                System.out.printf("%s. %s%n", incrementadorNumeroLista++, cliente);
            }
        }
    }

    @Override
    public void mostrarVehiculos(List<Vehiculo> vehiculos) {
        if (vehiculos.isEmpty()) {
            System.out.println("No se encontraron vehículos en la lista. ¡Registra un nuevo vehículo para comenzar!");
        } else {
            System.out.println("Mostrando lista de vehículos disponibles: ");
            int incrementadorNumeroLista = 1;
            for (Vehiculo vehiculo : vehiculos) {
                System.out.printf("%s. %s%n", incrementadorNumeroLista++, vehiculo);
            }
        }
    }

    @Override
    public void mostrarTrabajos(List<Trabajo> trabajos) {
        if (trabajos.isEmpty()) {
            System.out.println("No se encontraron trabajos en la lista. ¡Registra un nuevo trabajo para comenzar!");
        } else {
            System.out.println("Mostrando lista de trabajos disponibles: ");
            int incrementadorNumeroLista = 1;
            for (Trabajo trabajo : trabajos) {
                System.out.printf("%s. %s%n", incrementadorNumeroLista++, trabajo);
            }
        }
    }
}