package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.ClienteDto;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.TipoCuenta;
import ar.edu.utn.frbb.tup.model.TipoMoneda;
import ar.edu.utn.frbb.tup.model.TipoPersona;
import ar.edu.utn.frbb.tup.model.exception.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClienteServiceTest {

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private ClienteDto clienteDto;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeAll
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testClienteSuccess() throws ClienteAlreadyExistsException {
        ClienteDto cliente = new ClienteDto();
        cliente.setFechaNacimiento("1978-03-18");
        cliente.setDni(29857643);
        cliente.setTipoPersona(TipoPersona.PERSONA_FISICA.toString());
        Cliente clienteEntity = clienteService.darDeAltaCliente(cliente);

        verify(clienteDao, times(1)).save(clienteEntity);
    }

    @Test
    public void testClienteAlreadyExistsException() throws ClienteAlreadyExistsException {
        ClienteDto pepeRino = new ClienteDto();
        pepeRino.setDni(26456437);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento("1978-03-18");
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456437, false)).thenReturn(new Cliente());

        assertThrows(ClienteAlreadyExistsException.class, () -> clienteService.darDeAltaCliente(pepeRino));
    }



    @Test
    public void testAgregarCuentaAClienteSuccess() throws TipoCuentaAlreadyExistsException {
        Cliente pepeRino = new Cliente();
        pepeRino.setDni(26456439);
        pepeRino.setNombre("Pepe");
        pepeRino.setApellido("Rino");
        pepeRino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        pepeRino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        
        cuenta.setTitular(pepeRino.getDni());

        when(clienteDao.find(26456439, true)).thenReturn(pepeRino);

        clienteService.agregarCuenta(cuenta);

        verify(clienteDao, times(1)).save(pepeRino);

        assertEquals(1, pepeRino.getCuentas().size());
        assertEquals(pepeRino.getDni(), cuenta.getTitular());

    }


    @Test
    public void testAgregarCuentaAClienteDuplicada() throws TipoCuentaAlreadyExistsException {
        Cliente luciano = new Cliente();
        luciano.setDni(26456439);
        luciano.setNombre("Pepe");
        luciano.setApellido("Rino");
        luciano.setFechaNacimiento(LocalDate.of(1978, 3,25));
        luciano.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuenta.setTitular(luciano.getDni());

        when(clienteDao.find(26456439, true)).thenReturn(luciano);

        clienteService.agregarCuenta(cuenta);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(500000)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuenta2.setTitular(luciano.getDni());

        assertThrows(TipoCuentaAlreadyExistsException.class, () -> clienteService.agregarCuenta(cuenta2));
        verify(clienteDao, times(1)).save(luciano);
        assertEquals(1, luciano.getCuentas().size());
        assertEquals(luciano.getDni(), cuenta.getTitular());

    }

    @Test
    public void testAgregarDosCuentasCajaAhorroYCuentaCorrienteEnPesos() throws TipoCuentaAlreadyExistsException {
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuenta1.setTitular(peperino.getDni());

        when(clienteDao.find(26456439, true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuenta1);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);

        cuenta2.setTitular(peperino.getDni());

        assertDoesNotThrow(() -> clienteService.agregarCuenta(cuenta2), "Las cuentas son iguales.");
        verify(clienteDao, times(2)).save(peperino);
        assertEquals(2, peperino.getCuentas().size());
        assertEquals(TipoCuenta.CAJA_AHORRO, cuenta1.getTipoCuenta());
        assertEquals(TipoCuenta.CUENTA_CORRIENTE, cuenta2.getTipoCuenta());
        assertEquals(peperino.getDni(), cuenta1.getTitular());
        assertEquals(peperino.getDni(), cuenta2.getTitular());
    }


    @Test
    public void testAgregarDosCuentasCajaAhorroEnPesosYDolares() throws TipoCuentaAlreadyExistsException {
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        Cuenta cuenta1 = new Cuenta()
                .setMoneda(TipoMoneda.PESOS)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuenta1.setTitular(peperino.getDni());

        when(clienteDao.find(26456439, true)).thenReturn(peperino);

        clienteService.agregarCuenta(cuenta1);

        Cuenta cuenta2 = new Cuenta()
                .setMoneda(TipoMoneda.DOLARES)
                .setBalance(100)
                .setTipoCuenta(TipoCuenta.CAJA_AHORRO);

        cuenta2.setTitular(peperino.getDni());

        assertDoesNotThrow(() -> clienteService.agregarCuenta(cuenta2), "Las cuentas son iguales");
        verify(clienteDao, times(2)).save(peperino);
        assertEquals(2, peperino.getCuentas().size());
        assertEquals(TipoCuenta.CAJA_AHORRO, cuenta1.getTipoCuenta());
        assertEquals(TipoMoneda.PESOS, cuenta1.getMoneda());
        assertEquals(TipoCuenta.CAJA_AHORRO, cuenta2.getTipoCuenta());
        assertEquals(TipoMoneda.DOLARES, cuenta2.getMoneda());
        assertEquals(peperino.getDni(), cuenta1.getTitular());
        assertEquals(peperino.getDni(), cuenta2.getTitular());
    }

    @Test
    public void testBuscarPorDniExito() throws ClienteAlreadyExistsException{
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        when(clienteDao.find(26456439, true)).thenReturn(peperino);
        assertEquals(peperino, clienteService.buscarClientePorDni(26456439));
        verify(clienteDao, times(1)).find(26456439, true);
    }

    @Test
    public void testBuscarPorDniFallo() throws ClienteAlreadyExistsException{
        Cliente peperino = new Cliente();
        peperino.setDni(26456439);
        peperino.setNombre("Pepe");
        peperino.setApellido("Rino");
        peperino.setFechaNacimiento(LocalDate.of(1978, 3,25));
        peperino.setTipoPersona(TipoPersona.PERSONA_FISICA);

        assertThrows(IllegalArgumentException.class, () -> clienteService.buscarClientePorDni(123456789));
    }
}
