package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Revision;
import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Vehiculo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RevisionesTest {

    private static Revision revision1;
    private static Revision revision2;
    private static Revision revision3;
    private static Cliente cliente1;
    private static Cliente cliente2;
    private static Vehiculo vehiculo1;
    private static Vehiculo vehiculo2;
    private static LocalDate hoy;
    private static LocalDate ayer;
    private static LocalDate anteayer;
    private static LocalDate semanaPasada;
    private Revisiones revisiones;

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
        revisiones = new Revisiones();
        revision1 = mock();
        when(revision1.getCliente()).thenReturn(cliente1);
        when(revision1.getVehiculo()).thenReturn(vehiculo1);
        when(revision1.getFechaInicio()).thenReturn(semanaPasada);
        revision2 = mock();
        when(revision2.getCliente()).thenReturn(cliente1);
        when(revision2.getVehiculo()).thenReturn(vehiculo2);
        when(revision2.getFechaInicio()).thenReturn(ayer);
        revision3 = mock();
        when(revision3.getCliente()).thenReturn(cliente2);
        when(revision3.getVehiculo()).thenReturn(vehiculo1);
        when(revision3.getFechaInicio()).thenReturn(ayer);
    }

    @Test
    void constructorCreaRevisionsCorrectamente() {
        assertNotNull(revisiones);
        assertEquals(0, revisiones.get().size());
    }

    @Test
    void getDevuelveRevisionesCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        when(revision1.getFechaFin()).thenReturn(anteayer);
        when(revision1.estaCerrada()).thenReturn(true);
        assertDoesNotThrow(() -> revisiones.insertar(revision3));
        List<Revision> copiaRevisiones = revisiones.get();
        assertEquals(2, copiaRevisiones.size());
        assertEquals(revision1, copiaRevisiones.get(0));
        assertSame(revision1, copiaRevisiones.get(0));
        assertEquals(revision3, copiaRevisiones.get(1));
        assertSame(revision3, copiaRevisiones.get(1));
    }

    @Test
    void getClienteValidoDevuelveRevisionesClienteCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        when(revision1.getFechaFin()).thenReturn(anteayer);
        when(revision1.estaCerrada()).thenReturn(true);
        assertDoesNotThrow(() -> revisiones.insertar(revision2));
        assertDoesNotThrow(() -> revisiones.insertar(revision3));
        List<Revision> revisionesCliente = revisiones.get(cliente1);
        assertEquals(2, revisionesCliente.size());
        assertEquals(revision1, revisionesCliente.get(0));
        assertSame(revision1, revisionesCliente.get(0));
        assertEquals(revision2, revisionesCliente.get(1));
        assertSame(revision2, revisionesCliente.get(1));
    }

    @Test
    void getVehiculoValidoDevuelveRevisionesVehiculoCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        when(revision1.getFechaFin()).thenReturn(anteayer);
        when(revision1.estaCerrada()).thenReturn(true);
        assertDoesNotThrow(() -> revisiones.insertar(revision2));
        assertDoesNotThrow(() -> revisiones.insertar(revision3));
        List<Revision> revisionesVehiculo = revisiones.get(vehiculo1);
        assertEquals(2, revisionesVehiculo.size());
        assertEquals(revision1, revisionesVehiculo.get(0));
        assertSame(revision1, revisionesVehiculo.get(0));
        assertEquals(revision3, revisionesVehiculo.get(1));
        assertSame(revision3,revisionesVehiculo.get(1));
    }

    @Test
    void insertarRevisionValidaInsertaCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertEquals(revision1, revisiones.buscar(revision1));
        assertSame(revision1, revisiones.buscar(revision1));
    }

    @Test
    void insertarRevisionNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.insertar(null));
        assertEquals("No se puede insertar una revisión nula.", npe.getMessage());
    }

    @Test
    void insertarRevisionClienteRevisionAbiertaLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.insertar(revision2));
        assertEquals("El cliente tiene otra revisión en curso.", onse.getMessage());
    }

    @Test
    void insertarRevisionVehiculoRevisionAbiertaLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.insertar(revision3));
        assertEquals("El vehículo está actualmente en revisión.", onse.getMessage());
    }

    @Test
    void insertarRevisionClienteRevisionAnteiorLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.cerrar(revision1, anteayer));
        when(revision1.getFechaInicio()).thenReturn(ayer);
        when(revision1.getFechaFin()).thenReturn(anteayer);
        when(revision1.estaCerrada()).thenReturn(true);
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.cerrar(revision1, ayer));
        when(revision1.getFechaFin()).thenReturn(ayer);
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.insertar(revision2));
        assertEquals("El cliente tiene una revisión posterior.", onse.getMessage());
    }

    @Test
    void insertarRevisionVehiculoRevisionAnteriorLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.cerrar(revision1, anteayer));
        when(revision1.getFechaInicio()).thenReturn(ayer);
        when(revision1.getFechaFin()).thenReturn(anteayer);
        when(revision1.estaCerrada()).thenReturn(true);
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.cerrar(revision1, ayer));
        when(revision1.getFechaFin()).thenReturn(ayer);
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.insertar(revision3));
        assertEquals("El vehículo tiene una revisión posterior.", onse.getMessage());
    }

    @Test
    void anadirHorasRevisionValidaHorasValidasAnadeHorasCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.anadirHoras(revision1, 10));
        when(revision1.getHoras()).thenReturn(10);
        Revision revision = revisiones.buscar(revision1);
        assertEquals(10, revision.getHoras());
    }

    @Test
    void anadirHorasRevisionNulaHorasValidasLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.anadirHoras(null, 10));
        assertEquals("No puedo operar sobre una revisión nula.", npe.getMessage());
    }

    @Test
    void anadirHorasRevisionNoExistenteHorasValidasLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.anadirHoras(revision1, 10));
        assertEquals("No existe ninguna revisión igual.", onse.getMessage());
    }

    @Test
    void anadirPrecioMaterialRevisionValidaPrecioMaterialValidoAnadaPrecioMaterialCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.anadirPrecioMaterial(revision1, 100f));
        when(revision1.getPrecioMaterial()).thenReturn(100f);
        Revision revision = revisiones.buscar(revision1);
        assertEquals(100f, revision.getPrecioMaterial());
    }

    @Test
    void anadirPrecioMaterialRevisionNulaPrecioMaterialValidoLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.cerrar(null, ayer));
        assertEquals("No puedo operar sobre una revisión nula.", npe.getMessage());
    }

    @Test
    void anadirPrecioMaterialRevisionNoExistentePrecioMaterialValidoLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.anadirPrecioMaterial(revision1, 100f));
        assertEquals("No existe ninguna revisión igual.", onse.getMessage());
    }

    @Test
    void anadirPrecioMaterialRevisionValidaPrecioMaterialValidoAnadePrecioMaterialCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.anadirPrecioMaterial(revision1, 100f));
        when(revision1.getPrecioMaterial()).thenReturn(100f);
        Revision revision = revisiones.buscar(revision1);
        assertEquals(100f, revision.getPrecioMaterial());
    }

    @Test
    void cerrarRevisionNulaFechaValidaLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.cerrar(null, ayer));
        assertEquals("No puedo operar sobre una revisión nula.", npe.getMessage());
    }

    @Test
    void cerrarRevisionNoExistenteFechaValidaLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.cerrar(revision1, hoy));
        assertEquals("No existe ninguna revisión igual.", onse.getMessage());
    }

    @Test
    void cerrarRevisionValidaFechaValidaCierraCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.cerrar(revision1, ayer));
        when(revision1.getFechaFin()).thenReturn(ayer);
        Revision revision = revisiones.buscar(revision1);
        assertEquals(ayer, revision.getFechaFin());
    }

    @Test
    void borrarRevisionExistenteBorraRevisionCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertDoesNotThrow(() -> revisiones.borrar(revision1));
        assertNull(revisiones.buscar(revision1));
    }

    @Test
    void borrarRevisionNoExistenteLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revisiones.borrar(revision2));
        assertEquals("No existe ninguna revisión igual.", onse.getMessage());
    }

    @Test
    void borrarRevisionNulaLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.borrar(null));
        assertEquals("No se puede borrar una revisión nula.", npe.getMessage());
    }

    @Test
    void buscarRevisionExistenteDevuelveRevisionCorrectamente() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        assertEquals(revision1, revisiones.buscar(revision1));
        assertSame(revision1, revisiones.buscar(revision1));
    }

    @Test
    void busarRevisionNoExistenteDevuelveRevisionNula() {
        assertNull(revisiones.buscar(revision1));
    }

    @Test
    void buscarRevisionNulaLanzaExcepcion() {
        assertDoesNotThrow(() -> revisiones.insertar(revision1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revisiones.buscar(null));
        assertEquals("No se puede buscar una revisión nula.", npe.getMessage());
    }
}