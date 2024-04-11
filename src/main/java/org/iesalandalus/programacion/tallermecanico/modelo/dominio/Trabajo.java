package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public abstract class Trabajo {
    static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final float FACTOR_DIA = 10;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int horas;
    private Cliente cliente;
    private Vehiculo vehiculo;

    protected Trabajo(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio) {
        setCliente(cliente);
        setVehiculo(vehiculo);
        setFechaInicio(fechaInicio);
        horas = 0;
        fechaFin = null;
    }

    protected Trabajo(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "El trabajo no puede ser nulo.");
        cliente = new Cliente(trabajo.getCliente());
        vehiculo = trabajo.getVehiculo();
        fechaInicio = trabajo.getFechaInicio();
        fechaFin = trabajo.getFechaFin();
        horas = trabajo.getHoras();
    }

    public static Trabajo copiar(Trabajo trabajo) {
        Objects.requireNonNull(trabajo, "El trabajo no puede ser nulo.");
        if (trabajo instanceof Revision revision) {
            trabajo = new Revision(revision);
        } else if (trabajo instanceof Mecanico mecanico) {
            trabajo = new Mecanico(mecanico);
        }
        return trabajo;
    }

    public static Trabajo get(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        return new Revision(Cliente.get("99999999R"), vehiculo, LocalDate.now());
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    private void setFechaInicio(LocalDate fechaInicio) {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser nula.");
        if (fechaInicio.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser futura.");
        }
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    private void setFechaFin(LocalDate fechaFin) {
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser nula.");
        if (fechaFin.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser futura.");
        } else if (fechaFin.isBefore(getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        this.fechaFin = fechaFin;
    }

    public Cliente getCliente() {
        return cliente;
    }

    private void setCliente(Cliente cliente) {
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo.");
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    private void setVehiculo(Vehiculo vehiculo) {
        Objects.requireNonNull(vehiculo, "El vehículo no puede ser nulo.");
        this.vehiculo = vehiculo;
    }

    public int getHoras() {
        return horas;
    }

    public void anadirHoras(int horas) throws OperationNotSupportedException {
        if (estaCerrado()) {
            throw new OperationNotSupportedException("No se puede añadir horas, ya que el trabajo está cerrado.");
        }
        if (horas < 1) {
            throw new IllegalArgumentException("Las horas a añadir deben ser mayores que cero.");
        }
        this.horas += horas;
    }

    public void cerrar(LocalDate fechaFin) throws OperationNotSupportedException {
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser nula.");
        if (estaCerrado()) {
            throw new OperationNotSupportedException("El trabajo ya está cerrado.");
        }
        setFechaFin(fechaFin);
    }

    public boolean estaCerrado() {
        return (getFechaFin() != null);
    }

    public float getPrecio() {
        float precioDia = (FACTOR_DIA * getDias());
        return (precioDia + getPrecioEspecifico());
    }

    public abstract float getPrecioEspecifico();

    private float getDias() {
        return (estaCerrado()) ? (ChronoUnit.DAYS.between(getFechaInicio(), getFechaFin())) : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trabajo trabajo = (Trabajo) o;
        return Objects.equals(fechaInicio, trabajo.fechaInicio) && Objects.equals(cliente, trabajo.cliente) && Objects.equals(vehiculo, trabajo.vehiculo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaInicio, cliente, vehiculo);
    }
}
