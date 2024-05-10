package org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VehiculosTest {

    private static Vehiculo vehiculo1;
    private static Vehiculo vehiculo2;
    private IVehiculos vehiculos;

    @BeforeAll
    public static void setup() {
        vehiculo1 = mock();
        when(vehiculo1.matricula()).thenReturn("1234BCD");
        vehiculo2 = mock();
        when(vehiculo2.matricula()).thenReturn("1111BBB");
    }

    @BeforeEach
    void init() {
        vehiculos = Vehiculos.getInstancia();
        for (Vehiculo vehiculo : vehiculos.get()) {
            assertDoesNotThrow(() -> vehiculos.borrar(vehiculo));
        }
    }

    @Test
    void constructorCreaVehiculosCorrectamente() {
        assertNotNull(vehiculos);
        assertEquals(0, vehiculos.get().size());
    }

    @Test
    void getDevuelveVehiculosCorrectamente() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo2));
        List<Vehiculo> copiaVehiculos = vehiculos.get();
        assertEquals(vehiculo1, copiaVehiculos.get(0));
        assertSame(vehiculo1, copiaVehiculos.get(0));
        assertEquals(vehiculo2, copiaVehiculos.get(1));
        assertSame(vehiculo2, copiaVehiculos.get(1));
    }

    @Test
    void insertarVehiculoValidoInsertaCorrectamente() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        assertEquals(vehiculo1, vehiculos.buscar(vehiculo1));
        assertSame(vehiculo1, vehiculos.buscar(vehiculo1));
    }

    @Test
    void insertarVehiculoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> vehiculos.insertar(null));
        assertEquals("No se puede insertar un vehículo nulo.", npe.getMessage());
    }

    @Test
    void insertarVehiculoRepetidoLanzaExcepcion() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> vehiculos.insertar(vehiculo1));
        assertEquals("Ya existe un vehículo con esa matrícula.", onse.getMessage());
    }

    @Test
    void borrarVehiculoExistenteBorraVehiculoCorrectamente() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        assertDoesNotThrow(() -> vehiculos.borrar(vehiculo1));
        assertNull(vehiculos.buscar(vehiculo1));
    }

    @Test
    void borrarVehiculoNoExistenteLanzaExcepcion() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> vehiculos.borrar(vehiculo2));
        assertEquals("No existe ningún vehículo con esa matrícula.", onse.getMessage());
    }

    @Test
    void borrarVehiculoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> vehiculos.borrar(null));
        assertEquals("No se puede borrar un vehículo nulo.", npe.getMessage());
    }

    @Test
    void busarVehiculoExistenteDevuelveVehiculoCorrectamente() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        assertEquals(vehiculo1, vehiculos.buscar(vehiculo1));
        assertSame(vehiculo1, vehiculos.buscar(vehiculo1));
    }

    @Test
    void busarVehiculoNoExistenteDevuelveVehiculoNulo() {
        assertNull(vehiculos.buscar(vehiculo1));
    }

    @Test
    void buscarVehiculoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> vehiculos.insertar(vehiculo1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> vehiculos.buscar(null));
        assertEquals("No se puede buscar un vehículo nulo.", npe.getMessage());
    }
}