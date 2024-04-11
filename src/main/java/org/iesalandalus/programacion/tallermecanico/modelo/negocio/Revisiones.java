package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Revision;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.*;

public class Revisiones {
    private final List<Revision> coleccionRevisiones;

    public Revisiones() {
        coleccionRevisiones = new ArrayList<>();
    }

    public List<Revision> get() {
        return new ArrayList<>(coleccionRevisiones);
    }

    public List<Revision> get(Cliente cliente) {
        List<Revision> coleccionRevisionesCliente = new ArrayList<>();
        for (Revision revision : coleccionRevisiones) {
            if (revision.getCliente().equals(cliente)) {
                coleccionRevisionesCliente.add(revision);
            }
        }
        return coleccionRevisionesCliente;
    }

    public List<Revision> get(Vehiculo vehiculo) {
        List<Revision> coleccionRevisionesVehiculo = new ArrayList<>();
        for (Revision revision : coleccionRevisiones) {
            if (revision.getVehiculo().equals(vehiculo)) {
                coleccionRevisionesVehiculo.add(revision);
            }
        }
        return coleccionRevisionesVehiculo;
    }

    public void insertar(Revision revision) throws OperationNotSupportedException {
        Objects.requireNonNull(revision, "No se puede insertar una revisión nula.");
        comprobarRevision(revision.getCliente(), revision.getVehiculo(), revision.getFechaInicio());
        coleccionRevisiones.add(revision);
    }

    private void comprobarRevision(Cliente cliente, Vehiculo vehiculo, LocalDate fechaRevision) throws OperationNotSupportedException {
        for (Revision revision : coleccionRevisiones) {
            if (revision.getCliente().equals(cliente)) {
                if (!revision.estaCerrada()) {
                    throw new OperationNotSupportedException("El cliente tiene otra revisión en curso.");
                } else if (!fechaRevision.isAfter(revision.getFechaFin())) {
                    throw new OperationNotSupportedException("El cliente tiene una revisión posterior.");
                }
            }
            if (revision.getVehiculo().equals(vehiculo)) {
                if (!revision.estaCerrada()) {
                    throw new OperationNotSupportedException("El vehículo está actualmente en revisión.");
                } else if (!fechaRevision.isAfter(revision.getFechaFin())) {
                    throw new OperationNotSupportedException("El vehículo tiene una revisión posterior.");
                }
            }
        }
    }

    private Revision getRevision(Revision revision) throws OperationNotSupportedException {
        Objects.requireNonNull(revision, "No puedo operar sobre una revisión nula.");
        if (!coleccionRevisiones.contains(revision)) {
            throw new OperationNotSupportedException("No existe ninguna revisión igual.");
        }
        return coleccionRevisiones.get(coleccionRevisiones.indexOf(revision));
    }

    public void anadirHoras(Revision revision, int horas) throws OperationNotSupportedException {
        if (horas < 1) {
            throw new IllegalArgumentException("No puedes ingresar horas negativas.");
        }
        getRevision(revision).anadirHoras(horas);
    }

    public void anadirPrecioMaterial(Revision revision, float precioMaterial) throws OperationNotSupportedException {
        if (precioMaterial <= 0) {
            throw new IllegalArgumentException("El precio del material no puede ser inferior a 0.");
        }
        getRevision(revision).anadirPrecioMaterial(precioMaterial);
    }

    public void cerrar(Revision revision, LocalDate fechaFin) throws OperationNotSupportedException {
        Objects.requireNonNull(fechaFin, "No puedo operar sobre una fecha de fin nula.");
        getRevision(revision).cerrar(fechaFin);
    }

    public Revision buscar(Revision revision) {
        Objects.requireNonNull(revision, "No se puede buscar una revisión nula.");
        return (coleccionRevisiones.contains(revision)) ? coleccionRevisiones.get(coleccionRevisiones.indexOf(revision)) : null;
    }

    public void borrar(Revision revision) throws OperationNotSupportedException {
        Objects.requireNonNull(revision, "No se puede borrar una revisión nula.");
        if (!coleccionRevisiones.contains(revision)) {
            throw new OperationNotSupportedException("No existe ninguna revisión igual.");
        }
        coleccionRevisiones.remove(coleccionRevisiones.get(coleccionRevisiones.indexOf(revision)));
    }
}