package org.iesalandalus.programacion.tallermecanico.modelo;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.*;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.FabricaFuenteDatos;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IClientes;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ITrabajos;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.IVehiculos;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.Clientes;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.Trabajos;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.ficheros.Vehiculos;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModeloTest {

    @Mock
    private static IClientes clientes;
    @Mock
    private static IVehiculos vehiculos;
    @Mock
    private static ITrabajos trabajos;
    @InjectMocks
    private Modelo modelo = FabricaModelo.CASCADA.crear(FabricaFuenteDatos.FICHEROS);

    private static Cliente cliente;
    private static Vehiculo vehiculo;
    private static Revision revision;
    private static Mecanico mecanico;

    private AutoCloseable procesadorAnotaciones;
    private MockedConstruction<Cliente> controladorCreacionMockCliente;
    private MockedConstruction<Clientes> controladorCreacionMockClientes;
    private MockedConstruction<Vehiculos> controladorCreacionMockVehiculos;
    private MockedConstruction<Revision> controladorCreacionMockRevision;
    private MockedConstruction<Mecanico> controladorCreacionMockMecanico;
    private MockedConstruction<Trabajos> controladorCreacionMockTrabajos;


    @BeforeAll
    public static void setup() {
        cliente = mock();
        when(cliente.getNombre()).thenReturn("Bob Esponja");
        when(cliente.getDni()).thenReturn("11223344B");
        when(cliente.getTelefono()).thenReturn("950112233");
        vehiculo = mock();
        when(vehiculo.marca()).thenReturn("Seat");
        when(vehiculo.modelo()).thenReturn("León");
        when(vehiculo.matricula()).thenReturn("1234BCD");
        revision = mock();
        when(revision.getCliente()).thenReturn(cliente);
        when(revision.getVehiculo()).thenReturn(vehiculo);
        when(revision.getFechaInicio()).thenReturn(LocalDate.now().minusDays(1));
        mecanico = mock();
        when(mecanico.getCliente()).thenReturn(cliente);
        when(mecanico.getVehiculo()).thenReturn(vehiculo);
        when(mecanico.getFechaInicio()).thenReturn(LocalDate.now().minusDays(1));
    }

    @BeforeEach
    void init() {
        controladorCreacionMockCliente = mockConstruction(Cliente.class);
        controladorCreacionMockClientes = mockConstruction(Clientes.class);
        controladorCreacionMockVehiculos = mockConstruction(Vehiculos.class);
        controladorCreacionMockRevision = mockConstruction(Revision.class);
        controladorCreacionMockMecanico = mockConstruction(Mecanico.class);
        controladorCreacionMockTrabajos = mockConstruction(Trabajos.class);
        procesadorAnotaciones = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        procesadorAnotaciones.close();
        controladorCreacionMockCliente.close();
        controladorCreacionMockClientes.close();
        controladorCreacionMockVehiculos.close();
        controladorCreacionMockRevision.close();
        controladorCreacionMockMecanico.close();
        controladorCreacionMockTrabajos.close();
    }

    @Test
    void comenzarNoHaceNada() {
        assertDoesNotThrow(() -> modelo.comenzar());
    }

    @Test
    void terminarNoHaceNada() {
        assertDoesNotThrow(() -> modelo.terminar());
    }

    @Test
    void insertarClienteLlamaClientesInsertar() {
        assertDoesNotThrow(() -> modelo.insertar(cliente));
        assertDoesNotThrow(() -> verify(clientes).insertar(any(Cliente.class)));
        assertDoesNotThrow(() -> verify(clientes, times(0)).insertar(cliente));
    }

    @Test
    void insertarVehiculoLlamaVehiculosInsertar() {
        assertDoesNotThrow(() -> modelo.insertar(vehiculo));
        assertDoesNotThrow(() -> verify(vehiculos).insertar(vehiculo));
    }

    @Test
    void insertarTrabajoRevisionLlamaClientesBuscarVehiculosBuscarTrabajosInsertar() {
        InOrder orden = inOrder(clientes, vehiculos, trabajos);
        when(clientes.buscar(cliente)).thenReturn(cliente);
        when(vehiculos.buscar(vehiculo)).thenReturn(vehiculo);
        assertDoesNotThrow(() -> modelo.insertar(revision));
        orden.verify(clientes).buscar(cliente);
        orden.verify(vehiculos).buscar(vehiculo);
        assertDoesNotThrow(() -> orden.verify(trabajos).insertar(any(Trabajo.class)));
        assertDoesNotThrow(() -> verify(trabajos, times(0)).insertar(revision));
    }

    @Test
    void insertarTrabajoMecanicoLlamaClientesBuscarVehiculosBuscarTrabajosInsertar() {
        InOrder orden = inOrder(clientes, vehiculos, trabajos);
        when(clientes.buscar(cliente)).thenReturn(cliente);
        when(vehiculos.buscar(vehiculo)).thenReturn(vehiculo);
        assertDoesNotThrow(() -> modelo.insertar(mecanico));
        orden.verify(clientes).buscar(cliente);
        orden.verify(vehiculos).buscar(vehiculo);
        assertDoesNotThrow(() -> orden.verify(trabajos).insertar(any(Trabajo.class)));
        assertDoesNotThrow(() -> verify(trabajos, times(0)).insertar(mecanico));
    }

    @Test
    void buscarClienteLlamaClientesBuscar() {
        assertDoesNotThrow(() -> modelo.insertar(cliente));
        when(clientes.buscar(cliente)).thenReturn(cliente);
        Cliente clienteEncontrado = modelo.buscar(cliente);
        verify(clientes).buscar(cliente);
        assertNotSame(cliente, clienteEncontrado);
    }

    @Test
    void buscarVehiculoLlamaVehiculosBuscar() {
        assertDoesNotThrow(() -> modelo.insertar(vehiculo));
        when(vehiculos.buscar(vehiculo)).thenReturn(vehiculo);
        modelo.buscar(vehiculo);
        verify(vehiculos).buscar(vehiculo);
    }

    @Test
    void buscarTrabajoLlamaTrabajosBuscar() {
        assertDoesNotThrow(() -> modelo.insertar(revision));
        when(trabajos.buscar(revision)).thenReturn(revision);
        Trabajo trabajoEncontrada = modelo.buscar(revision);
        verify(trabajos).buscar(revision);
        assertNotSame(revision, trabajoEncontrada);
    }

    @Test
    void modificarClienteLlamaClientesModificar() {
        assertDoesNotThrow(() -> modelo.modificar(cliente, "Patricio Estrella", "950123456"));
        assertDoesNotThrow(() -> verify(clientes).modificar(cliente, "Patricio Estrella", "950123456"));
    }

    @Test
    void anadirHorasLlamaTrabajosAnadirHoras() {
        assertDoesNotThrow(() -> modelo.anadirHoras(revision, 10));
        assertDoesNotThrow(() -> verify(trabajos).anadirHoras(revision, 10));
    }

    @Test
    void anadirPrecioMateriaLlamaTrabajosAnadirPrecioMaterial() {
        assertDoesNotThrow(() -> modelo.anadirPrecioMaterial(revision, 100f));
        assertDoesNotThrow(() -> verify(trabajos).anadirPrecioMaterial(revision, 100f));
    }

    @Test
    void cerrarLlamaTrabajosCerrar() {
        assertDoesNotThrow(() -> modelo.cerrar(revision, LocalDate.now()));
        assertDoesNotThrow(() -> verify(trabajos).cerrar(revision, LocalDate.now()));
    }

    @Test
    void borrarClienteLlamaTrabajosGetClienteTrabajosBorrarClientesBorrar() {
        simularClientesConTrabajos();
        InOrder orden = inOrder(clientes, trabajos);
        assertDoesNotThrow(() -> modelo.borrar(cliente));
        orden.verify(trabajos).get(cliente);
        for (Trabajo trabajo : trabajos.get(cliente)) {
            assertDoesNotThrow(() -> orden.verify(trabajos).borrar(trabajo));
        }
        assertDoesNotThrow(() -> orden.verify(clientes).borrar(cliente));
    }

    private void simularClientesConTrabajos() {
        when(trabajos.get(cliente)).thenReturn(new ArrayList<>(List.of(mock(), mock())));
    }

    @Test
    void borrarVehiculoLlamaTrabajosGetVehiculoTrabajosBorrarVehiculosBorrar() {
        simularVehiculosConTrabajos();
        InOrder orden = inOrder(vehiculos, trabajos);
        assertDoesNotThrow(() -> modelo.borrar(vehiculo));
        orden.verify(trabajos).get(vehiculo);
        for (Trabajo trabajo : trabajos.get(vehiculo)) {
            assertDoesNotThrow(() -> orden.verify(trabajos).borrar(trabajo));
        }
        assertDoesNotThrow(() -> orden.verify(vehiculos).borrar(vehiculo));
    }

    private void simularVehiculosConTrabajos() {
        when(trabajos.get(vehiculo)).thenReturn(new ArrayList<>(List.of(mock(), mock())));
    }

    @Test
    void borrarTrabajoLlamaTrabajosBorrar() {
        assertDoesNotThrow(() -> modelo.borrar(revision));
        assertDoesNotThrow(() -> verify(trabajos).borrar(revision));
    }

    @Test
    void getClientesLlamaClientesGet() {
        when(clientes.get()).thenReturn(new ArrayList<>(List.of(cliente)));
        List<Cliente> clientesExistentes = modelo.getClientes();
        verify(clientes).get();
        assertNotSame(cliente, clientesExistentes.get(0));
    }

    @Test
    void getVehiculosLlamaVehiculosGet() {
        when(vehiculos.get()).thenReturn(new ArrayList<>(List.of(vehiculo)));
        List<Vehiculo> vehiculosExistentes = modelo.getVehiculos();
        verify(vehiculos).get();
        assertSame(vehiculo, vehiculosExistentes.get(0));
    }

    @Test
    void getTrabajosLlamaTrabajosGet() {
        when(trabajos.get()).thenReturn(new ArrayList<>(List.of(revision)));
        List<Trabajo> trabajosExistentes = modelo.getTrabajos();
        verify(trabajos).get();
        assertNotSame(revision, trabajosExistentes.get(0));
    }

    @Test
    void getTrabajosClienteLlamaTrabajosGetCliente() {
        when(trabajos.get(cliente)).thenReturn(new ArrayList<>(List.of(revision)));
        List<Trabajo> trabajosCliente = modelo.getTrabajos(cliente);
        verify(trabajos).get(cliente);
        assertNotSame(revision, trabajosCliente.get(0));
    }

    @Test
    void getTrabajosVehiculoLlamaTrabajosGetVehiculo() {
        when(trabajos.get(vehiculo)).thenReturn(new ArrayList<>(List.of(revision)));
        List<Trabajo> trabajosVehiculo = modelo.getTrabajos(vehiculo);
        verify(trabajos).get(vehiculo);
        assertNotSame(revision, trabajosVehiculo.get(0));
    }

    @Test
    void getEstadisticasMensualesLlamaTrabajosGetEstadisticasMensuales() {
        when(trabajos.getEstadisticasMensuales(LocalDate.now())).thenReturn(new EnumMap<>(TipoTrabajo.class));
        modelo.getEstadisticasMensuales(LocalDate.now());
        verify(trabajos).getEstadisticasMensuales(LocalDate.now());
    }

}