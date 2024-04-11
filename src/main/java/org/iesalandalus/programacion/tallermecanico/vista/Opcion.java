package org.iesalandalus.programacion.tallermecanico.vista;

import java.util.HashMap;
import java.util.Map;

public enum Opcion {
    INSERTAR_CLIENTE("Insertar cliente", 1),
    BUSCAR_CLIENTE("Buscar cliente", 2),
    BORRAR_CLIENTE("Borrar cliente", 3),
    LISTAR_CLIENTES("Listar clientes", 4),
    MODIFICAR_CLIENTE("Modificar cliente", 5),
    INSERTAR_VEHICULO("Insertar vehículo", 6),
    BUSCAR_VEHICULO("Buscar vehículo", 7),
    BORRAR_VEHICULO("Borrar vehículo", 8),
    LISTAR_VEHICULOS("Listar vehículos", 9),
    INSERTAR_REVISION("Insertar revisión", 10),
    BUSCAR_REVISION("Buscar revisión", 11),
    BORRAR_REVISION("Borrar revisión", 12),
    LISTAR_REVISIONES("Listar revisiones", 13),
    LISTAR_REVISIONES_CLIENTE("Listar revisiones cliente", 14),
    LISTAR_REVISIONES_VEHICULO("Listar revisiones vehículo", 15),
    ANADIR_HORAS_REVISION("Añadir horas revisión", 16),
    ANADIR_PRECIO_MATERIAL_REVISION("Añadir precio material revisión", 17),
    CERRAR_REVISION("Cerrar revisión", 18),
    SALIR("Salir", 19);

    private final String mensaje;
    private final int numeroOpcion;
    private static final Map<Integer, Opcion> opciones = new HashMap<>();

    static {
        for (Opcion opcion : Opcion.values()) {
            opciones.put(opcion.numeroOpcion, opcion);
        }
    }

    Opcion(String mensaje, int numeroOpcion) {
        this.mensaje = mensaje;
        this.numeroOpcion = numeroOpcion;
    }

    public static boolean esValida(int numeroOpcion) {
        return (opciones.containsKey(numeroOpcion));
    }

    public static Opcion get(int numeroOpcion) {
        if (!esValida(numeroOpcion)) {
            throw new IllegalArgumentException("Número de la opción invalido. Inténtelo de nuevo.");
        }
        return opciones.get(numeroOpcion);
    }

    @Override
    public String toString() {
        return String.format("%s.- %s", this.numeroOpcion, this.mensaje);
    }
}