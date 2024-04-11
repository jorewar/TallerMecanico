package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;

import javax.naming.OperationNotSupportedException;
import java.util.List;

public interface IClientes {
    List<Cliente> get();

    void insertar(Cliente cliente) throws OperationNotSupportedException;

    boolean modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException;

    Cliente buscar(Cliente cliente);

    void borrar(Cliente cliente) throws OperationNotSupportedException;
}
