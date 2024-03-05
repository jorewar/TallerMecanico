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

class MecanicoTest {

    private static Cliente cliente;
    private static Vehiculo vehiculo;
    private static LocalDate hoy;
    private static LocalDate ayer;
    private static LocalDate manana;
    private static LocalDate semanaPasada;

    private Mecanico mecanico;

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
        mecanico = new Mecanico(cliente, vehiculo, ayer);
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
    void constructorClienteValidoVehiculoValidoFechaInicioValidaCreaMecanicoCorrectamente() {
        assertEquals(cliente, mecanico.getCliente());
        assertSame(cliente, mecanico.getCliente());
        assertEquals(vehiculo, mecanico.getVehiculo());
        assertSame(vehiculo, mecanico.getVehiculo());
        assertEquals(ayer, mecanico.getFechaInicio());
        assertNull(mecanico.getFechaFin());
        assertEquals(0, mecanico.getHoras());
        assertEquals(0, mecanico.getPrecioMaterial());
        assertEquals(0, mecanico.getPrecio());
        Mecanico mecanicoSemanaPasada = new Mecanico(cliente, vehiculo, semanaPasada);
        assertEquals(semanaPasada, mecanicoSemanaPasada.getFechaInicio());
    }

    @Test
    void constructorClienteNuloVehiculoValidoFechaInicioValidaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Mecanico(null, vehiculo, hoy));
        assertEquals("El cliente no puede ser nulo.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoNuloFechaInicioValidaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Mecanico(cliente, null, hoy));
        assertEquals("El vehículo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoValidoFechaInicioNulaLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Mecanico(cliente, vehiculo, null));
        assertEquals("La fecha de inicio no puede ser nula.", npe.getMessage());
    }

    @Test
    void constructorClienteValidoVehiculoValidoFechaInicioNoValidaLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> new Mecanico(cliente, vehiculo, manana));
        assertEquals("La fecha de inicio no puede ser futura.", iae.getMessage());
    }

    @Test
    void constructorMecanicoValidoCopiaMecanicoCorrectamente() {
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
    void constructorMecanicoNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new Mecanico(null));
        assertEquals("El trabajo no puede ser nulo.", npe.getMessage());
    }

    @Test
    void anadirHorasHorasValidasSumaHorasCorrectamente() {
        assertDoesNotThrow(() -> mecanico.anadirHoras(5));
        assertEquals(5, mecanico.getHoras());
        assertDoesNotThrow(() -> mecanico.anadirHoras(5));
        assertEquals(10, mecanico.getHoras());
    }

    @Test
    void anadirHorasHorasNoValidasLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> mecanico.anadirHoras(0));
        assertEquals("Las horas a añadir deben ser mayores que cero.", iae.getMessage());
    }

    @Test
    void anadirHorasMecanicoCerradoLanzaExcepcion() {
        assertDoesNotThrow(() -> mecanico.cerrar(hoy));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> mecanico.anadirHoras(5));
        assertEquals("No se puede añadir horas, ya que el trabajo está cerrado.", onse.getMessage());
    }

    @Test
    void anadirPrecioMaterialPrecioMaterialValidoSumaPrecioMaterialCorrectamente() {
        assertDoesNotThrow(() -> mecanico.anadirPrecioMaterial(100));
        assertEquals(100, mecanico.getPrecioMaterial());
        assertDoesNotThrow(() -> mecanico.anadirPrecioMaterial(100));
        assertEquals(200, mecanico.getPrecioMaterial());
    }

    @Test
    void anadirPrecioMaterialPrecioMaterialNoValidoLanzaExcepcion() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> mecanico.anadirPrecioMaterial(0));
        assertEquals("El precio del material a añadir debe ser mayor que cero.", iae.getMessage());
    }

    @Test
    void anadirPrecioMaterialMecanicoCerradaLanzaExcepcion() {
        assertDoesNotThrow(() -> mecanico.cerrar(hoy));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> mecanico.anadirPrecioMaterial(100));
        assertEquals("No se puede añadir precio del material, ya que el trabajo mecánico está cerrado.", onse.getMessage());
    }

    @ParameterizedTest(name = "Cuando llamamos a getPrecio y se han añadido {0} horas y {1} precio de material calcula el precio como {2}")
    @CsvSource({"0, 1, 1, 31.5", "1, 1, 1, 41.5", "5, 1, 1, 81.5", "0, 2, 2, 63.0", "1, 2, 2, 73.0", "5, 2, 2, 113.0",
            "0, 5, 5, 157.5", "1, 5, 5, 167.5", "5, 5, 5, 207.5", "0, 10, 10, 315.0", "1, 10, 10, 325.0", "5, 10, 10, 365.0",
            "0, 10, 100, 450.0", "1, 10, 100, 460.0", "5, 10, 100, 500.0"})
    void getPrecioCalculaCorrectamentePrecio(int dias, int horas, float precioMaterial, float precio) {
        Mecanico mecanicoSemanaPAsada = new Mecanico(cliente, vehiculo, semanaPasada);
        assertDoesNotThrow(() -> mecanicoSemanaPAsada.anadirHoras(horas));
        assertDoesNotThrow(() -> mecanicoSemanaPAsada.anadirPrecioMaterial(precioMaterial));
        LocalDate fechaFin = semanaPasada.plusDays(dias);
        assertDoesNotThrow(() -> mecanicoSemanaPAsada.cerrar(fechaFin));
        assertEquals(precio, mecanicoSemanaPAsada.getPrecio());
    }

    @Test
    void equalsHashCodeSeBasanSoloEnClienteVehiculoFechaInicio() {
        Mecanico otroMecanico = new Mecanico(cliente, vehiculo, ayer);
        assertEquals(mecanico, otroMecanico);
        assertEquals(mecanico.hashCode(), otroMecanico.hashCode());
        assertDoesNotThrow(() -> otroMecanico.cerrar(hoy));
        assertEquals(mecanico, otroMecanico);
        assertEquals(mecanico.hashCode(), otroMecanico.hashCode());
    }

    @Test
    void toStringDevuelveLaCadenaEsperada() {
        String cadenaCliente = "Bob Esponja - 11223344B (950112233)";
        String caenaVehiculo = "Seat León - 1234BCD";
        when(cliente.toString()).thenReturn(cadenaCliente);
        when(vehiculo.toString()).thenReturn(caenaVehiculo);
        String cadenaAyer = ayer.format(Trabajo.FORMATO_FECHA);
        String cadenaHoy = hoy.format(Trabajo.FORMATO_FECHA);
        String cadena = String.format("Mecánico -> %s - %s (%s - ): 0 horas, 0,00 € en material", cadenaCliente, caenaVehiculo, cadenaAyer);
        assertEquals(cadena, mecanico.toString());
        assertDoesNotThrow(() -> mecanico.cerrar(hoy));
        cadena = String.format("Mecánico -> %s - %s (%s - %s): 0 horas, 0,00 € en material, 10,00 € total", cadenaCliente, caenaVehiculo, cadenaAyer, cadenaHoy);
        assertEquals(cadena, mecanico.toString());
    }

}