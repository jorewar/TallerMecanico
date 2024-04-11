package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clientes {
    private final List<Cliente> coleccionClientes;

    public Clientes() {
        coleccionClientes = new ArrayList<>();
    }

    public List<Cliente> get() {
        return new ArrayList<>(coleccionClientes);
    }

    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede insertar un cliente nulo.");
        if (coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("Ya existe un cliente con ese DNI.");
        }
        coleccionClientes.add(cliente);
    }

    public boolean modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede modificar un cliente nulo.");
        if (!coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("No existe ningún cliente con ese DNI.");
        } else {
            boolean haSidoModificado = false;
            if (nombre != null && !nombre.isBlank()) {
                coleccionClientes.get(coleccionClientes.indexOf(cliente)).setNombre(nombre);
                haSidoModificado = true;
            }
            if (telefono != null && !telefono.isBlank()) {
                coleccionClientes.get(coleccionClientes.indexOf(cliente)).setTelefono(telefono);
                haSidoModificado = true;
            }
            return haSidoModificado;
        }
    }

    public Cliente buscar(Cliente cliente) {
        Objects.requireNonNull(cliente, "No se puede buscar un cliente nulo.");
        return (coleccionClientes.contains(cliente)) ? coleccionClientes.get(coleccionClientes.indexOf(cliente)) : null;
    }

    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede borrar un cliente nulo.");
        if (!coleccionClientes.contains(cliente)) {
            throw new OperationNotSupportedException("No existe ningún cliente con ese DNI.");
        } else {
            coleccionClientes.remove(coleccionClientes.get(coleccionClientes.indexOf(cliente)));
        }
    }
}