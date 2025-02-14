package ar.edu.utn.frbb.tup.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.service.ClienteService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteControllerTest {
    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = getCliente();

        doNothing().when(clienteValidator).validate(clienteDto);
        when(clienteService.darDeAltaCliente(clienteDto)).thenReturn(cliente);

        Cliente clienteControl = clienteController.crearCliente(clienteDto);

        assertEquals(cliente, clienteControl);

        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

     @Test
    public void testCrearClienteFail() throws ClienteAlreadyExistsException {
        ClienteDto clienteDto = getClienteDto();
        Cliente cliente = getCliente();

        doThrow(new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni())).when(clienteService).darDeAltaCliente(clienteDto);

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteController.crearCliente(clienteDto));

        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).darDeAltaCliente(clienteDto);
    }

    @Test
    public void testRetornarClienteSuccess() {
        long dni = 12345678;
        Cliente cliente = getCliente();

        when(clienteService.buscarClientePorDni(dni)).thenReturn(cliente);

        Cliente clienteControl = clienteController.retornarCliente(dni);

        assertEquals(cliente, clienteControl);

        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    @Test
    public void testRetornarClienteFail() throws IllegalArgumentException {
        long dni = 12345678L;

        doThrow(new IllegalArgumentException("El cliente no existe")).when(clienteService).buscarClientePorDni(dni);

        assertThrows(IllegalArgumentException.class, () -> clienteController.retornarCliente(dni));

        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

     @Test
    public void testRetornarClientesSuccess() {
        List<Cliente> clientes = new ArrayList<>(List.of(getCliente(), getCliente()));

        when(clienteService.getClientes()).thenReturn(clientes);

        List<Cliente> clientesControl = clienteController.retornarClientes();

        assertEquals(clientes, clientesControl);

        verify(clienteService, times(1)).getClientes();
    }

    @Test
    public void testEliminarClienteSuccess() {
        long dni = 12345678;
        Cliente cliente = getCliente();

        when(clienteService.eliminarCliente(dni)).thenReturn(cliente);

        Cliente clienteControl = clienteController.eliminarCliente(dni);

        assertEquals(cliente, clienteControl);

        verify(clienteService, times(1)).eliminarCliente(dni);
    }

    @Test
    public void testDeleteClienteFail() {
        long dni = 12345678;

        doThrow(new IllegalArgumentException("El cliente no existe")).when(clienteService).eliminarCliente(dni);

        assertThrows(IllegalArgumentException.class, () -> clienteController.eliminarCliente(dni));

        verify(clienteService, times(1)).eliminarCliente(dni);
    }


    private Cliente getCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(12345678);
        cliente.setNombre("Juan");
        cliente.setApellido("Perez");
        cliente.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA);
        cliente.setBanco("Banco Prueba");
        return cliente;
    }

    private ClienteDto getClienteDto() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento(("2000-1-1"));
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Prueba");
        return clienteDto;
    }

}
