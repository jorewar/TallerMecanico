package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Trabajos;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TrabajosTest {

    private static Revision revision;
    private static Mecanico mecanico;
    private static Trabajo trabajo3;
    private static Cliente cliente1;
    private static Cliente cliente2;
    private static Vehiculo vehiculo1;
    private static Vehiculo vehiculo2;
    private static LocalDate hoy;
    private static LocalDate ayer;
    private static LocalDate anteayer;
    private static LocalDate semanaPasada;
    private ITrabajos trabajos;

    @BeforeAll
    static void setup() {
        hoy = LocalDate.now();
        ayer = hoy.minusDays(1);
        anteayer = hoy.minusDays(2);
        semanaPasada = hoy.minusDays(7);
        cliente1 = mock();
        when(cliente1.getDni()).thenReturn("11223344B");
        cliente2 = mock();
        when(cliente2.getDni()).thenReturn("11111111H");
        vehiculo1 = mock();
        when(vehiculo1.matricula()).thenReturn("1234BCD");
        vehiculo2 = mock();
        when(vehiculo2.matricula()).thenReturn("1111BBB");
    }

    @BeforeEach
    void init() {
        trabajos = new Trabajos();
        revision = mock();
        when(revision.getCliente()).thenReturn(cliente1);
        when(revision.getVehiculo()).thenReturn(vehiculo1);
        when(revision.getFechaInicio()).thenReturn(semanaPasada);
        mecanico = mock();
        when(mecanico.getCliente()).thenReturn(cliente1);
        when(mecanico.getVehiculo()).thenReturn(vehiculo2);
        when(mecanico.getFechaInicio()).thenReturn(ayer);
        trabajo3 = mock();
        when(trabajo3.getCliente()).thenReturn(cliente2);
        when(trabajo3.getVehiculo()).thenReturn(vehiculo1);
        when(trabajo3.getFechaInicio()).thenReturn(ayer);
    }

    @Test
    void constructorCreaTrabajosCorrectamente() {
        assertNotNull(trabajos);
        assertEquals(0, trabajos.get().size());
    }

    @Test
    void getDevuelveTrabajosCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        when(revision.getFechaFin()).thenReturn(anteayer);
        when(revision.estaCerrado()).thenReturn(true);
        assertDoesNotThrow(() -> trabajos.insertar(trabajo3));
        List<Trabajo> copiaTrabajos = trabajos.get();
        assertEquals(2, copiaTrabajos.size());
        assertEquals(revision, copiaTrabajos.get(0));
        assertSame(revision, copiaTrabajos.get(0));
        assertEquals(trabajo3, copiaTrabajos.get(1));
        assertSame(trabajo3, copiaTrabajos.get(1));
    }

    @Test
    void getClienteValidoDevuelveTrabajosClienteCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        when(revision.getFechaFin()).thenReturn(anteayer);
        when(revision.estaCerrado()).thenReturn(true);
        assertDoesNotThrow(() -> trabajos.insertar(mecanico));
        assertDoesNotThrow(() -> trabajos.insertar(trabajo3));
        List<Trabajo> trabajosCliente = trabajos.get(cliente1);
        assertEquals(2, trabajosCliente.size());
        assertEquals(revision, trabajosCliente.get(0));
        assertSame(revision, trabajosCliente.get(0));
        assertEquals(mecanico, trabajosCliente.get(1));
        assertSame(mecanico, trabajosCliente.get(1));
    }

    @Test
    void getVehiculoValidoDevuelveTrabajosVehiculoCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        when(revision.getFechaFin()).thenReturn(anteayer);
        when(revision.estaCerrado()).thenReturn(true);
        assertDoesNotThrow(() -> trabajos.insertar(mecanico));
        assertDoesNotThrow(() -> trabajos.insertar(trabajo3));
        List<Trabajo> trabajosVehiculo = trabajos.get(vehiculo1);
        assertEquals(2, trabajosVehiculo.size());
        assertEquals(revision, trabajosVehiculo.get(0));
        assertSame(revision, trabajosVehiculo.get(0));
        assertEquals(trabajo3, trabajosVehiculo.get(1));
        assertSame(trabajo3,trabajosVehiculo.get(1));
    }

    @Test
    void insertarTrabajoValidaInsertaCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertEquals(revision, trabajos.buscar(revision));
        assertSame(revision, trabajos.buscar(revision));
    }

    @Test
    void insertarTrabajoNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.insertar(null));
        assertEquals("No se puede insertar un trabajo nulo.", npe.getMessage());
    }

    @Test
    void insertarTrabajoClienteTrabajoAbiertaLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.insertar(mecanico));
        assertEquals("El cliente tiene otro trabajo en curso.", onse.getMessage());
    }

    @Test
    void insertarTrabajoVehiculoTrabajoAbiertaLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.insertar(trabajo3));
        assertEquals("El vehículo está actualmente en el taller.", onse.getMessage());
    }

    @Test
    void insertarTrabajoClienteTrabajoAnteiorLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertDoesNotThrow(() -> trabajos.cerrar(revision, anteayer));
        when(revision.getFechaInicio()).thenReturn(ayer);
        when(revision.getFechaFin()).thenReturn(anteayer);
        when(revision.estaCerrado()).thenReturn(true);
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        when(revision.estaCerrado()).thenReturn(false);
        assertDoesNotThrow(() -> trabajos.cerrar(revision, ayer));
        when(revision.estaCerrado()).thenReturn(true);
        when(revision.getFechaFin()).thenReturn(ayer);
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.insertar(mecanico));
        assertEquals("El cliente tiene otro trabajo posterior.", onse.getMessage());
    }

    @Test
    void insertarTrabajoVehiculoTrabajoAnteriorLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertDoesNotThrow(() -> trabajos.cerrar(revision, anteayer));
        when(revision.getFechaInicio()).thenReturn(ayer);
        when(revision.getFechaFin()).thenReturn(anteayer);
        when(revision.estaCerrado()).thenReturn(true);
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        when(revision.estaCerrado()).thenReturn(false);
        assertDoesNotThrow(() -> trabajos.cerrar(revision, ayer));
        when(revision.estaCerrado()).thenReturn(true);
        when(revision.getFechaFin()).thenReturn(ayer);
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.insertar(trabajo3));
        assertEquals("El vehículo tiene otro trabajo posterior.", onse.getMessage());
    }

    @Test
    void anadirHorasTrabajoValidoHorasValidasAnadeHorasCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertDoesNotThrow(() -> trabajos.anadirHoras(revision, 10));
        when(revision.getHoras()).thenReturn(10);
        Trabajo trabajo = trabajos.buscar(revision);
        assertEquals(10, trabajo.getHoras());
    }

    @Test
    void anadirHorasTrabajoNuloHorasValidasLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.anadirHoras(null, 10));
        assertEquals("No puedo añadir horas a un trabajo nulo.", npe.getMessage());
    }

    @Test
    void anadirHorasTrabajoNoExistenteHorasValidasLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.anadirHoras(revision, 10));
        assertEquals("No existe ningún trabajo abierto para dicho vehículo.", onse.getMessage());
    }

    @Test
    void anadirPrecioMaterialRevisionValidaPrecioMaterialValidoLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.anadirPrecioMaterial(revision, 100f));
        assertEquals("No se puede añadir precio al material para este tipo de trabajos.", onse.getMessage());
    }

    @Test
    void anadirPrecioMaterialMecancioValidoPrecioMaterialValidoAnadaPrecioMaterialCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(mecanico));
        assertDoesNotThrow(() -> trabajos.anadirPrecioMaterial(mecanico, 100f));
        when(mecanico.getPrecioMaterial()).thenReturn(100f);
        Mecanico trabajo = (Mecanico) trabajos.buscar(mecanico);
        assertEquals(100f, trabajo.getPrecioMaterial());
    }

    @Test
    void anadirPrecioMaterialTrabajoNuloPrecioMaterialValidoLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.anadirPrecioMaterial(null, 100f));
        assertEquals("No puedo añadir precio del material a un trabajo nulo.", npe.getMessage());
    }

    @Test
    void anadirPrecioMaterialTrabajoNoExistentePrecioMaterialValidoLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.anadirPrecioMaterial(revision, 100f));
        assertEquals("No existe ningún trabajo abierto para dicho vehículo.", onse.getMessage());
    }

    @Test
    void cerrarTrabajoNuloFechaValidaLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.cerrar(null, ayer));
        assertEquals("No puedo cerrar un trabajo nulo.", npe.getMessage());
    }

    @Test
    void cerrarTrabajoNoExistenteFechaValidaLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.cerrar(revision, hoy));
        assertEquals("No existe ningún trabajo abierto para dicho vehículo.", onse.getMessage());
    }

    @Test
    void cerrarTrabajoValioaFechaValidaCierraCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertDoesNotThrow(() -> trabajos.cerrar(revision, ayer));
        when(revision.getFechaFin()).thenReturn(ayer);
        Trabajo trabajo = trabajos.buscar(revision);
        assertEquals(ayer, trabajo.getFechaFin());
    }

    @Test
    void borrarTrabajoExistenteBorraTrabajoCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertDoesNotThrow(() -> trabajos.borrar(revision));
        assertNull(trabajos.buscar(revision));
    }

    @Test
    void borrarTrabajoNoExistenteLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> trabajos.borrar(mecanico));
        assertEquals("No existe ningún trabajo igual.", onse.getMessage());
    }

    @Test
    void borrarTrabajoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.borrar(null));
        assertEquals("No se puede borrar un trabajo nulo.", npe.getMessage());
    }

    @Test
    void buscarTrabajoExistenteDevuelveTrabajoCorrectamente() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        assertEquals(revision, trabajos.buscar(revision));
        assertSame(revision, trabajos.buscar(revision));
    }

    @Test
    void busarTrabajoNoExistenteDevuelveTrabajoNula() {
        assertNull(trabajos.buscar(revision));
    }

    @Test
    void buscarTrabajoNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> trabajos.insertar(revision));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> trabajos.buscar(null));
        assertEquals("No se puede buscar un trabajo nulo.", npe.getMessage());
    }
}