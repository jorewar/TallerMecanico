package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedConstruction;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrabajoTest {

    private static Cliente cliente;
    private static Vehiculo vehiculo;
    private static LocalDate hoy;
    private static LocalDate ayer;
    private static LocalDate manana;
    private static LocalDate semanaPasada;

    private Revision revision;

    private MockedConstruction<Cliente> controladorCreacionMockCliente;

    @BeforeAll
    static void setup() {
        hoy = LocalDate.now();
        ayer = hoy.minusDays(1);
        manana = hoy.plusDays(1);
        semanaPasada = hoy.minusDays(7);
    }

    @BeforeEach
    void init() {
        creaComportamientoCliente();
        creaComportamientoVehiculo();
        revision = new Revision(cliente, vehiculo, ayer);
    }

    @AfterEach
    void close() {
        controladorCreacionMockCliente.close();
    }

    private void creaComportamientoVehiculo() {
        vehiculo = mock();
        when(vehiculo.marca()).thenReturn("Seat");
        when(vehiculo.modelo()).thenReturn("León");
        when(vehiculo.matricula()).thenReturn("1234BCD");
    }

    private void creaComportamientoCliente() {
        cliente = mock();
        controladorCreacionMockCliente = mockConstruction(Cliente.class);
        when(cliente.getNombre()).thenReturn("Bob Esponja");
        when(cliente.getDni()).thenReturn("11223344B");
        when(cliente.getTelefono()).thenReturn("950112233");
    }

    @Test
    void constructorClienteValidoVehiculoValidoFechaInicioValidaCreaRevisionCorrectamente() {
        assertEquals(cliente, revision.getCliente());
        assertSame(cliente, revision.getCliente());
        assertEquals(vehiculo, revision.getVehiculo());
        assertSame(vehiculo, revision.getVehiculo());
        assertEquals(ayer, revision.getFechaInicio());
        assertNull(revision.getFechaFin());
        assertEquals(0, revision.getHoras());
        assertEquals(0, revision.getPrecio());
        Revision revisonSemanaPasada = new Revision(cliente, vehiculo, semanaPasada);
        assertEquals(semanaPasada, revisonSemanaPasada.getFechaInicio());
    }

    @Test
    void constructorClienteNuloVehiculoValidoFechaInicioValidaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Revision(null, vehiculo, hoy));
        assertEquals("El cliente no puede ser nulo.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoNuloFechaInicioValidaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Revision(cliente, null, hoy));
        assertEquals("El vehículo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoValidoFechaInicioNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Revision(cliente, vehiculo, null));
        assertEquals("La fecha de inicio no puede ser nula.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoValidoFechaInicioNoValidaLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Revision(cliente, vehiculo, manana));
        assertEquals("La fecha de inicio no puede ser futura.", iae.getMessage());
    }

    @Test
    void constructorTrabajoValidoCopiaTrabajoCorrectamente() {
        assertDoesNotThrow(() -> revision.anadirHoras(5));
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        Revision copiaRevision = new Revision(revision);
        assertNotSame(cliente, copiaRevision.getCliente());
        assertSame(vehiculo, copiaRevision.getVehiculo());
        assertEquals(ayer, copiaRevision.getFechaInicio());
        assertEquals(hoy, copiaRevision.getFechaFin());
        assertEquals(5, copiaRevision.getHoras());
        Mecanico mecanico = new Mecanico(cliente, vehiculo, ayer);
        assertDoesNotThrow(() -> mecanico.anadirHoras(5));
        assertDoesNotThrow(() -> mecanico.anadirPrecioMaterial(100));
        assertDoesNotThrow(() -> mecanico.cerrar(hoy));
        Mecanico copiaMecanico = new Mecanico(mecanico);
        assertNotSame(cliente, copiaMecanico.getCliente());
        assertSame(vehiculo, copiaMecanico.getVehiculo());
        assertEquals(ayer, copiaMecanico.getFechaInicio());
        assertEquals(hoy, copiaMecanico.getFechaFin());
        assertEquals(5, copiaMecanico.getHoras());
        assertEquals(100, copiaMecanico.getPrecioMaterial());
    }

    @Test
    void constructorTrabajoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Revision(null));
        assertEquals("El trabajo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void copiarTrabajoValidoCopiaTrabajoCorrectamente() {
        assertDoesNotThrow(() -> revision.anadirHoras(5));
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        Revision copiaRevision = (Revision) Trabajo.copiar(revision);
        assertNotSame(cliente, copiaRevision.getCliente());
        assertSame(vehiculo, copiaRevision.getVehiculo());
        assertEquals(ayer, copiaRevision.getFechaInicio());
        assertEquals(hoy, copiaRevision.getFechaFin());
        assertEquals(5, copiaRevision.getHoras());
        Mecanico mecanico = new Mecanico(cliente, vehiculo, ayer);
        assertDoesNotThrow(() -> mecanico.anadirHoras(5));
        assertDoesNotThrow(() -> mecanico.anadirPrecioMaterial(100));
        assertDoesNotThrow(() -> mecanico.cerrar(hoy));
        Mecanico copiaMecanico = (Mecanico) Trabajo.copiar(mecanico);
        assertNotSame(cliente, copiaMecanico.getCliente());
        assertSame(vehiculo, copiaMecanico.getVehiculo());
        assertEquals(ayer, copiaMecanico.getFechaInicio());
        assertEquals(hoy, copiaMecanico.getFechaFin());
        assertEquals(5, copiaMecanico.getHoras());
        assertEquals(100, copiaMecanico.getPrecioMaterial());
    }

    @Test
    void copiarTrabajoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> Trabajo.copiar(null));
        assertEquals("El trabajo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void getTrabajoVehiculoValidoDevuelveTrabajoConDichoVehiculo() {
        Trabajo trabajo = Trabajo.get(revision.getVehiculo());
        assertEquals(revision.getVehiculo(), trabajo.getVehiculo());
    }

    @Test
    void getTrabajoVehiculoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> Trabajo.get(null));
        assertEquals("El vehículo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void anadirHorasHorasValidasSumaHorasCorrectamente() {
        assertDoesNotThrow(() -> revision.anadirHoras(5));
        assertEquals(5, revision.getHoras());
        assertDoesNotThrow(() -> revision.anadirHoras(5));
        assertEquals(10, revision.getHoras());
    }

    @Test
    void anadirHorasHorasNoValidasLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> revision.anadirHoras(0));
        assertEquals("Las horas a añadir deben ser mayores que cero.", iae.getMessage());
    }

    @Test
    void anadirHorasTrabajoCerradoLanzaExcepcion() {
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revision.anadirHoras(5));
        assertEquals("No se puede añadir horas, ya que el trabajo está cerrado.", onse.getMessage());
    }

    @Test
    void cerrarFechaFinValidaCierraCorrectamente() {
        assertFalse(revision.estaCerrado());
        assertNull(revision.getFechaFin());
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        assertTrue(revision.estaCerrado());
        assertEquals(hoy, revision.getFechaFin());
    }

    @Test
    void cerrarFechaFinNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> revision.cerrar(null));
        assertEquals("La fecha de fin no puede ser nula.", npe.getMessage());
    }

    @Test
    void cerrarFechaFinFuturaLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> revision.cerrar(manana));
        assertEquals("La fecha de fin no puede ser futura.", iae.getMessage());
    }

    @Test
    void cerrarFechaFinAnteriorFechaInicioLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> revision.cerrar(semanaPasada));
        assertEquals("La fecha de fin no puede ser anterior a la fecha de inicio.", iae.getMessage());
    }

    @Test
    void cerrarTrabajonCerradoLanzaExcepcion() {
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> revision.cerrar(hoy));
        assertEquals("El trabajo ya está cerrado.", onse.getMessage());
    }

    @Test
    void equalsHashCodeSeBasanSoloEnClienteVehiculoFechaInicio() {
        Revision otraRevision = new Revision(cliente, vehiculo, ayer);
        assertEquals(revision, otraRevision);
        assertEquals(revision.hashCode(), otraRevision.hashCode());
        assertDoesNotThrow(() -> otraRevision.cerrar(hoy));
        assertEquals(revision, otraRevision);
        assertEquals(revision.hashCode(), otraRevision.hashCode());
    }

}