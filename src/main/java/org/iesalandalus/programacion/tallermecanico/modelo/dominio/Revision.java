package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import java.time.LocalDate;

public class Revision extends Trabajo {

    private static final float FACTOR_HORA = 35F;

    public Revision(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio) {
        super(cliente, vehiculo, fechaInicio);
    }

    public Revision(Revision revision) {
        super(revision);
    }

    public Revision(Cliente cliente, Object vehiculo, LocalDate now) {
    }

    @Override
    public float getPrecioEspecifico() {
        return (estaCerrado()) ? FACTOR_HORA * getHoras() : 0;
    }

    @Override
    public String toString() {
        String cadena;
        if (!estaCerrado()) {
            cadena = String.format("Revisión -> %s - %s (%s - ): %d horas", cliente, vehiculo, fechaInicio.format(FORMATO_FECHA), horas);
        } else {
            cadena = String.format("Revisión -> %s - %s (%s - %s): %d horas, %.2f € total", cliente, vehiculo, fechaInicio.format(FORMATO_FECHA), fechaFin.format(FORMATO_FECHA), horas, getPrecio());
        }
        return cadena;
    }

    public boolean estaCerrada() {
        return false;
    }

    public void anadirPrecioMaterial(float precioMaterial) {
    }
}