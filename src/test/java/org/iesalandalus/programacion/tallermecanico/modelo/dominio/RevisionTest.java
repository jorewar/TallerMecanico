package org.iesalandalus.programacion.tallermecanico.modelo.dominio;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedConstruction;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevisionTest {

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
    void constructorRevisionValidaCopiaRevisionCorrectamente() {
        assertDoesNotThrow(() -> revision.anadirHoras(5));
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        Revision revisionCopia = new Revision(revision);
        assertNotSame(cliente, revisionCopia.getCliente());
        assertSame(vehiculo, revisionCopia.getVehiculo());
        assertEquals(ayer, revisionCopia.getFechaInicio());
        assertEquals(hoy, revisionCopia.getFechaFin());
        assertEquals(5, revisionCopia.getHoras());
    }

    @Test
    void constructorRevisionNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Revision(null));
        assertEquals("El trabajo no puede ser nulo.", npe.getMessage());
    }

    @ParameterizedTest(name = "Cuando llamamos a getPrecio y se han añadido {0} horas calcula el precio como {1}")
    @CsvSource({"0, 1, 35.0", "1, 1, 45.0", "5, 1, 85.0", "0, 2, 70.0", "1, 2, 80.0", "5, 2, 120.0",
            "0, 5, 175.0", "1, 5, 185.0", "5, 5, 225.0", "0, 10, 350.0", "1, 10, 360.0", "5, 10, 400.0",
            "0, 10, 350.0", "1, 10, 360.0", "5, 10, 400.0"})
    void getPrecioCalculaCorrectamentePrecio(int dias, int horas, float precio) {
        Revision revisonSemanaPasada = new Revision(cliente, vehiculo, semanaPasada);
        assertDoesNotThrow(() -> revisonSemanaPasada.anadirHoras(horas));
        LocalDate fechaFin = semanaPasada.plusDays(dias);
        assertDoesNotThrow(() -> revisonSemanaPasada.cerrar(fechaFin));
        assertEquals(precio, revisonSemanaPasada.getPrecio());
    }

    @Test
    void toStringDevuelveLaCadenaEsperada() {
        String cadenaCliente = "Bob Esponja - 11223344B (950112233)";
        String caenaVehiculo = "Seat León - 1234BCD";
        when(cliente.toString()).thenReturn(cadenaCliente);
        when(vehiculo.toString()).thenReturn(caenaVehiculo);
        String cadenaAyer = ayer.format(Trabajo.FORMATO_FECHA);
        String cadenaHoy = hoy.format(Trabajo.FORMATO_FECHA);
        String cadena = String.format("Revisión -> %s - %s (%s - ): 0 horas", cadenaCliente, caenaVehiculo, cadenaAyer);
        assertEquals(cadena, revision.toString());
        assertDoesNotThrow(() -> revision.cerrar(hoy));
        cadena = String.format("Revisión -> %s - %s (%s - %s): 0 horas, 10,00 € total", cadenaCliente, caenaVehiculo, cadenaAyer, cadenaHoy);
        assertEquals(cadena, revision.toString());
    }

}