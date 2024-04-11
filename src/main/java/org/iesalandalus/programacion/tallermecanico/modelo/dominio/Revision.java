package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import java.time.LocalDate;
import java.util.Objects;

public class Revision extends Trabajo {
    private static final float FACTOR_HORA = 35;

    public Revision(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio) {
        super(cliente, vehiculo, fechaInicio);
    }

    public Revision(Revision revision) {
        super(revision);
    }

    @Override
    public float getPrecioEspecifico() {
        return (FACTOR_HORA * getHoras());
    }

    @Override
    public String toString() {
        return (estaCerrado()) ? String.format("Revisión -> %s - %s (%s - %s): %s horas, %.2f € total", getCliente(), getVehiculo(), getFechaInicio().format(FORMATO_FECHA),getFechaFin().format(FORMATO_FECHA), getHoras(), getPrecio()) : String.format("Revisión -> %s - %s (%s - ): %s horas", getCliente(), getVehiculo(), getFechaInicio().format(FORMATO_FECHA), getHoras());
    }
}