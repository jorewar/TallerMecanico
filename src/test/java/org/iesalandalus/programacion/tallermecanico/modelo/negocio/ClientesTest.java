package org.iesalandalus.programacion.tallermecanico.modelo.negocio;

import org.iesalandalus.programacion.tallermecanico.modelo.dominio.Cliente;
import org.iesalandalus.programacion.tallermecanico.modelo.negocio.memoria.Clientes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientesTest {

    private static Cliente cliente1;
    private static Cliente cliente2;
    private IClientes clientes;

    @BeforeEach
    void init() {
        clientes = new Clientes();
        cliente1 = mock();
        when(cliente1.getDni()).thenReturn("11223344B");
        cliente2 = mock();
        when(cliente2.getDni()).thenReturn("11111111H");
    }

    @Test
    void constructorCreaClientesCorrectamente() {
        assertNotNull(clientes);
        assertEquals(0, clientes.get().size());
    }

    @Test
    void getDevuelveClientesCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.insertar(cliente2));
        List<Cliente> copiaClientes = clientes.get();
        assertEquals(cliente1, copiaClientes.get(0));
        assertSame(cliente1, copiaClientes.get(0));
        assertEquals(cliente2, copiaClientes.get(1));
        assertSame(cliente2, copiaClientes.get(1));
    }

    @Test
    void insertarClienteValidoInsertaCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertEquals(cliente1, clientes.buscar(cliente1));
        assertSame(cliente1, clientes.buscar(cliente1));
    }

    @Test
    void insertarClienteNuloLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.insertar(null));
        assertEquals("No se puede insertar un cliente nulo.", npe.getMessage());
    }

    @Test
    void insertarClienteRepetidoLanzaExcepcion() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.insertar(cliente1));
        assertEquals("Ya existe un cliente con ese DNI.", onse.getMessage());
    }

    @Test
    void borrarClienteExistenteBorraClienteCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.borrar(cliente1));
        assertNull(clientes.buscar(cliente1));
    }

    @Test
    void borrarClienteNoExistenteLanzaExcepcion() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.borrar(cliente2));
        assertEquals("No existe ningún cliente con ese DNI.", onse.getMessage());
    }

    @Test
    void borrarClienteNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.borrar(null));
        assertEquals("No se puede borrar un cliente nulo.", npe.getMessage());
    }

    @Test
    void busarClienteExistenteDevuelveClienteCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertEquals(cliente1, clientes.buscar(cliente1));
        assertSame(cliente1, clientes.buscar(cliente1));
    }

    @Test
    void busarClienteNoExistenteDevuelveClienteNulo() {
        assertNull(clientes.buscar(cliente1));
    }

    @Test
    void buscarClienteNuloLanzaExcepcion() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.buscar(null));
        assertEquals("No se puede buscar un cliente nulo.", npe.getMessage());
    }

    @Test
    void modificarClienteExistenteNombreValidoTelefonoValidoModificaClienteCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.modificar(cliente1, "Patricio Estrella", "950123456"));
        verify(cliente1).setNombre("Patricio Estrella");
        verify(cliente1).setTelefono("950123456");
    }

    @Test
    void modificarClienteExistenteNombreNuloTelefonoValidoModificaClienteCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.modificar(cliente1, null, "950123456"));
        verify(cliente1, never()).setNombre(any());
        verify(cliente1).setTelefono("950123456");
    }

    @Test
    void modificarClienteExistenteNombreValidoTelefonoNuloModificaClienteCorrectamente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.modificar(cliente1, "Patricio Estrella", null));
        verify(cliente1).setNombre("Patricio Estrella");
        verify(cliente1, never()).setTelefono(any());
    }

    @Test
    void modificarClienteExistenteNombreNuloTelefonoNuloNoModificaCliente() {
        assertDoesNotThrow(() -> clientes.insertar(cliente1));
        assertDoesNotThrow(() -> clientes.modificar(cliente1, null, null));
        verify(cliente1, never()).setNombre(any());
        verify(cliente1, never()).setTelefono(any());
    }

    @Test
    void modificarClienteNoExistenteNombreValidoTelefonoValidoLanzaExcepcion() {
        OperationNotSupportedException onse = assertThrows(OperationNotSupportedException.class, () -> clientes.modificar(cliente1, "Patricio Estrella", "950123456"));
        assertEquals("No existe ningún cliente con ese DNI.", onse.getMessage());
    }

    @Test
    void modificarClienteNuloNombreValidoTelefonoValidoLanzaExcepcion() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> clientes.modificar(null, "Patricio Estrella", "950123456"));
        assertEquals("No se puede modificar un cliente nulo.", npe.getMessage());
    }

}