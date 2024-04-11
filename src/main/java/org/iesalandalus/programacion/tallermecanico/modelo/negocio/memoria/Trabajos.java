package org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Mecanico;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ITrabajos;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.*;

public class Trabajos implements ITrabajos {
    private final List<Trabajo> coleccionTrabajos;

    public Trabajos() {
        coleccionTrabajos = new ArrayList<>();
    }

    @Override
    public List<Trabajo> get() {
        return new ArrayList<>(coleccionTrabajos);
    }

    @Override
    public List<Trabajo> get(Cliente cliente) {
        List<Trabajo> coleccionTrabajoesCliente = new ArrayList<>();
        for (Trabajo trabajo : coleccionTrabajos) {
            if (trabajo.getCliente().equals(cliente)) {
                coleccionTrabajoesCliente.add(trabajo);
            }
        }
        return coleccionTrabajoesCliente;
    }

    @Override
    public List<Trabajo> get(Vehiculo vehiculo) {
        List<Trabajo> coleccionTrabajoesVehiculo = new ArrayList<>();
        for (Trabajo trabajo : coleccionTrabajos) {
            if (trabajo.getVehiculo().equals(vehiculo)) {
                coleccionTrabajoesVehiculo.add(trabajo);
            }
        }
        return coleccionTrabajoesVehiculo;
    }

    @Override
    public void insertar(Trabajo trabajo) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No se puede insertar un trabajo nulo.");
        comprobarTrabajo(trabajo.getCliente(), trabajo.getVehiculo(), trabajo.getFechaInicio());
        coleccionTrabajos.add(trabajo);
    }

    private void comprobarTrabajo(Cliente cliente, Vehiculo vehiculo, LocalDate fechaTrabajo) throws OperationNotSupportedException {
        for (Trabajo trabajo : coleccionTrabajos) {
            if (trabajo.getCliente().equals(cliente)) {
                if (!trabajo.estaCerrado()) {
                    throw new OperationNotSupportedException("El cliente tiene otro trabajo en curso.");
                } else if (!fechaTrabajo.isAfter(trabajo.getFechaFin())) {
                    throw new OperationNotSupportedException("El cliente tiene otro trabajo posterior.");
                }
            }
            if (trabajo.getVehiculo().equals(vehiculo)) {
                if (!trabajo.estaCerrado()) {
                    throw new OperationNotSupportedException("El vehículo está actualmente en el taller.");
                } else if (!fechaTrabajo.isAfter(trabajo.getFechaFin())) {
                    throw new OperationNotSupportedException("El vehículo tiene otro trabajo posterior.");
                }
            }
        }
    }

    private Trabajo getTrabajoAbierto(Vehiculo vehiculo) throws OperationNotSupportedException {
        Trabajo trabajoAbierto = null;
        for (Trabajo trabajo : coleccionTrabajos) {
            if (trabajo.getVehiculo().equals(vehiculo)) {
                if (!trabajo.estaCerrado()) {
                    trabajoAbierto = trabajo;
                }
            }
        }
        if (!coleccionTrabajos.contains(trabajoAbierto)) {
            throw new OperationNotSupportedException("No existe ningún trabajo abierto para dicho vehículo.");
        }
        return coleccionTrabajos.get(coleccionTrabajos.indexOf(trabajoAbierto));
    }

    @Override
    public void anadirHoras(Trabajo trabajo, int horas) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo añadir horas a un trabajo nulo.");
        if (horas < 1) {
            throw new IllegalArgumentException("No puedes ingresar horas negativas.");
        }
        getTrabajoAbierto(trabajo.getVehiculo()).anadirHoras(horas);
    }

    @Override
    public void anadirPrecioMaterial(Trabajo trabajo, float precioMaterial) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo añadir precio del material a un trabajo nulo.");
        if (precioMaterial <= 0) {
            throw new IllegalArgumentException("El precio del material no puede ser inferior a 0.");
        }
        if (getTrabajoAbierto(trabajo.getVehiculo()) instanceof Mecanico mecanico) {
            mecanico.anadirPrecioMaterial(precioMaterial);
        } else {
            throw new OperationNotSupportedException("No se puede añadir precio al material para este tipo de trabajos.");
        }
    }

    @Override
    public void cerrar(Trabajo trabajo, LocalDate fechaFin) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No puedo cerrar un trabajo nulo.");
        Objects.requireNonNull(fechaFin, "No puedo operar sobre una fecha de fin nula.");
        getTrabajoAbierto(trabajo.getVehiculo()).cerrar(fechaFin);
    }

    @Override
    public Trabajo buscar(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "No se puede buscar un trabajo nulo.");
        return (coleccionTrabajos.contains(trabajo)) ? coleccionTrabajos.get(coleccionTrabajos.indexOf(trabajo)) : null;
    }

    @Override
    public void borrar(Trabajo trabajo) throws OperationNotSupportedException {
        Objects.requireNonNull(trabajo, "No se puede borrar un trabajo nulo.");
        if (!coleccionTrabajos.contains(trabajo)) {
            throw new OperationNotSupportedException("No existe ningún trabajo igual.");
        }
        coleccionTrabajos.remove(coleccionTrabajos.get(coleccionTrabajos.indexOf(trabajo)));
    }
}