package org.iesalandalus.programacion.tallermecanico.vista;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.GestorEventos;

import java.time.LocalDate;
import java.util.List;

public interface Vista {
    GestorEventos getGestorEventos();

    void comenzar();

    void terminar();

    Cliente leerCliente();

    Cliente leerClienteDni();

    String leerNuevoNombre();

    String leerNuevoTelefono();

    Vehiculo leerVehiculo();

    Vehiculo leerVehiculoMatricula();

    Trabajo leerRevision();

    Trabajo leerMecanico();

    Trabajo leerTrabajoVehiculo();

    int leerHoras();

    float leerPrecioMaterial();

    LocalDate leerFechaCierre();

    void notificarResultado(Evento evento, String texto, boolean exito);

    void mostrarCliente(Cliente cliente);

    void mostrarVehiculo(Vehiculo vehiculo);

    void mostrarTrabajo(Trabajo trabajo);

    void mostrarClientes(List<Cliente> clientes);

    void mostrarVehiculos(List<Vehiculo> vehiculos);

    void mostrarTrabajos(List<Trabajo> trabajos);
}