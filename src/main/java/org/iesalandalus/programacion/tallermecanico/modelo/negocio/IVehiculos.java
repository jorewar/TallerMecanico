package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface IVehiculos {
    List<Vehiculo> get();

    void insertar(Vehiculo vehiculo) throws OperationNotSupportedException;

    Vehiculo buscar(Vehiculo vehiculo);

    void borrar(Vehiculo vehiculo) throws OperationNotSupportedException;
}
