package org.iesalandalus.programacion.tallermecanico.modelo.cascada;

import org.iesalandalus.programacion.tallermecanico.modelo.Modelo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.*;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Clientes;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.FuenteDatosMemoria;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Trabajos;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Vehiculos;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ModeloCascada implements Modelo {
    private IClientes clientes;
    private IVehiculos vehiculos;
    private ITrabajos trabajos;

    public ModeloCascada(FabricaFuenteDatos fabricaFuenteDatos) {
        IFuenteDatos fuenteDatos = fabricaFuenteDatos.crear();
        clientes = fuenteDatos.crearClientes();
        vehiculos = fuenteDatos.crearVehiculos();
        trabajos = fuenteDatos.crearTrabajos();
    }

    @Override
    public void comenzar() {
        System.out.println("Modelo ha comenzado");
    }

    @Override
    public void terminar() {
        System.out.println("El modelo ha terminado con éxito. ¡Gracias por utilizar nuestro sistema!");
    }

    @Override
    public void insertar(Cliente cliente) throws OperationNotSupportedException {
        clientes.insertar(new Cliente(cliente));
    }

    @Override
    public void insertar(Vehiculo vehiculo) throws OperationNotSupportedException {
        this.vehiculos.insertar(vehiculo);
    }

    @Override
    public void insertar(Trabajo trabajo) throws OperationNotSupportedException {
        if (trabajo instanceof Revision revision) {
            this.trabajos.insertar(new Revision(clientes.buscar(revision.getCliente()), vehiculos.buscar(revision.getVehiculo()), revision.getFechaInicio()));
        } else if (trabajo instanceof Mecanico mecanico) {
            this.trabajos.insertar(new Mecanico(clientes.buscar(mecanico.getCliente()), vehiculos.buscar(mecanico.getVehiculo()), mecanico.getFechaInicio()));
        }
    }

    @Override
    public Cliente buscar(Cliente cliente) {
        Cliente clientebuscado = clientes.buscar(cliente);
        return (clientebuscado == null) ? null : new Cliente(clientebuscado);
    }

    @Override
    public Vehiculo buscar(Vehiculo vehiculo) {
        return vehiculos.buscar(vehiculo);
    }

    @Override
    public Trabajo buscar(Trabajo trabajo) {
        Trabajo trabajoBuscado = trabajos.buscar(trabajo);
        if (trabajoBuscado != null) {
            if (trabajo instanceof Revision revision) {
                trabajoBuscado = new Revision(revision);
            } else if (trabajo instanceof Mecanico mecanico) {
                trabajoBuscado = new Mecanico(mecanico);
            }
        }
        return trabajoBuscado;
    }

    @Override
    public boolean modificar(Cliente cliente, String nombre, String telefono) throws OperationNotSupportedException {
        return this.clientes.modificar(cliente, nombre, telefono);
    }

    @Override
    public void anadirHoras(Trabajo trabajo, int horas) throws OperationNotSupportedException {
        this.trabajos.anadirHoras(trabajo, horas);
    }

    @Override
    public void anadirPrecioMaterial(Trabajo trabajo, float precioMaterial) throws OperationNotSupportedException {
        this.trabajos.anadirPrecioMaterial(trabajo, precioMaterial);
    }

    @Override
    public void cerrar(Trabajo trabajo, LocalDate fechaFin) throws OperationNotSupportedException {
        this.trabajos.cerrar(trabajo, fechaFin);
    }

    @Override
    public void borrar(Cliente cliente) throws OperationNotSupportedException {
        for (Trabajo trabajoCliente : trabajos.get(cliente)) {
            this.trabajos.borrar(trabajoCliente);
        }
        this.clientes.borrar(cliente);
    }

    @Override
    public void borrar(Vehiculo vehiculo) throws OperationNotSupportedException {
        for (Trabajo trabajoVehiculo : trabajos.get(vehiculo)) {
            this.trabajos.borrar(trabajoVehiculo);
        }
        this.vehiculos.borrar(vehiculo);
    }

    @Override
    public void borrar(Trabajo trabajo) throws OperationNotSupportedException {
        this.trabajos.borrar(trabajo);
    }

    @Override
    public List<Cliente> getClientes() {
        List<Cliente> coleccionClientesInstanciada = new ArrayList<>();
        for (Cliente cliente : clientes.get()) {
            coleccionClientesInstanciada.add(new Cliente(cliente));
        }
        return coleccionClientesInstanciada;
    }

    @Override
    public List<Vehiculo> getVehiculos() {
        return vehiculos.get();
    }

    @Override
    public List<Trabajo> getTrabajos() {
        List<Trabajo> coleccionTrabajosInstanciada = new ArrayList<>();
        for (Trabajo trabajo : trabajos.get()) {
            if (trabajo instanceof Revision revision) {
                coleccionTrabajosInstanciada.add(new Revision(revision));
            } else if (trabajo instanceof Mecanico mecanico) {
                coleccionTrabajosInstanciada.add(new Mecanico(mecanico));
            }
        }

        return coleccionTrabajosInstanciada;
    }

    @Override
    public List<Trabajo> getTrabajos(Cliente cliente) {
        List<Trabajo> coleccionTrabajosClienteInstanciada = new ArrayList<>();
        for (Trabajo trabajoCliente : trabajos.get(cliente)) {
            if (trabajoCliente instanceof Revision revision) {
                coleccionTrabajosClienteInstanciada.add(new Revision(revision));
            } else if (trabajoCliente instanceof Mecanico mecanico) {
                coleccionTrabajosClienteInstanciada.add(new Mecanico(mecanico));
            }
        }
        return coleccionTrabajosClienteInstanciada;
    }

    @Override
    public List<Trabajo> getTrabajos(Vehiculo vehiculo) {
        List<Trabajo> coleccionTrabajosVehiculoInstanciada = new ArrayList<>();
        for (Trabajo trabajoVehiculo : trabajos.get(vehiculo)) {
            if (trabajoVehiculo instanceof Revision revision) {
                coleccionTrabajosVehiculoInstanciada.add(new Revision(revision));
            } else if (trabajoVehiculo instanceof Mecanico mecanico) {
                coleccionTrabajosVehiculoInstanciada.add(new Mecanico(mecanico));
            }
        }
        return coleccionTrabajosVehiculoInstanciada;
    }
}