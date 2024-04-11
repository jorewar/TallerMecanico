package org.iesalandalus.programacion.tallermecanico.vista.eventos;

import java.util.HashMap;
import java.util.Map;

public enum Evento {
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
    INSERTAR_MECANICO("Insertar trabajo mecánico", 11),
    BUSCAR_TRABAJO("Buscar trabajo", 12),
    BORRAR_TRABAJO("Borrar trabajo", 13),
    LISTAR_TRABAJOS("Listar trabajos", 14),
    LISTAR_TRABAJOS_CLIENTE("Listar trabajos cliente", 15),
    LISTAR_TRABAJOS_VEHICULO("Listar trabajos vehículo", 16),
    ANADIR_HORAS_TRABAJO("Añadir horas trabajo", 17),
    ANADIR_PRECIO_MATERIAL_TRABAJO("Añadir precio material trabajo", 18),
    CERRAR_TRABAJO("Cerrar trabajo", 19),
    SALIR("Salir", 20);

    private final String texto;
    private final int codigo;
    private static final Map<Integer, Evento> eventos = new HashMap<>();

    static {
        for (Evento opcion : Evento.values()) {
            eventos.put(opcion.codigo, opcion);
        }
    }

    Evento(String texto, int evento) {
        this.texto = texto;
        this.codigo = evento;
    }

    public static boolean esValida(int codigo) {
        return (eventos.containsKey(codigo));
    }

    public static Evento get(int codigo) {
        if (!esValida(codigo)) {
            throw new IllegalArgumentException("Número de código invalido. Inténtelo de nuevo.");
        }
        return eventos.get(codigo);
    }

    @Override
    public String toString() {
        return String.format("%s.- %s", this.codigo, this.texto);
    }
}