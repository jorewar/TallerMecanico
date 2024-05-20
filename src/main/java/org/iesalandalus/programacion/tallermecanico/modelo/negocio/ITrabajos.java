package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.TipoTrabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ITrabajos {
    void comenzar();

    void terminar();

    List<Trabajo> get();

    List<Trabajo> get(Cliente cliente);

    List<Trabajo> get(Vehiculo vehiculo);

    Map<TipoTrabajo, Integer> getEstadisticasMensuales(LocalDate mes);

    void insertar(Trabajo trabajo) throws OperationNotSupportedException;

    void anadirHoras(Trabajo trabajo, int horas) throws OperationNotSupportedException;

    void anadirPrecioMaterial(Trabajo trabajo, float precioMaterial) throws OperationNotSupportedException;

    void cerrar(Trabajo trabajo, LocalDate fechaFin) throws OperationNotSupportedException;

    Trabajo buscar(Trabajo trabajo);

    void borrar(Trabajo trabajo) throws OperationNotSupportedException;
}
