package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Vehiculos;
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
    private org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos IVehiculos;

    @BeforeAll
    public static void setup() {
        vehiculo1 = mock();
        when(vehiculo1.matricula()).thenReturn("1234BCD");
        vehiculo2 = mock();
        when(vehiculo2.matricula()).thenReturn("1111BBB");
    }

    @BeforeEach
    void init() {
        IVehiculos = new Vehiculos();
    }

    @Test
    void constructorCreaVehiculosCorrectamente() {
        assertNotNull(IVehiculos);
        assertEquals(0, IVehiculos.get().size());
    }

    @Test
    void getDevuelveVehiculosCorrectamente() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo2));
        List<Vehiculo> copiaVehiculos = IVehiculos.get();
        assertEquals(vehiculo1, copiaVehiculos.get(0));
        assertSame(vehiculo1, copiaVehiculos.get(0));
        assertEquals(vehiculo2, copiaVehiculos.get(1));
        assertSame(vehiculo2, copiaVehiculos.get(1));
    }

    @Test
    void insertarVehiculoValidoInsertaCorrectamente() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        assertEquals(vehiculo1, IVehiculos.buscar(vehiculo1));
        assertSame(vehiculo1, IVehiculos.buscar(vehiculo1));
    }

    @Test
    void insertarVehiculoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> IVehiculos.insertar(null));
        assertEquals("No se puede insertar un vehículo nulo.", npe.getMessage());
    }

    @Test
    void insertarVehiculoRepetidoLanzaExcepcion() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> IVehiculos.insertar(vehiculo1));
        assertEquals("Ya existe un vehículo con esa matrícula.", onse.getMessage());
    }

    @Test
    void borrarVehiculoExistenteBorraVehiculoCorrectamente() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        assertDoesNotThrow(() -> IVehiculos.borrar(vehiculo1));
        assertNull(IVehiculos.buscar(vehiculo1));
    }

    @Test
    void borrarVehiculoNoExistenteLanzaExcepcion() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> IVehiculos.borrar(vehiculo2));
        assertEquals("No existe ningún vehículo con esa matrícula.", onse.getMessage());
    }

    @Test
    void borrarVehiculoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> IVehiculos.borrar(null));
        assertEquals("No se puede borrar un vehículo nulo.", npe.getMessage());
    }

    @Test
    void busarVehiculoExistenteDevuelveVehiculoCorrectamente() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        assertEquals(vehiculo1, IVehiculos.buscar(vehiculo1));
        assertSame(vehiculo1, IVehiculos.buscar(vehiculo1));
    }

    @Test
    void busarVehiculoNoExistenteDevuelveVehiculoNulo() {
        assertNull(IVehiculos.buscar(vehiculo1));
    }

    @Test
    void buscarVehiculoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> IVehiculos.insertar(vehiculo1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> IVehiculos.buscar(null));
        assertEquals("No se puede buscar un vehículo nulo.", npe.getMessage());
    }
}