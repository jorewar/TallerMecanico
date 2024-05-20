package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mariadb;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;
import org.mariadb.jdbc.Connection;
import org.mariadb.jdbc.Statement;

import javax.naming.OperationNotSupportedException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Clientes implements IClientes {

    private static final String NOMBRE = "nombre";
    private static final String DNI = "dni";
    private static final String TELEFONO = "telefono";

    private Connection conexion;
    private static Clientes instancia;

    static Clientes getInstancia() {
        if (instancia == null) {
            instancia = new Clientes();
        }
        return instancia;
    }

    private Clientes() {}

    @Override
    public void comenzar() {
        conexion = MariaDB.getConexion();
    }

    @Override
    public void terminar() {
        MariaDB.cerrarConexion();
    }

    private Cliente getCliente(ResultSet fila) throws SQLException {
        String nombre = fila.getString(NOMBRE);
        String dni = fila.getString(DNI);
        String telefono = fila.getString(TELEFONO);
        return new Cliente(nombre, dni, telefono);
    }

    private void prepararSentencia(PreparedStatement sentencia, Cliente cliente) throws SQLException {
        sentencia.setString(1, cliente.getNombre());
        sentencia.setString(2, cliente.getDni());
        sentencia.setString(3, cliente.getTelefono());
    }

    @Override
    public List<Cliente> get() {
        List<Cliente> clientes = new ArrayList<>();
        try (Statement sentencia = conexion.createStatement()) {
            ResultSet filas = sentencia.executeQuery("select * from clientes");
            while (filas.next()) {
                clientes.add(getCliente(filas));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return clientes;
    }

    @Override
    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede insertar un cliente nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("insert into clientes values (?, ?, ?)")) {
            prepararSentencia(sentencia, cliente);
            sentencia.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new OperationNotSupportedException("Ya existe un cliente con ese DNI.");
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public boolean modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede modificar un cliente nulo.");
        boolean modificado = false;
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from clientes where dni = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            sentencia.setString(1, cliente.getDni());
            ResultSet filas = sentencia.executeQuery();
            if (filas.first()) {
                if (nombre != null && !nombre.isBlank()) {
                    filas.updateString(NOMBRE, nombre);
                    modificado = true;
                }
                if (telefono != null && !telefono.isBlank()) {
                    filas.updateString(TELEFONO, telefono);
                    modificado = true;
                }
                filas.updateRow();
                cliente.setNombre(filas.getString(NOMBRE));
                cliente.setTelefono(filas.getString(TELEFONO));
            } else {
                throw new OperationNotSupportedException("No existe ningún cliente con ese DNI.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return modificado;
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Objects.requireNonNull(cliente, "No se puede buscar un cliente nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from clientes where dni = ? ")) {
            sentencia.setString(1, cliente.getDni());
            ResultSet filas = sentencia.executeQuery();
            cliente = filas.first() ? getCliente(filas) : null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return cliente;
    }

    @Override
    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        Objects.requireNonNull(cliente, "No se puede borrar un cliente nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("delete from clientes where dni = ?")) {
            sentencia.setString(1, cliente.getDni());
            int filas = sentencia.executeUpdate();
            if (filas == 0) {
                throw new OperationNotSupportedException("No existe ningún cliente con ese DNI.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
