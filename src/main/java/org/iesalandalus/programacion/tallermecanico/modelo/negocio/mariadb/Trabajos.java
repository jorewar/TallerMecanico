package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mariadb;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ITrabajos;
import org.mariadb.jdbc.Connection;

import javax.naming.OperationNotSupportedException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class Trabajos implements ITrabajos {

    private static final String CLIENTE = "cliente";
    private static final String VEHICULO = "vehiculo";
    private static final String FECHA_INICIO = "fechaInicio";
    private static final String FECHA_FIN = "fechaFin";
    private static final String TIPO = "tipo";
    private static final String REVISION = "Revisión";
    private static final String MECANICO = "Mecánico";
    private static final String HORAS = "horas";
    private static final String PRECIO_MATERIAL = "precioMaterial";

    private Connection conexion;
    private static Trabajos instancia;

    static Trabajos getInstancia() {
        if (instancia == null) {
            instancia = new Trabajos();
        }
        return instancia;
    }

    private Trabajos() {}


    @Override
    public void comenzar() {
        conexion = MariaDB.getConexion();
    }

    @Override
    public void terminar() {
        MariaDB.cerrarConexion();
    }

    private Trabajo getTrabajo(ResultSet fila) throws SQLException, OperationNotSupportedException {
        Cliente cliente = Clientes.getInstancia().buscar(Cliente.get(fila.getString(CLIENTE)));
        Vehiculo vehiculo = Vehiculos.getInstancia().buscar(Vehiculo.get(fila.getString(VEHICULO))).getVehiculo();
        LocalDate fechaInicio = fila.getDate(FECHA_INICIO).toLocalDate();
        Trabajo trabajo = null;
        String tipo = fila.getString(TIPO);
        if (tipo.equals(REVISION)) {
            trabajo = new Revision(cliente, vehiculo, fechaInicio);
        } else if (tipo.equals(MECANICO)){
            trabajo = new Mecanico(cliente, vehiculo, fechaInicio);
            float precioMaterial = fila.getFloat(PRECIO_MATERIAL);
            if (precioMaterial != 0) {
                ((Mecanico) trabajo).anadirPrecioMaterial(precioMaterial);
            }
        }
        int horas = fila.getInt(HORAS);
        if (horas != 0) {
            trabajo.anadirHoras(horas);
        }
        LocalDate fechaFin = (fila.getDate(FECHA_FIN) == null) ? null : fila.getDate(FECHA_FIN).toLocalDate();
        if (fechaFin != null) {
            trabajo.cerrar(fechaFin);
        }
        return trabajo;
    }

    private void prepararSentencia(PreparedStatement sentencia, Trabajo trabajo) throws SQLException {
        sentencia.setString(1, trabajo.getCliente().getDni());
        sentencia.setString(2, trabajo.getVehiculo().matricula());
        sentencia.setDate(3, Date.valueOf(trabajo.getFechaInicio()));
        sentencia.setNull(4, Types.DATE);
        sentencia.setString(5, TipoTrabajo.get(trabajo).toString());
        sentencia.setNull(6, Types.INTEGER);
        sentencia.setNull(7, Types.FLOAT);
    }

    @Override
    public List<Trabajo> get() {
        List<Trabajo> trabajos = new ArrayList<>();
        try (Statement sentencia = conexion.createStatement()) {
            ResultSet filas = sentencia.executeQuery("select * from trabajos");
            while (filas.next()) {
                trabajos.add(getTrabajo(filas));
            }
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return trabajos;
    }

    @Override
    public List<Trabajo> get(Cliente cliente) {
        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where cliente = ?")) {
            sentencia.setString(1, cliente.getDni());
            ResultSet filas = sentencia.executeQuery();
            while (filas.next()) {
                trabajos.add(getTrabajo(filas));
            }
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return trabajos;
    }

    @Override
    public List<Trabajo> get(Vehiculo vehiculo) {
        List<Trabajo> trabajos = new ArrayList<>();
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where vehiculo = ?")) {
            sentencia.setString(1, vehiculo.matricula());
            ResultSet filas = sentencia.executeQuery();
            while (filas.next()) {
                trabajos.add(getTrabajo(filas));
            }
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return trabajos;
    }

    @Override
    public Map<TipoTrabajo, Integer> getEstadisticasMensuales(LocalDate mes) {
        Objects.requireNonNull(mes, "El mes no puede ser nulo.");
        Map<TipoTrabajo, Integer> estadisticas = inicializarEstadisticas();
        LocalDate inicioMes = LocalDate.of(mes.getYear(), mes.getMonth(), 1);
        LocalDate finMes = LocalDate.of(mes.getYear(), mes.getMonth(), mes.lengthOfMonth());
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where fechaInicio >= ? and fechaInicio <= ?")) {
            sentencia.setDate(1, Date.valueOf(inicioMes));
            sentencia.setDate(2, Date.valueOf(finMes));
            ResultSet filas = sentencia.executeQuery();
            while (filas.next()) {
                TipoTrabajo tipoTrabajo = (TipoTrabajo) TipoTrabajo.get(getTrabajo(filas));
                estadisticas.put(tipoTrabajo, estadisticas.get(tipoTrabajo) + 1);
            }
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return estadisticas;
    }

    private Map<TipoTrabajo, Integer> inicializarEstadisticas() {
        Map<TipoTrabajo, Integer> estadisticas = new EnumMap<>(TipoTrabajo.class);
        for (TipoTrabajo tipoTrabajo : TipoTrabajo.values()) {
            estadisticas.put(tipoTrabajo, 0);
        }
        return estadisticas;
    }

    @Override
    public void insertar(Trabajo trabajo) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No se puede insertar un trabajo nulo.");
        comprobarTrabajo(trabajo.getCliente(), trabajo.getVehiculo(), trabajo.getFechaInicio());
        try (PreparedStatement sentencia = conexion.prepareStatement("insert into trabajos values (?, ?, ?, ?, ?, ?, ?)")) {
            prepararSentencia(sentencia, trabajo);
            sentencia.execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private void comprobarTrabajo(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio) throws OperationNotSupportedException {
        try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from trabajos where cliente = ? and fechaFin is null")) {
            sentencia.setString(1, cliente.getDni());
            ResultSet filas = sentencia.executeQuery();
            filas.first();
            if (filas.getInt(1) == 1) {
                throw new OperationNotSupportedException("El cliente tiene otro trabajo en curso.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from trabajos where vehiculo = ? and fechaFin is null")) {
            sentencia.setString(1, vehiculo.matricula());
            ResultSet filas = sentencia.executeQuery();
            filas.first();
            if (filas.getInt(1) == 1) {
                throw new OperationNotSupportedException("El vehículo está actualmente en el taller.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from trabajos where cliente = ? and fechaFin >= ?")) {
            sentencia.setString(1, cliente.getDni());
            sentencia.setDate(2, Date.valueOf(fechaInicio));
            ResultSet filas = sentencia.executeQuery();
            filas.first();
            if (filas.getInt(1) == 1) {
                throw new OperationNotSupportedException("El cliente tiene otro trabajo posterior.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        try (PreparedStatement sentencia = conexion.prepareStatement("select count(*) from trabajos where vehiculo = ? and fechaFin >= ?")) {
            sentencia.setString(1, vehiculo.matricula());
            sentencia.setDate(2, Date.valueOf(fechaInicio));
            ResultSet filas = sentencia.executeQuery();
            filas.first();
            if (filas.getInt(1) == 1) {
                throw new OperationNotSupportedException("El vehículo tiene otro trabajo posterior.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void anadirHoras(Trabajo trabajo, int horas) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo añadir horas a un trabajo nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where cliente = ? and vehiculo = ? and fechaFin is null", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            sentencia.setString(1, trabajo.getCliente().getDni());
            sentencia.setString(2, trabajo.getVehiculo().matricula());
            ResultSet filas = sentencia.executeQuery();
            if (filas.first()) {
                filas.updateInt(HORAS, horas);
                filas.updateRow();
                trabajo.anadirHoras(horas);
            } else {
                throw new OperationNotSupportedException("No existe ningún trabajo abierto igual.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void anadirPrecioMaterial(Trabajo trabajo, float precioMaterial) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo añadir precio del material a un trabajo nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where cliente = ? and vehiculo = ? and tipo = ? and fechaFin is null", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            sentencia.setString(1, trabajo.getCliente().getDni());
            sentencia.setString(2, trabajo.getVehiculo().matricula());
            sentencia.setString(3, TipoTrabajo.MECANICO.toString());
            ResultSet filas = sentencia.executeQuery();
            if (filas.first()) {
                filas.updateFloat(PRECIO_MATERIAL, precioMaterial);
                filas.updateRow();
                ((Mecanico)trabajo).anadirPrecioMaterial(precioMaterial);
            } else {
                throw new OperationNotSupportedException("No existe ningún trabajo mecánico abierto igual.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void cerrar(Trabajo trabajo, LocalDate fechaFin) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo cerrar a un trabajo nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where cliente = ? and vehiculo = ? and fechaFin is null", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)) {
            sentencia.setString(1, trabajo.getCliente().getDni());
            sentencia.setString(2, trabajo.getVehiculo().matricula());
            ResultSet filas = sentencia.executeQuery();
            if (filas.first()) {
                filas.updateDate(FECHA_FIN, Date.valueOf(fechaFin));
                filas.updateRow();
                trabajo.cerrar(fechaFin);
            } else {
                throw new OperationNotSupportedException("No existe ningún trabajo abierto igual.");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public Trabajo buscar(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "No se puede buscar un trabajo nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("select * from trabajos where cliente = ? and vehiculo = ? and fechaInicio = ?")) {
            sentencia.setString(1, trabajo.getCliente().getDni());
            sentencia.setString(2, trabajo.getVehiculo().matricula());
            sentencia.setDate(3,  Date.valueOf(trabajo.getFechaInicio()));
            ResultSet filas = sentencia.executeQuery();
            trabajo = filas.first() ? getTrabajo(filas) : null;
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return trabajo;
    }

    @Override
    public void borrar(Trabajo trabajo) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No se puede borrar un trabajo nulo.");
        try (PreparedStatement sentencia = conexion.prepareStatement("delete from trabajos where cliente = ? and vehiculo = ? and fechaInicio = ?")) {
            sentencia.setString(1, trabajo.getCliente().getDni());
            sentencia.setString(2, trabajo.getVehiculo().matricula());
            sentencia.setDate(3,  Date.valueOf(trabajo.getFechaInicio()));
            int filas = sentencia.executeUpdate();
            if (filas == 0) {
                throw new OperationNotSupportedException("No existe ningún trabajo igual.");
            }
        } catch (SQLException | OperationNotSupportedException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
