/*package org.iesalandalus.programacion.tallermecanico.vista;

import org.iesalandalus.programacion.tallermecanico.controlador.Controlador;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.Evento;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.GestorEventos;
import org.iesalandalus.programacion.tallermecanico.vista.eventos.ReceptorEventos;
import org.iesalandalus.programacion.utilidades.Entrada;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.IllformedLocaleException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Vista {

    private Controlador controlador;
    private GestorEventos gestorEventos = new GestorEventos(Evento.values());

    public void setControlador(Controlador controlador) {
        Objects.requireNonNull(controlador, "El controlador no puede ser nulo.");
        this.controlador = controlador;
    }

    public GestorEventos getGestorEventos(){
        return gestorEventos;
    }

    public void comenzar() {
        Opcion opcion;
        do {
            Consola.mostrarMenu();
            opcion = Consola.elegirOpcion();
            ejecutar(opcion);
        } while (opcion != Opcion.SALIR);
    }

    public void terminar() {
        Consola.mostrarCabecera("¡Gracias por utilizar nuestra aplicación! Esperamos haber hecho tu experiencia más fácil y agradable. ¡Hasta pronto!");
    }

    private void ejecutar(Opcion opcion) {
        switch (opcion) {
            case INSERTAR_CLIENTE -> insertarCliente();
            case BUSCAR_CLIENTE -> buscarCliente();
            case BORRAR_CLIENTE -> borrarCliente();
            case MODIFICAR_CLIENTE -> modificarCliente();
            case LISTAR_CLIENTES -> listarClientes();
            case INSERTAR_VEHICULO -> insertarVehiculo();
            case BUSCAR_VEHICULO -> buscarVehiculo();
            case BORRAR_VEHICULO -> borrarVehiculo();
            case LISTAR_VEHICULOS -> listarVehiculos();
            case INSERTAR_TRABAJO -> insertarTrabajo();
            case BUSCAR_TRABAJO -> buscarTrabajo();
            case BORRAR_TRABAJO -> borrarTrabajo();
            case LISTAR_TRABAJOS -> listarTrabajos();
            case LISTAR_TRABAJOS_CLIENTE -> listarTrabajosCliente();
            case LISTAR_TRABAJOS_VEHICULO -> listarTrabajosVehiculo();
            case ANADIR_HORAS_TRABAJO -> anadirHoras();
            case ANADIR_PRECIO_MATERIAL_TRABAJO -> anadirPrecioMaterial();
            case CERRAR_TRABAJO -> cerrarTrabajo();
            case SALIR -> salir();
        }
    }

    public Cliente leerCliente() {
        System.out.println("Introduzca los datos del cliente.");
        return new Cliente(Consola.leerCadena("Ingrese el nombre: "), Consola.leerCadena("Ingrese el DNI: "), Consola.leerCadena("Ingrese el teléfono: "));
    }

    public Cliente leerClienteDni() {
        return new Cliente(Cliente.get(Consola.leerCadena("Ingrese el DNI: ")));
    }

    public String leerNuevoNombre() {
        return Consola.leerCadena("Ingrese el nombre: ");
    }

    public String leerNuevoTelefono() {
        return Consola.leerCadena("Ingrese el teléfono: ");
    }

    public Vehiculo leerVehiculo() {
        System.out.println("Introduzca los datos del vehículo.");
        return new Vehiculo(Consola.leerCadena("Ingrese la marca: "), Consola.leerCadena("Ingrese el modelo: "), Consola.leerCadena("Ingrese la matricula: "));
    }

    public Vehiculo leerVehiculoMatricula() {
        return Vehiculo.get(Consola.leerCadena("Ingrese la matrícula: "));
    }

    public Trabajo leerRevision() {
        System.out.println("Introduzca los datos de la revisión.");
        return new Revision(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio de la revisión: ")));
    }

    public Trabajo leerMecanico() {
        System.out.println("Introduzca los datos del trabajo mecánico.");
        return new Mecanico(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio del trabajo mecánico: ")));
    }

    public Trabajo leerTrabajoVehiculo() {
        System.out.println("Introduzca los datos del vehiculo al cual pertenece el trabajo.");
        Trabajo trabajoAbierto = null;
        List<Trabajo> trabajosVehiculo = gestorEventos.getTrabajos(leerVehiculoMatricula());
        Iterator<Trabajo> iterador = trabajosVehiculo.iterator();
        boolean estaCerrado = true;
        while (iterador.hasNext() && estaCerrado) {
            Trabajo trabajoVehiculo = iterador.next();
            if (!trabajoVehiculo.estaCerrado()) {
                if (trabajoVehiculo instanceof Revision revision) {
                    trabajoAbierto = new Revision(revision);
                } else if (trabajoVehiculo instanceof Mecanico mecanico) {
                    trabajoAbierto = new Mecanico(mecanico);
                }
                estaCerrado = false;
            }
        }
        return trabajoAbierto;
    }

    public Trabajo leerTrabajo() {
        System.out.println("Tipos de trabajo: ");
        System.out.println("[1] Revisión.");
        System.out.println("[2] Mecánico.");
        System.out.print("Ingrese el número correspondiente a su tipo de trabajo elegido: ");
        int numTipoTrabajo = Entrada.entero();
        while (numTipoTrabajo < 1 || numTipoTrabajo > 2) {
            System.out.println("ERROR: Número invalido. Inténtelo de nuevo.");
            System.out.println("Tipos de trabajo: ");
            System.out.println("[1] Revisión.");
            System.out.println("[2] Mecánico.");
            System.out.print("Ingrese el número correspondiente a su tipo de trabajo elegido: ");
            numTipoTrabajo = Entrada.entero();
        }
        Trabajo trabajoDevuleto = null;
        if (numTipoTrabajo == 1) {
            trabajoDevuleto = new Revision(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio de la revisión: ")));
        }
        if (numTipoTrabajo == 2) {
            trabajoDevuleto = new Mecanico(leerClienteDni(), leerVehiculoMatricula(), LocalDate.parse(Consola.leerFecha("Ingrese la fecha de inicio del trabajo mecánico: ")));
        }
        return trabajoDevuleto;
    }

    public int leerHoras() {
        return Consola.leerEntero("Introduzca las horas que desee añadir a el trabajo: ");
    }

    public float leerPrecioMaterial() {
        return Consola.leerReal("Introduzca el precio que desee añadir al material del trabajo: ");
    }

    public LocalDate leerFechaCierre() {
        return LocalDate.parse(Consola.leerFecha("Introduzca la fecha de cierre del trabajo: "));
    }

    private void insertarCliente() {
        Consola.mostrarCabecera("INSERCIÓN DE CLIENTE ACTIVADA");
        String mensajeCliente = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Cliente clienteAInsertar = leerCliente();
                if (controlador.buscar(clienteAInsertar) != null) {
                    mensajeCliente = "El cliente ya ha sido registrado anteriormente. Por favor, verifica los detalles e inténtalo de nuevo.";
                } else {
                    controlador.insertar(clienteAInsertar);
                    mensajeCliente = String.format("¡Cliente insertado exitosamente en el sistema! A continuación se muestran los detalles: %s", controlador.buscar(clienteAInsertar));
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeCliente);
    }

    private void insertarVehiculo() {
        Consola.mostrarCabecera("INSERCIÓN DE VEHÍCULO ACTIVADA");
        String mensajeVehiculo = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Vehiculo vehiculoAInsertar = leerVehiculo();
                if (controlador.buscar(vehiculoAInsertar) != null) {
                    mensajeVehiculo = "El vehículo ya ha sido registrado anteriormente. Por favor, verifica los detalles e inténtalo de nuevo.";
                } else {
                    controlador.insertar(vehiculoAInsertar);
                    mensajeVehiculo = String.format("¡Vehículo insertado exitosamente en el sistema! A continuación se muestran los detalles: %s", controlador.buscar(vehiculoAInsertar));
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeVehiculo);
    }

    private void insertarTrabajo() {
        Consola.mostrarCabecera("INSERCIÓN DE TRABAJO ACTIVADA");
        String mensajeTrabajo = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Trabajo trabajoAInsertar = leerTrabajo();
                Cliente clienteTrabajo = controlador.buscar(trabajoAInsertar.getCliente());
                Vehiculo vehiculoTrabajo = controlador.buscar(trabajoAInsertar.getVehiculo());
                if (clienteTrabajo == null) {
                    mensajeTrabajo = "No se puede insertar el trabajo porque el cliente especificado no existe en la base de datos. Por favor, registra al cliente primero y luego intenta nuevamente.";
                } else if (vehiculoTrabajo == null) {
                    mensajeTrabajo = "No se puede insertar el trabajo porque el vehículo especificado no existe en la base de datos. Por favor, registra al vehículo primero y luego intenta nuevamente.";
                } else {
                    if (controlador.buscar(trabajoAInsertar) != null) {
                        mensajeTrabajo = "El trabajo ya ha sido registrado anteriormente. Por favor, verifica los detalles e inténtalo de nuevo.";
                    } else {
                        Trabajo trabajoInsertado = null;
                        if (trabajoAInsertar instanceof Revision) {
                            trabajoInsertado = new Revision(clienteTrabajo, vehiculoTrabajo, trabajoAInsertar.getFechaInicio());
                        } else if (trabajoAInsertar instanceof Mecanico) {
                            trabajoInsertado = new Mecanico(clienteTrabajo, vehiculoTrabajo, trabajoAInsertar.getFechaInicio());
                        }
                        controlador.insertar(trabajoInsertado);
                        mensajeTrabajo = String.format("¡Trabajo insertado exitosamente en el sistema! A continuación se muestran los detalles: %s", controlador.buscar(trabajoInsertado));
                    }
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeTrabajo);
    }

    private void buscarCliente() {
        Consola.mostrarCabecera("BÚSQUEDA DE CLIENTE ACTIVADA");
        boolean saltaExcepcion = true;
        Cliente clienteBuscado = null;
        while (saltaExcepcion) {
            try {
                clienteBuscado = controlador.buscar(leerClienteDni());
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        String mensajeCliente = (clienteBuscado == null) ? "No se ha encontrado ningún cliente con esa información. Por favor, inténtalo de nuevo." : String.format("¡Cliente encontrado! A continuación se muestran los detalles: %s", clienteBuscado);
        System.out.println(mensajeCliente);
    }

    private void buscarVehiculo() {
        Consola.mostrarCabecera("BÚSQUEDA DE VEHÍCULO ACTIVADA");
        boolean saltaExcepcion = true;
        Vehiculo vehiculoBuscado = null;
        while (saltaExcepcion) {
            try {
                vehiculoBuscado = controlador.buscar(leerVehiculoMatricula());
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        String mensajeTurismo = (vehiculoBuscado == null) ? "No se ha encontrado ningún vehículo con esa información. Por favor, inténtalo de nuevo." : String.format("¡Vehículo encontrado! A continuación se muestran los detalles: %s", vehiculoBuscado);
        System.out.println(mensajeTurismo);
    }

    private void buscarTrabajo() {
        Consola.mostrarCabecera("BÚSQUEDA DE TRABAJO ACTIVADA");
        boolean saltaExcepcion = true;
        Trabajo trabajoBuscada = null;
        while (saltaExcepcion) {
            try {
                trabajoBuscada = controlador.buscar(leerTrabajo());
                saltaExcepcion = false;
            } catch (IllformedLocaleException e) {
                System.out.println(e.getMessage());
            }
        }
        String mensajeAlquiler = (trabajoBuscada == null) ? "No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo." : String.format("¡Trabajo encontrado! A continuación se muestran los detalles: %s", trabajoBuscada);
        System.out.println(mensajeAlquiler);
    }

    private void modificarCliente() {
        Consola.mostrarCabecera("MODIFICACIÓN DE CLIENTE ACTIVADA");
        boolean saltaExcepcion = true;
        String mensajeCliente = null;
        while (saltaExcepcion) {
            try {
                Cliente clienteAModificar = controlador.buscar(leerClienteDni());
                if (clienteAModificar == null) {
                    mensajeCliente = "No se ha encontrado ningún cliente con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    boolean haSidoModificado = controlador.modificar(clienteAModificar, leerNuevoNombre(), leerNuevoTelefono());
                    mensajeCliente = (haSidoModificado) ? String.format("¡Cliente modificado exitosamente! A continuación se muestran los detalles: %s", controlador.buscar(clienteAModificar)) : "Lo siento, no se ha podido modificar al cliente. Por favor, verifica los detalles e inténtalo de nuevo.";
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeCliente);
    }

    private void anadirHoras() {
        Consola.mostrarCabecera("AÑADIR HORAS A TRABAJO ACTIVADA");
        boolean saltaExcepcion = true;
        String mensajeTrabajo = null;
        boolean yaTieneFechaCierre = true;
        Trabajo trabajoAnadirHoras = null;
        while (saltaExcepcion) {
            try {
                List<Trabajo> trabajosVehiculo = controlador.getTrabajos(leerVehiculoMatricula());
                if (trabajosVehiculo.isEmpty()) {
                    mensajeTrabajo = "No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    for (Trabajo trabajoVehiculo : trabajosVehiculo) {
                        if (!trabajoVehiculo.estaCerrado()) {
                            yaTieneFechaCierre = false;
                            trabajoAnadirHoras = trabajoVehiculo;
                        }
                    }
                    if (yaTieneFechaCierre) {
                        mensajeTrabajo = "El cierre del trabajo ya ha sido registrado anteriormente, por lo tanto, no es posible añadir más horas a este trabajo.";
                    }
                }
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (!yaTieneFechaCierre) {
                saltaExcepcion = true;
                while (saltaExcepcion) {
                    try {
                        controlador.anadirHoras(trabajoAnadirHoras, leerHoras());
                        mensajeTrabajo = String.format("¡La cantidad de horas ha sido añadida exitosamente a el trabajo! A continuación se muestran los detalles: %s", controlador.buscar(trabajoAnadirHoras));
                        saltaExcepcion = false;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        System.out.println(mensajeTrabajo);
    }

    private void anadirPrecioMaterial() {
        Consola.mostrarCabecera("AÑADIR PRECIO MATERIAL A TRABAJO ACTIVADA");
        boolean saltaExcepcion = true;
        String mensajeTrabajo = null;
        boolean yaTieneFechaCierre = true;
        Trabajo trabajoAnadirPrecioMaterial = null;
        while (saltaExcepcion) {
            try {
                List<Trabajo> trabajosVehiculo = controlador.getTrabajos(leerVehiculoMatricula());
                if (trabajosVehiculo.isEmpty()) {
                    mensajeTrabajo = "No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    for (Trabajo trabajoVehiculo : trabajosVehiculo) {
                        if (!trabajoVehiculo.estaCerrado()) {
                            yaTieneFechaCierre = false;
                            trabajoAnadirPrecioMaterial = trabajoVehiculo;
                        }
                    }
                    if (yaTieneFechaCierre) {
                        mensajeTrabajo = "El cierre del trabajo ya ha sido registrado anteriormente, por lo tanto, no es posible añadir el precio del material a este trabajo.";
                    }
                }
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (!yaTieneFechaCierre) {
                saltaExcepcion = true;
                while (saltaExcepcion) {
                    try {
                        controlador.anadirPrecioMaterial(trabajoAnadirPrecioMaterial, leerPrecioMaterial());
                        mensajeTrabajo = String.format("¡El precio del material ha sido añadido exitosamente a el trabajo! A continuación se muestran los detalles: %s", controlador.buscar(trabajoAnadirPrecioMaterial));
                        saltaExcepcion = false;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        System.out.println(mensajeTrabajo);
    }

    private void cerrarTrabajo() {
        Consola.mostrarCabecera("CIERRE DE TRABAJO ACTIVADA");
        boolean saltaExcepcion = true;
        String mensajeTrabajo = null;
        boolean yaTieneFechaCierre = true;
        Trabajo trabajoACerrar = null;
        while (saltaExcepcion) {
            try {
                List<Trabajo> trabajoesVehiculo = controlador.getTrabajos(leerVehiculoMatricula());
                if (trabajoesVehiculo.isEmpty()) {
                    mensajeTrabajo = "No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    for (Trabajo trabajoVehiculo : trabajoesVehiculo) {
                        if (!trabajoVehiculo.estaCerrado()) {
                            yaTieneFechaCierre = false;
                            trabajoACerrar = trabajoVehiculo;
                        }
                    }
                    if (yaTieneFechaCierre) {
                        mensajeTrabajo = "El cierre del trabajo ya ha sido registrada anteriormente.";
                    }
                }
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            if (!yaTieneFechaCierre) {
                saltaExcepcion = true;
                while (saltaExcepcion) {
                    try {
                        controlador.cerrar(trabajoACerrar, leerFechaCierre());
                        mensajeTrabajo = String.format("¡Trabajo cerrado con éxito! A continuación se muestran los detalles: %s", controlador.buscar(trabajoACerrar));
                        saltaExcepcion = false;
                    } catch (OperationNotSupportedException | IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        System.out.println(mensajeTrabajo);
    }

    private void borrarCliente() {
        Consola.mostrarCabecera("ELIMINACIÓN DE CLIENTE ACTIVADA");
        String mensajeCliente = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Cliente clienteABorrar = controlador.buscar(leerClienteDni());
                if (clienteABorrar == null) {
                    mensajeCliente = "No se ha encontrado ningún cliente con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    controlador.borrar(clienteABorrar);
                    mensajeCliente = "¡Cliente eliminado exitosamente del sistema! Todos los trabajos asociados a este cliente también han sido eliminadas.";
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeCliente);
    }

    private void borrarVehiculo() {
        Consola.mostrarCabecera("ELIMINACIÓN DE VEHÍCULO ACTIVADA");
        String mensajeVehiculo = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Vehiculo vehiculoABorrar = controlador.buscar(leerVehiculoMatricula());
                if (vehiculoABorrar == null) {
                    mensajeVehiculo = "No se ha encontrado ningún vehículo con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    controlador.borrar(vehiculoABorrar);
                    mensajeVehiculo = "¡Vehículo eliminado exitosamente del sistema! Todos los alquileres asociados a este cliente también han sido eliminados.";
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeVehiculo);
    }

    private void borrarTrabajo() {
        Consola.mostrarCabecera("ELIMINACIÓN DE TRABAJO ACTIVADA");
        String mensajeTrabajo = null;
        boolean saltaExcepcion = true;
        while (saltaExcepcion) {
            try {
                Trabajo trabajoABorrar = controlador.buscar(leerTrabajo());
                if (trabajoABorrar == null) {
                    mensajeTrabajo = "No se ha encontrado ningún trabajo con esa información. Por favor, inténtalo de nuevo.";
                } else {
                    controlador.borrar(trabajoABorrar);
                    mensajeTrabajo = "¡Trabajo eliminado exitosamente del sistema!";
                }
                saltaExcepcion = false;
            } catch (OperationNotSupportedException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(mensajeTrabajo);
    }

    private void listarClientes() {
        Consola.mostrarCabecera("LISTADO DE CLIENTES ACTIVADA");
        List<Cliente> clientes = controlador.getClientes();
        StringBuilder mensajeCliente = new StringBuilder();
        if (clientes.isEmpty()) {
            mensajeCliente.append("No se encontraron clientes en la lista. ¡Registra un nuevo cliente para comenzar!  ");
        } else {
            int indiceLista = 1;
            mensajeCliente.append("Mostrando lista de clientes disponibles: \n");
            for (Cliente cliente : clientes) {
                mensajeCliente.append(String.format("%s. %s%n", indiceLista++, cliente));
            }
        }
        mensajeCliente.replace(mensajeCliente.length() - 2, mensajeCliente.length(), "");
        System.out.println(mensajeCliente);
    }

    private void listarVehiculos() {
        Consola.mostrarCabecera("LISTADO DE VEHÍCULOS ACTIVADA");
        List<Vehiculo> vehiculos = controlador.getVehiculos();
        StringBuilder mensajeVehiculo = new StringBuilder();
        if (vehiculos.isEmpty()) {
            mensajeVehiculo.append("No se encontraron vehículos en la lista. ¡Registra un nuevo vehículo para comenzar!  ");
        } else {
            int indiceLista = 1;
            mensajeVehiculo.append("Mostrando lista de vehículos disponibles: \n");
            for (Vehiculo vehiculo : vehiculos) {
                mensajeVehiculo.append(String.format("%s. %s%n", indiceLista++, vehiculo));
            }
        }
        mensajeVehiculo.replace(mensajeVehiculo.length() - 2, mensajeVehiculo.length(), "");
        System.out.println(mensajeVehiculo);
    }

    private void listarTrabajos() {
        Consola.mostrarCabecera("LISTADO DE TRABAJOS ACTIVADA");
        List<Trabajo> trabajos = controlador.getTrabajos();
        StringBuilder mensajeTrabajos = new StringBuilder();
        if (trabajos.isEmpty()) {
            mensajeTrabajos.append("No se encontraron trabajos en la lista. ¡Registra un nuevo trabajo para comenzar!  ");
        } else {
            int indiceLista = 1;
            mensajeTrabajos.append("Mostrando lista de trabajos disponibles: \n");
            for (Trabajo trabajo : trabajos) {
                mensajeTrabajos.append(String.format("%s. %s%n", indiceLista++, trabajo));
            }
        }
        mensajeTrabajos.replace(mensajeTrabajos.length() - 2, mensajeTrabajos.length(), "");
        System.out.println(mensajeTrabajos);
    }

    private void listarTrabajosCliente() {
        Consola.mostrarCabecera("LISTADO DE TRABAJOS POR CLIENTE ACTIVADA");
        System.out.print("Introduce el DNI del cliente que deseas el listado de sus trabajos: ");
        boolean saltaExcepcion = true;
        List<Trabajo> trabajoesCliente = null;
        while (saltaExcepcion) {
            try {
                trabajoesCliente = controlador.getTrabajos(leerClienteDni());
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        StringBuilder mensajeTrabajoesCliente = new StringBuilder();
        if (trabajoesCliente.isEmpty()) {
            mensajeTrabajoesCliente.append("No se encontraron trabajos asociadas a este cliente en la lista. ¡Asegúrate de haber registrado al menos un trabajo para este cliente!  ");
        } else {
            int indiceLista = 1;
            mensajeTrabajoesCliente.append("Mostrando lista de trabajos asociadas a este cliente disponibles: \n");
            for (Trabajo trabajoCliente : trabajoesCliente) {
                mensajeTrabajoesCliente.append(String.format("%s. %s%n", indiceLista++, trabajoCliente));
            }
        }
        mensajeTrabajoesCliente.replace(mensajeTrabajoesCliente.length() - 2, mensajeTrabajoesCliente.length(), "");
        System.out.println(mensajeTrabajoesCliente);
    }

    private void listarTrabajosVehiculo() {
        Consola.mostrarCabecera("LISTADO DE TRABAJOS POR VEHÍCULO ACTIVADA");
        System.out.print("Introduce la matrícula del vehículo que deseas el listado de sus trabajos: ");
        boolean saltaExcepcion = true;
        List<Trabajo> trabajoesVehiculo = null;
        while (saltaExcepcion) {
            try {
                trabajoesVehiculo = controlador.getTrabajos(leerVehiculoMatricula());
                saltaExcepcion = false;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        StringBuilder mensajeTrabajoesVehiculo = new StringBuilder();
        if (trabajoesVehiculo.isEmpty()) {
            mensajeTrabajoesVehiculo.append("No se encontraron trabajos asociadas a este vehículo en la lista. ¡Asegúrate de haber registrado al menos un trabajo para este vehículo!  ");
        } else {
            int indiceLista = 1;
            mensajeTrabajoesVehiculo.append("Mostrando lista de trabajos asociados a este vehículo disponibles: \n");
            for (Trabajo trabajoVehiculo : trabajoesVehiculo) {
                mensajeTrabajoesVehiculo.append(String.format("%s. %s%n", indiceLista++, trabajoVehiculo));
            }
        }
        mensajeTrabajoesVehiculo.replace(mensajeTrabajoesVehiculo.length() - 2, mensajeTrabajoesVehiculo.length(), "");
        System.out.println(mensajeTrabajoesVehiculo);
    }

    private void salir() {
        controlador.terminar();
    }
}
*/

