package org.iesalandalus.programacion.tallermecanico.controlador;

import org.iesalandalus.programacion.tallermecanico.modelo.Modelo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Trabajo;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.vista.Vista;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Controlador implements IControlador {
    private final Modelo modelo;

    private final Vista vista;

    public Controlador(Modelo modelo, Vista vista) {
        Objects.requireNonNull(modelo, "El modelo no puede ser nulo");
        Objects.requireNonNull(vista, "La vista no puede ser nula");
        this.modelo = modelo;
        this.vista = vista;
        this.vista.getGestorEventos().suscribir(this, Evento.values());
    }

    @Override
    public void comenzar() {
        this.modelo.comenzar();
        this.vista.comenzar();
    }

    @Override
    public void terminar() {
        this.modelo.terminar();
        this.vista.terminar();
    }

    @Override
    public void actualizar(Evento evento) {
        String texto = "";
        boolean exito = false;
        try {
            switch (evento) {
                case INSERTAR_CLIENTE -> {
                    modelo.insertar(vista.leerCliente());
                    texto = "¡Cliente insertado exitosamente en el sistema!";
                }
                case BUSCAR_CLIENTE -> {
                    vista.mostrarCliente(modelo.buscar(vista.leerClienteDni()));
                    texto = "¡Búsqueda de cliente en el sistema realizada!";
                }
                case BORRAR_CLIENTE -> {
                    modelo.borrar(vista.leerClienteDni());
                    texto = "¡Cliente eliminado exitosamente del sistema!";
                }
                case MODIFICAR_CLIENTE -> {
                    modelo.modificar(modelo.buscar(vista.leerClienteDni()), vista.leerNuevoNombre(), vista.leerNuevoTelefono());
                    texto = "¡Cliente modificado exitosamente en el sistema!";
                }
                case LISTAR_CLIENTES -> {
                    vista.mostrarClientes(modelo.getClientes());
                    texto = "¡Lista de clientes del sistema obtenida!";
                }
                case INSERTAR_VEHICULO -> {
                    modelo.insertar(vista.leerVehiculo());
                    texto = "¡Vehículo insertado exitosamente en el sistema!";
                }
                case BUSCAR_VEHICULO -> {
                    vista.mostrarVehiculo(modelo.buscar(vista.leerVehiculoMatricula()));
                    texto = "¡Búsqueda de vehículo en el sistema realizada!";
                }
                case BORRAR_VEHICULO -> {
                    modelo.borrar(vista.leerVehiculoMatricula());
                    texto = "¡Vehículo eliminado exitosamente del sistema!";
                }
                case LISTAR_VEHICULOS -> {
                    vista.mostrarVehiculos(modelo.getVehiculos());
                    texto = "¡Lista de vehículos del sistema obtenida!";
                }
                case INSERTAR_REVISION -> {
                    modelo.insertar(vista.leerRevision());
                    texto = "¡Revisión insertado exitosamente en el sistema!";
                }
                case INSERTAR_MECANICO -> {
                    modelo.insertar(vista.leerMecanico());
                    texto = "¡Trabajo mecánico insertado exitosamente en el sistema!";
                }
                case BUSCAR_TRABAJO -> {
                    vista.mostrarTrabajo(modelo.buscar(vista.leerRevision()));
                    texto = "¡Búsqueda de trabajo en el sistema realizada!";
                }
                case BORRAR_TRABAJO -> {
                    modelo.borrar(vista.leerRevision());
                    texto = "¡Trabajo eliminado exitosamente del sistema!";
                }
                case LISTAR_TRABAJOS -> {
                    vista.mostrarTrabajos(modelo.getTrabajos());
                    texto = "¡Lista de trabajos del sistema obtenida!";
                }
                case LISTAR_TRABAJOS_CLIENTE -> {
                    vista.mostrarTrabajos(modelo.getTrabajos(vista.leerClienteDni()));
                    texto = "¡Lista de trabajos para el cliente del sistema obtenida!";
                }
                case LISTAR_TRABAJOS_VEHICULO -> {
                    vista.mostrarTrabajos(modelo.getTrabajos(vista.leerVehiculoMatricula()));
                    texto = "¡Lista de trabajos para el vehículo del sistema obtenida!";
                }
                case ANADIR_HORAS_TRABAJO -> {
                    modelo.anadirHoras(vista.leerRevision(), vista.leerHoras());
                    texto = "¡La cantidad de horas ha sido añadida exitosamente al trabajo en el sistema!";
                }
                case ANADIR_PRECIO_MATERIAL_TRABAJO -> {
                    modelo.anadirPrecioMaterial(vista.leerRevision(), vista.leerPrecioMaterial());
                    texto = "¡El precio del material ha sido añadido exitosamente al trabajo en el sistema!";
                }
                case CERRAR_TRABAJO -> {
                    modelo.cerrar(vista.leerRevision(), vista.leerFechaCierre());
                    texto = "¡Trabajo cerrado exitosamente en el sistema!";
                }
                case SALIR -> {
                    terminar();
                    texto = "¡Saliendo del sistema!";
                }
            }
            exito = true;
        } catch (OperationNotSupportedException | IllegalArgumentException | NullPointerException e) {
            texto = e.getMessage();
        }
        vista.notificarResultado(evento, texto, exito);
    }
}
