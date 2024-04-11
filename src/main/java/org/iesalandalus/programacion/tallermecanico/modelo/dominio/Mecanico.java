package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.Objects;

public class Mecanico extends Trabajo {
    private static final float FACTOR_HORA = 30;
    private static final float FACTOR_PRECIO_MATERIAL = 1.5F;
    private float precioMaterial;

    public Mecanico(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio) {
        super(cliente, vehiculo, fechaInicio);
        precioMaterial = 0;
    }

    public Mecanico(Mecanico mecanico) {
        super(mecanico);
        precioMaterial = mecanico.getPrecioMaterial();
    }

    public float getPrecioMaterial() {
        return precioMaterial;
    }

    public void anadirPrecioMaterial(float precioMaterial) throws OperationNotSupportedException {
        if (estaCerrado()) {
            throw new OperationNotSupportedException("No se puede añadir precio del material, ya que el trabajo mecánico está cerrado.");
        }
        if (precioMaterial <= 0) {
            throw new IllegalArgumentException("El precio del material a añadir debe ser mayor que cero.");
        }
        this.precioMaterial += precioMaterial;
    }

    @Override
    public float getPrecioEspecifico() {
        float precioHora = (FACTOR_HORA * getHoras());
        float precioMaterial = (FACTOR_PRECIO_MATERIAL * getPrecioMaterial());
        return (precioHora + precioMaterial);
    }

    @Override
    public String toString() {
        return (estaCerrado()) ? String.format("Mecánico -> %s - %s (%s - %s): %s horas, %.2f € en material, %.2f € total", getCliente(), getVehiculo(), getFechaInicio().format(FORMATO_FECHA), getFechaFin().format(FORMATO_FECHA), getHoras(), this.precioMaterial, getPrecio()) : String.format("Mecánico -> %s - %s (%s - ): %s horas, %.2f € en material", getCliente(), getVehiculo(), getFechaInicio().format(FORMATO_FECHA), getHoras(), this.precioMaterial);
    }
}