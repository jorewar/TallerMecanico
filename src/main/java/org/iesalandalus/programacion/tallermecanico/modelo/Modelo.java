package org.iesalandalus.programacion.tallermecanico.modelo;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.List;

public interface Modelo {
    void comenzar();

    void terminar();

    void insertar(Cliente cliente) throws OperationNotSupportedException;

    void insertar(Vehiculo vehiculo) throws OperationNotSupportedException;

    void insertar(Trabajo trabajo) throws OperationNotSupportedException;

    Cliente buscar(Cliente cliente);

    Vehiculo buscar(Vehiculo vehiculo);

    Trabajo buscar(Trabajo trabajo);

    boolean modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException;

    void anadirHoras(Trabajo trabajo, int horas) throws OperationNotSupportedException;

    void anadirPrecioMaterial(Trabajo trabajo, float precioMaterial) throws OperationNotSupportedException;

    void cerrar(Trabajo trabajo, LocalDate fechaFin) throws OperationNotSupportedException;

    void borrar(Cliente cliente) throws OperationNotSupportedException;

    void borrar(Vehiculo vehiculo) throws OperationNotSupportedException;

    void borrar(Trabajo trabajo) throws OperationNotSupportedException;

    List<Cliente> getClientes();

    List<Vehiculo> getVehiculos();

    List<Trabajo> getTrabajos();

    List<Trabajo> getTrabajos(Cliente cliente);

    List<Trabajo> getTrabajos(Vehiculo vehiculo);
}
